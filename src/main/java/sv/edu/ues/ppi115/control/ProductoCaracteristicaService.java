package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Caracteristica;
import sv.edu.ues.ppi115.entity.Producto;
import sv.edu.ues.ppi115.entity.ProductoCaracteristica;

@Stateless
@LocalBean
public class ProductoCaracteristicaService extends AbstractDataAccess<ProductoCaracteristica> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<ProductoCaracteristica> getEntityClass() {
        return ProductoCaracteristica.class;
    }

    public ProductoCaracteristicaService() {
    }

    /**
     * El JSON solo trae los ids de producto y caracteristica; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(ProductoCaracteristica entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdProducto() != null) {
            entity.setIdProducto(em.getReference(Producto.class,
                    entity.getIdProducto().getIdProducto()));
        }
        if (entity.getIdCaracteristica() != null) {
            entity.setIdCaracteristica(em.getReference(Caracteristica.class,
                    entity.getIdCaracteristica().getIdCaracteristica()));
        }
        super.create(entity);
    }

    public List<ProductoCaracteristica> findByProducto(UUID idProducto, int first, int max) {
        if (idProducto == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createNamedQuery("ProductoCaracteristica.findByProducto", ProductoCaracteristica.class)
                .setParameter("idProducto", idProducto)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public long contarPorProducto(UUID idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(p) FROM ProductoCaracteristica p "
                + "WHERE p.idProducto.idProducto = :idProducto", Long.class)
                .setParameter("idProducto", idProducto)
                .getSingleResult();
    }
}

package sv.edu.ues.ppi115.control;
 
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Orden;
import sv.edu.ues.ppi115.entity.OrdenProducto;
import sv.edu.ues.ppi115.entity.Producto;
 
@Stateless
@LocalBean
public class OrdenProductoService extends AbstractDataAccess<OrdenProducto> {
 
    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 
    @Override
    protected Class<OrdenProducto> getEntityClass() {
        return OrdenProducto.class;
    }
 
    public OrdenProductoService() {
    }
 
    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(OrdenProducto entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdOrden() != null) {
            entity.setIdOrden(em.getReference(Orden.class,
                    entity.getIdOrden().getIdOrden()));
        }
        if (entity.getIdProducto() != null) {
            entity.setIdProducto(em.getReference(Producto.class,
                    entity.getIdProducto().getIdProducto()));
        }
        super.create(entity);
    }
 
    public List<OrdenProducto> findByOrden(UUID idOrden, int first, int max) {
        if (idOrden == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createNamedQuery("OrdenProducto.findByOrden", OrdenProducto.class)
                .setParameter("idOrden", idOrden)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorOrden(UUID idOrden) {
        if (idOrden == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM OrdenProducto x "
                + "WHERE x.idOrden.idOrden = :idOrden", Long.class)
                .setParameter("idOrden", idOrden)
                .getSingleResult();
    }
 
    public List<OrdenProducto> findByProducto(UUID idProducto, int first, int max) {
        if (idProducto == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createNamedQuery("OrdenProducto.findByProducto", OrdenProducto.class)
                .setParameter("idProducto", idProducto)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorProducto(UUID idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM OrdenProducto x "
                + "WHERE x.idProducto.idProducto = :idProducto", Long.class)
                .setParameter("idProducto", idProducto)
                .getSingleResult();
    }
}
 

package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Caracteristica;
import sv.edu.ues.ppi115.entity.TipoCaracteristica;

@Stateless
@LocalBean
public class CaracteristicaService extends AbstractDataAccess<Caracteristica> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Caracteristica> getEntityClass() {
        return Caracteristica.class;
    }

    public CaracteristicaService() {
    }

    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(Caracteristica entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdTipoCaracteristica() != null) {
            entity.setIdTipoCaracteristica(em.getReference(TipoCaracteristica.class,
                    entity.getIdTipoCaracteristica().getIdTipoCaracteristica()));
        }
        super.create(entity);
    }

    public List<Caracteristica> findByTipoCaracteristica(UUID idTipoCaracteristica, int first, int max) {
        if (idTipoCaracteristica == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createNamedQuery("Caracteristica.findByTipoCaracteristica", Caracteristica.class)
                .setParameter("idTipoCaracteristica", idTipoCaracteristica)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public long contarPorTipoCaracteristica(UUID idTipoCaracteristica) {
        if (idTipoCaracteristica == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM Caracteristica x "
                + "WHERE x.idTipoCaracteristica.idTipoCaracteristica = :idTipoCaracteristica", Long.class)
                .setParameter("idTipoCaracteristica", idTipoCaracteristica)
                .getSingleResult();
    }
}
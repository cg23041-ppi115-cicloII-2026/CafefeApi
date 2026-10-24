package sv.edu.ues.ppi115.control;
 
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.EmpleadoRol;
import sv.edu.ues.ppi115.entity.Orden;
 
@Stateless
@LocalBean
public class OrdenService extends AbstractDataAccess<Orden> {
 
    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 
    @Override
    protected Class<Orden> getEntityClass() {
        return Orden.class;
    }
 
    public OrdenService() {
    }
 
    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(Orden entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdEmpleadoRol() != null) {
            entity.setIdEmpleadoRol(em.getReference(EmpleadoRol.class,
                    entity.getIdEmpleadoRol().getIdEmpleadoRol()));
        }
        super.create(entity);
    }
 
    public List<Orden> findByEmpleadoRol(UUID idEmpleadoRol, int first, int max) {
        if (idEmpleadoRol == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM Orden x "
                + "WHERE x.idEmpleadoRol.idEmpleadoRol = :idEmpleadoRol ORDER BY x.idOrden", Orden.class)
                .setParameter("idEmpleadoRol", idEmpleadoRol)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorEmpleadoRol(UUID idEmpleadoRol) {
        if (idEmpleadoRol == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM Orden x "
                + "WHERE x.idEmpleadoRol.idEmpleadoRol = :idEmpleadoRol", Long.class)
                .setParameter("idEmpleadoRol", idEmpleadoRol)
                .getSingleResult();
    }
}
 

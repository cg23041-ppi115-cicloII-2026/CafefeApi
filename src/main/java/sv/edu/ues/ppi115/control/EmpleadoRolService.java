package sv.edu.ues.ppi115.control;
 
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Empleado;
import sv.edu.ues.ppi115.entity.EmpleadoRol;
import sv.edu.ues.ppi115.entity.Rol;
 
@Stateless
@LocalBean
public class EmpleadoRolService extends AbstractDataAccess<EmpleadoRol> {
 
    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 
    @Override
    protected Class<EmpleadoRol> getEntityClass() {
        return EmpleadoRol.class;
    }
 
    public EmpleadoRolService() {
    }
 
    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(EmpleadoRol entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdEmpleado() != null) {
            entity.setIdEmpleado(em.getReference(Empleado.class,
                    entity.getIdEmpleado().getIdEmpleado()));
        }
        if (entity.getIdRol() != null) {
            entity.setIdRol(em.getReference(Rol.class,
                    entity.getIdRol().getIdRol()));
        }
        super.create(entity);
    }
 
    public List<EmpleadoRol> findByEmpleado(UUID idEmpleado, int first, int max) {
        if (idEmpleado == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM EmpleadoRol x "
                + "WHERE x.idEmpleado.idEmpleado = :idEmpleado ORDER BY x.idEmpleadoRol", EmpleadoRol.class)
                .setParameter("idEmpleado", idEmpleado)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorEmpleado(UUID idEmpleado) {
        if (idEmpleado == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM EmpleadoRol x "
                + "WHERE x.idEmpleado.idEmpleado = :idEmpleado", Long.class)
                .setParameter("idEmpleado", idEmpleado)
                .getSingleResult();
    }
 
    public List<EmpleadoRol> findByRol(UUID idRol, int first, int max) {
        if (idRol == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM EmpleadoRol x "
                + "WHERE x.idRol.idRol = :idRol ORDER BY x.idEmpleadoRol", EmpleadoRol.class)
                .setParameter("idRol", idRol)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorRol(UUID idRol) {
        if (idRol == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM EmpleadoRol x "
                + "WHERE x.idRol.idRol = :idRol", Long.class)
                .setParameter("idRol", idRol)
                .getSingleResult();
    }
}
 
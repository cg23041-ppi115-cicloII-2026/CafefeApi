package sv.edu.ues.ppi115.control;
 
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Caja;
import sv.edu.ues.ppi115.entity.EmpleadoRol;
import sv.edu.ues.ppi115.entity.Factura;
 
@Stateless
@LocalBean
public class FacturaService extends AbstractDataAccess<Factura> {
 
    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 
    @Override
    protected Class<Factura> getEntityClass() {
        return Factura.class;
    }
 
    public FacturaService() {
    }
 
    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(Factura entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdCaja() != null) {
            entity.setIdCaja(em.getReference(Caja.class,
                    entity.getIdCaja().getIdCaja()));
        }
        if (entity.getIdEmpleadoRol() != null) {
            entity.setIdEmpleadoRol(em.getReference(EmpleadoRol.class,
                    entity.getIdEmpleadoRol().getIdEmpleadoRol()));
        }
        super.create(entity);
    }
 
    /**
     * Las relaciones de Factura son LAZY: se traen con JOIN FETCH para que
     * el JSON se pueda armar fuera de la transaccion.
     */
    @Override
    public Factura findById(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT x FROM Factura x LEFT JOIN FETCH x.idCaja LEFT JOIN FETCH x.idEmpleadoRol "
                + "WHERE x.idFactura = :id", Factura.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }
 
    @Override
    public List<Factura> findRange(int first, int max) {
        if (first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros de rango invalidos");
        }
        return em.createQuery("SELECT x FROM Factura x LEFT JOIN FETCH x.idCaja LEFT JOIN FETCH x.idEmpleadoRol "
                + "ORDER BY x.idFactura", Factura.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public List<Factura> findByCaja(UUID idCaja, int first, int max) {
        if (idCaja == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM Factura x LEFT JOIN FETCH x.idCaja LEFT JOIN FETCH x.idEmpleadoRol "
                + "WHERE x.idCaja.idCaja = :idCaja ORDER BY x.idFactura", Factura.class)
                .setParameter("idCaja", idCaja)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorCaja(UUID idCaja) {
        if (idCaja == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM Factura x "
                + "WHERE x.idCaja.idCaja = :idCaja", Long.class)
                .setParameter("idCaja", idCaja)
                .getSingleResult();
    }
 
    public List<Factura> findByEmpleadoRol(UUID idEmpleadoRol, int first, int max) {
        if (idEmpleadoRol == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM Factura x LEFT JOIN FETCH x.idCaja LEFT JOIN FETCH x.idEmpleadoRol "
                + "WHERE x.idEmpleadoRol.idEmpleadoRol = :idEmpleadoRol ORDER BY x.idFactura", Factura.class)
                .setParameter("idEmpleadoRol", idEmpleadoRol)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorEmpleadoRol(UUID idEmpleadoRol) {
        if (idEmpleadoRol == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM Factura x "
                + "WHERE x.idEmpleadoRol.idEmpleadoRol = :idEmpleadoRol", Long.class)
                .setParameter("idEmpleadoRol", idEmpleadoRol)
                .getSingleResult();
    }
}
 

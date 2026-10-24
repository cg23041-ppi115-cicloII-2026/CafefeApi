package sv.edu.ues.ppi115.control;
 
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.ppi115.entity.Factura;
import sv.edu.ues.ppi115.entity.Pago;
 
@Stateless
@LocalBean
public class PagoService extends AbstractDataAccess<Pago> {
 
    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 
    @Override
    protected Class<Pago> getEntityClass() {
        return Pago.class;
    }
 
    public PagoService() {
    }
 
    /**
     * El JSON solo trae los ids de las relaciones; se convierten
     * en referencias administradas por JPA antes de guardar.
     */
    @Override
    public void create(Pago entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entity.getIdFactura() != null) {
            entity.setIdFactura(em.getReference(Factura.class,
                    entity.getIdFactura().getIdFactura()));
        }
        super.create(entity);
    }
 
    /**
     * Las relaciones de Pago son LAZY: se traen con JOIN FETCH para que
     * el JSON se pueda armar fuera de la transaccion.
     */
    @Override
    public Pago findById(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT x FROM Pago x LEFT JOIN FETCH x.idFactura f LEFT JOIN FETCH f.idCaja LEFT JOIN FETCH f.idEmpleadoRol "
                + "WHERE x.idPago = :id", Pago.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }
 
    @Override
    public List<Pago> findRange(int first, int max) {
        if (first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros de rango invalidos");
        }
        return em.createQuery("SELECT x FROM Pago x LEFT JOIN FETCH x.idFactura f LEFT JOIN FETCH f.idCaja LEFT JOIN FETCH f.idEmpleadoRol "
                + "ORDER BY x.idPago", Pago.class)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public List<Pago> findByFactura(UUID idFactura, int first, int max) {
        if (idFactura == null || first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros invalidos");
        }
        return em.createQuery("SELECT x FROM Pago x LEFT JOIN FETCH x.idFactura f LEFT JOIN FETCH f.idCaja LEFT JOIN FETCH f.idEmpleadoRol "
                + "WHERE x.idFactura.idFactura = :idFactura ORDER BY x.idPago", Pago.class)
                .setParameter("idFactura", idFactura)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }
 
    public long contarPorFactura(UUID idFactura) {
        if (idFactura == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return em.createQuery("SELECT COUNT(x) FROM Pago x "
                + "WHERE x.idFactura.idFactura = :idFactura", Long.class)
                .setParameter("idFactura", idFactura)
                .getSingleResult();
    }
}
 

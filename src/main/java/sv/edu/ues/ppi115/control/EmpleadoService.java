package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.ppi115.entity.Empleado;

@Stateless
@LocalBean
public class EmpleadoService extends AbstractDataAccess<Empleado> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Empleado> getEntityClass() {
        return Empleado.class;
    }

    public EmpleadoService() {
    }
}

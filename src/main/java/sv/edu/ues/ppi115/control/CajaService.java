package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.ppi115.entity.Caja;

@Stateless
@LocalBean
public class CajaService extends AbstractDataAccess<Caja> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Caja> getEntityClass() {
        return Caja.class;
    }

    public CajaService() {
    }
}

package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.ppi115.entity.TipoCaracteristica;

@Stateless
@LocalBean
public class TipoCaracteristicaService extends AbstractDataAccess<TipoCaracteristica> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<TipoCaracteristica> getEntityClass() {
        return TipoCaracteristica.class;
    }

    public TipoCaracteristicaService() {
    }
}

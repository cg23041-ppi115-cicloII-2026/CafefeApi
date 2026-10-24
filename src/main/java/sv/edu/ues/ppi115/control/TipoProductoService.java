package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.ppi115.entity.TipoProducto;

@Stateless
@LocalBean
public class TipoProductoService extends AbstractDataAccess<TipoProducto> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<TipoProducto> getEntityClass() {
        return TipoProducto.class;
    }

    public TipoProductoService() {
    }
}

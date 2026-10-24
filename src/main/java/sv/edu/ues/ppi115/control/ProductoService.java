package sv.edu.ues.ppi115.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.ppi115.entity.Producto;

@Stateless
@LocalBean
public class ProductoService extends AbstractDataAccess<Producto> {

    @PersistenceContext(unitName = "CafefePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Producto> getEntityClass() {
        return Producto.class;
    }

    public ProductoService() {
    }
}

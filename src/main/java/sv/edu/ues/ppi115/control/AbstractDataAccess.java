package sv.edu.ues.ppi115.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Clase base para el acceso a datos de cualquier entidad.
 * Cada service concreto solo indica su EntityManager y su clase de entidad.
 *
 * @param <T> tipo de la entidad
 */
public abstract class AbstractDataAccess<T> implements Serializable {

    protected abstract EntityManager getEntityManager();

    protected abstract Class<T> getEntityClass();

    public void create(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        getEntityManager().persist(entity);
    }

    public T findById(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return getEntityManager().find(getEntityClass(), id);
    }

    public List<T> findRange(int first, int max) {
        if (first < 0 || max <= 0) {
            throw new IllegalArgumentException("Parametros de rango invalidos");
        }
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.select(root);
        TypedQuery<T> query = getEntityManager().createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        return getEntityManager().merge(entity);
    }

    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        getEntityManager().remove(getEntityManager().contains(entity)
                ? entity : getEntityManager().merge(entity));
    }

    public long contar() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(getEntityClass());
        cq.select(cb.count(root));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
}
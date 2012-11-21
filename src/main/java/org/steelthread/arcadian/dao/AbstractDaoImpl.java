package org.steelthread.arcadian.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.steelthread.arcadian.server.ServerType;

public abstract class AbstractDaoImpl <T, K> {

  private Class<T> persistentClass;

  public abstract EntityManager getEntityManager();

  @SuppressWarnings("unchecked")
  public AbstractDaoImpl() {
    persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }
  
  @SuppressWarnings("unchecked")
  public List<T> findByServerType(ServerType serverType) {
    Query query = getEntityManager().createQuery("from " + persistentClass.getSimpleName() + " where serverType = :serverType");
    query.setParameter("serverType", serverType);
    return query.getResultList();
  }  

  @SuppressWarnings("unchecked")
  public List<T> getAll() {
    return getEntityManager().createQuery("from " + persistentClass.getSimpleName()).getResultList();
  }

  public List<T> queryByExample(T searchEntity) {
    return queryByExample(searchEntity, null);
  }

  public T queryByExampleFindOne(T searchEntity) {
    return queryByExample(searchEntity, 1).get(0);
  }
  
  public List<T> queryByExample(T searchEntity, Integer expectedSize) {
    Example example = Example.create(searchEntity);
    Criteria criteria = getCriteria();
    List<T> results = criteria.add(example).list();
    if(expectedSize != null && (results.size() != expectedSize)) {
      throw new IncorrectResultSizeDataAccessException("IncorrectResultSizeDataAccessException for " + persistentClass.getSimpleName() + " found for search entity:" + searchEntity, expectedSize , results.size());
    }
    return results;
  }
  
  public void refresh(T domainObject) {
    getEntityManager().refresh(domainObject);
  }

  public T get(K id) {
    return getEntityManager().find(this.persistentClass, id);
  }


  public void create(T domainObject) {
    getEntityManager().persist(domainObject);
  }

  public void update(T domainObject) {
    getEntityManager().merge(domainObject);
  }

  public void flush() {
    getEntityManager().flush();
  }

  public void clear() {
    getEntityManager().clear();
  }

  public void remove(T domainObject) {
    getEntityManager().remove(domainObject);
  }

  public Criteria getCriteria() {
    Session session = (Session) getEntityManager().getDelegate();
    Criteria result = session.createCriteria(persistentClass);
    result.setFlushMode(FlushMode.COMMIT);
    return(result);
  }
}
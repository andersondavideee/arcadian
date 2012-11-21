package org.steelthread.arcadian.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class GenericDao <T, K> extends AbstractDaoImpl<T, K> {

  @Inject
  private EntityManager entityManager;
  @Inject
  private JdbcTemplate jdbcTemplate;
  
  public Query createNamedQuery(String name) {
    Query result = entityManager.createNamedQuery(name);
    result.setFlushMode(FlushModeType.COMMIT);
    return result;
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
}
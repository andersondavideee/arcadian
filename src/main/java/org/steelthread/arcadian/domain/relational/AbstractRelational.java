package org.steelthread.arcadian.domain.relational;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class AbstractRelational {

  private Long id;
  private Date createDate;
  private Date lastUpdateDate;

  @Id
  @GeneratedValue(generator = "idSequence", strategy = GenerationType.AUTO)
  @Column(name = "id")
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  @Column(name = "create_date")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "last_update_date")
  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }
  
  @PrePersist
  protected void handleCreateDate() {
    setCreateDate(new Date());
    setLastUpdateDate(getCreateDate());
  }

  @PreUpdate
  @SuppressWarnings("unused")
  private void handleUpdateDate() {
    setLastUpdateDate(new Date());
  }
}
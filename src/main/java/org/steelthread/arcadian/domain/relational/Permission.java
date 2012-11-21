package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.steelthread.arcadian.domain.logical.PermissionName;

@Entity
@Table(name="permission")
@SequenceGenerator(name = "idSequence", sequenceName = "permission_id_seq")
public class Permission extends AbstractRelational {

  private PermissionName name;
  private Set<Role> roles = new HashSet<Role>(0);
  
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
  @JsonIgnore
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
  
  @Column(name = "name")
  @Enumerated(EnumType.STRING)  
  public PermissionName getName() {
    return name;
  }

  public void setName(PermissionName name) {
    this.name = name;
  } 
}
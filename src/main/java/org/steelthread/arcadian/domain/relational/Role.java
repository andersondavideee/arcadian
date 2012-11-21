package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.steelthread.arcadian.domain.logical.RoleName;

@Entity
@Table(name="role")
@SequenceGenerator(name = "idSequence", sequenceName = "role_id_seq")
public class Role extends AbstractRelational {

  private RoleName name;
  private Set<User> users = new HashSet<User>(0);
  private Set<Permission> permissions = new HashSet<Permission>(0);

  @Column(name = "name")
  @Enumerated(EnumType.STRING)  
  public RoleName getName() {
    return name;
  }

  public void setName(RoleName name) {
    this.name = name;
  }
  
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
  @JsonIgnore
  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
  
  public void addPermission(Permission permission) {
    getPermissions().add(permission);
  }

  public void removePermission(Long id) {
    Permission foundPermission = null;
    for (Permission permission: getPermissions()) {
      if(id == permission.getId()) {
        foundPermission = permission;
      }
    }
    getPermissions().remove(foundPermission);
  }  

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }
}
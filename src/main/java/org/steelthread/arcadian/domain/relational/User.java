package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="users")
@SequenceGenerator(name = "idSequence", sequenceName = "users_id_seq")
public class User extends AbstractRelational {

  private String username;
  private String password;
  private String email;  
  private String salt;
  private Boolean enabled;
  private String authenticationUUID;
  
  private Set<Server> servers = new HashSet<Server>(0);
  private Set<Role> roles = new HashSet<Role>(0);
 
  public void addServer(Server server) {
    getServers().add(server);
  }

  public void removeServer(Long id) {
    Server foundServer = null;
    for (Server server : getServers()) {
      if(id == server.getId()) {
        foundServer = server;
      }
    }
    getServers().remove(foundServer);
  }

  public void addRole(Role role) {
    getRoles().add(role);
  }

  public void removeRole(Long id) {
    Role foundRole = null;
    for (Role role : getRoles()) {
      if(id == role.getId()) {
        foundRole = role;
      }
    }
    getRoles().remove(foundRole);
  }

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "users_role", joinColumns = { @JoinColumn(name = "users_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
  
  @Column(name = "enabled")
  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
  
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "users_server", joinColumns = { @JoinColumn(name = "users_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "server_id", nullable = false, updatable = false) })
  public Set<Server> getServers() {
    return servers;
  }
  public void setServers(Set<Server> servers) {
    this.servers = servers;
  }
  @Column(name = "username")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  @Column(name = "password")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  @Column(name = "salt")
  public String getSalt() {
    return salt;
  }
  public void setSalt(String salt) {
    this.salt = salt;
  }
  @Column(name = "email")
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  @Column(name = "authentication_uuid")
  public String getAuthenticationUUID() {
    return authenticationUUID;
  }

  public void setAuthenticationUUID(String authenticationUUID) {
    this.authenticationUUID = authenticationUUID;
  }  
}
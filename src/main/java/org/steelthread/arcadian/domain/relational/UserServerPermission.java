package org.steelthread.arcadian.domain.relational;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
  @NamedQuery(
  name = "findServerPermissionForUser",
  query = "from UserServerPermission usp where usp.user = :user and usp.server = :server and usp.permission = :permission"
  ),
  @NamedQuery(
  name = "findAllServerPermissionsForUser",
  query = "from UserServerPermission usp where usp.user = :user and usp.server = :server"
  )  
})
@Entity
@Table(name="users_server_permission")
@SequenceGenerator(name = "idSequence", sequenceName = "users_server_permission_id_seq")
public class UserServerPermission {

  private Long id;
  private User user;
  private Server server;
  private Permission permission;
  
  public UserServerPermission(User user, Server server, Permission permission) {
    this.user = user;
    this.server = server;
    this.permission = permission;
  }

  public UserServerPermission() {
  }
  
  @Id
  @GeneratedValue(generator = "idSequence", strategy = GenerationType.AUTO)
  @Column(name = "id")
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @OneToOne
  @JoinColumn(name = "users_id")
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  @OneToOne
  @JoinColumn(name = "server_id")
  public Server getServer() {
    return server;
  }
  public void setServer(Server server) {
    this.server = server;
  }

  @OneToOne
  @JoinColumn(name = "permission_id")
  public Permission getPermission() {
    return permission;
  }
  public void setPermission(Permission permission) {
    this.permission = permission;
  }
}
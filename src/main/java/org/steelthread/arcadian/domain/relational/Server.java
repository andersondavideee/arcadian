package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.steelthread.arcadian.server.ServerType;

@Entity
@Table(name="server")
@SequenceGenerator(name = "idSequence", sequenceName = "server_id_seq")
public class Server extends AbstractRelational {

  private String host;
  private Integer port;
  private String name;
  private String password;
  private ServerType serverType;
  private Set<User> users = new HashSet<User>(0);
  private User owner;
  
  @OneToOne
  @JoinColumn(name="owner_user_id")
  @JsonIgnore
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "servers")
  @JsonIgnore
  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
  
  @Column(name = "server_type")
  @Enumerated(EnumType.STRING)
  public ServerType getServerType() {
    return serverType;
  }

  public void setServerType(ServerType serverType) {
    this.serverType = serverType;
  }

  @Column(name = "password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "host")
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @Column(name = "port")
  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  } 
}
package org.steelthread.arcadian.domain.logical;

import org.steelthread.arcadian.server.ServerType;

public class Server {

  private Long id;
  private String host;
  private Integer port;
  private String name;
  private String password;  
  private ServerType serverType;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
 
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ServerType getServerType() {
    return serverType;
  }

  public void setServerType(ServerType serverType) {
    this.serverType = serverType;
  }

  private boolean active;

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
  
  private boolean currentOwner = false;

  public boolean getCurrentOwner() {
    return currentOwner;
  }

  public void setCurrentOwner(boolean currentOwner) {
    this.currentOwner = currentOwner;
  }
}
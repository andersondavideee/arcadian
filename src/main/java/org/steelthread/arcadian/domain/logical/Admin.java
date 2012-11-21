package org.steelthread.arcadian.domain.logical;

import java.util.List;

public class Admin extends AbstractLogicalObject {

  public Admin(Long serverId) {
    super(serverId);
  }
  
  public Admin() {
  }  
 
  private List<String> permissions;
  private String username;
  private boolean connected;
  
  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
  
}
package org.steelthread.arcadian.domain.logical;

public class Permission extends AbstractLogicalObject {

  public Permission(Long serverId) {
    super(serverId);
  }
  
  public Permission() {
  }  

  private String description;
  private String name;
  
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
}

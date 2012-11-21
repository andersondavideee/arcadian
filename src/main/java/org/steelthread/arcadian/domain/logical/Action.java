package org.steelthread.arcadian.domain.logical;

import java.util.List;

public class Action {

  private Long serverId;
  private String action;
  private List<String> updateValues;

  public Long getServerId() {
    return serverId;
  }
  public void setServerId(Long serverId) {
    this.serverId = serverId;
  }
  public String getAction() {
    return action;
  }
  public void setAction(String action) {
    this.action = action;
  }
  public List<String> getUpdateValues() {
    return updateValues;
  }
  public void setUpdateValues(List<String> updateValues) {
    this.updateValues = updateValues;
  }
}
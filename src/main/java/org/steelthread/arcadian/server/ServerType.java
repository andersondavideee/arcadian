package org.steelthread.arcadian.server;

import org.steelthread.arcadian.domain.relational.Command;


public enum ServerType {

  BATTLEFIELD3("Battlefield 3");
  
  private ServerType(String description) {
    this.description = description;
  }
  
  private Command command;  

  public Command getCommand() {
    return command;
  }

  public void setCommand(Command command) {
    this.command = command;
  }

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

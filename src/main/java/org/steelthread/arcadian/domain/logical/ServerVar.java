package org.steelthread.arcadian.domain.logical;


import org.steelthread.arcadian.command.Datatype;

public class ServerVar extends AbstractLogicalObject {

  public ServerVar(Long serverId) {
    super(serverId);
  }
  
  public ServerVar() {
  }

  private String command;
  private String description;
  private Datatype datatype;
  private String value;
  
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getCommand() {
    return command;
  }
  public void setCommand(String command) {
    this.command = command;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Datatype getDatatype() {
    return datatype;
  }
  public void setDatatype(Datatype datatype) {
    this.datatype = datatype;
  }
}
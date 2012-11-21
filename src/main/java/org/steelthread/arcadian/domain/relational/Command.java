package org.steelthread.arcadian.domain.relational;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.steelthread.arcadian.command.CommandConstant;
import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.command.Datatype;
import org.steelthread.arcadian.server.ServerType;

@Entity
@Table(name="command")
@SequenceGenerator(name = "idSequence", sequenceName = "command_id_seq")
public class Command extends AbstractRelational {

  private String command;
  private String mapperName;
  private String description;
  private Datatype datatype;
  private CommandType commandType;
  private ServerType serverType;
  private CommandConstant commandConstant;

  @Column(name = "command_constant")
  @Enumerated(EnumType.STRING)  
  public CommandConstant getCommandConstant() {
    return commandConstant;
  }
  public void setCommandConstant(CommandConstant commandConstant) {
    this.commandConstant = commandConstant;
  }
  @Column(name = "command")
  public String getCommand() {
    return command;
  }
  public void setCommand(String command) {
    this.command = command;
  }
  @Column(name = "mapper_name")
  public String getMapperName() {
    return mapperName;
  }
  public void setMapperName(String mapperName) {
    this.mapperName = mapperName;
  }
  @Column(name = "description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  @Column(name = "data_type")
  @Enumerated(EnumType.STRING)  
  public Datatype getDatatype() {
    return datatype;
  }
  public void setDatatype(Datatype datatype) {
    this.datatype = datatype;
  }
  @Column(name = "command_type")
  @Enumerated(EnumType.STRING)   
  public CommandType getCommandType() {
    return commandType;
  }
  public void setCommandType(CommandType commandType) {
    this.commandType = commandType;
  }
  @Column(name = "server_type")
  @Enumerated(EnumType.STRING)    
  public ServerType getServerType() {
    return serverType;
  }
  public void setServerType(ServerType serverType) {
    this.serverType = serverType;
  }
}
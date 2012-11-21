package org.steelthread.arcadian.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.command.CommandConstant;
import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.server.ServerType;

@Repository
public class CommandDao extends GenericDao<Command, Long> {

  public List<Command> findCommandsByCommandType(CommandType commandType, ServerType serverType) {
    Command command = new Command();
    command.setCommandType(commandType);
    command.setServerType(serverType);
    return queryByExample(command);
  }
  
  public Command findCommandByCommandConstant(CommandConstant commandConstant, ServerType serverType) {
    Command command = new Command();
    command.setCommandConstant(commandConstant);
    command.setServerType(serverType);
    return queryByExampleFindOne(command);
  }
  
  public Command findCommandByCommand(String command, ServerType serverType) {
    Command search = new Command();
    search.setCommand(command);
    search.setServerType(serverType);
    return queryByExampleFindOne(search);
  }
}

package org.steelthread.arcadian.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.steelthread.arcadian.command.CommandConstant;
import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.dao.CommandDao;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.network.Connection;
import org.steelthread.arcadian.network.ConnectionObjectManager;

@Component
public class CommandRunnerService {

  @Inject
  protected CommandService commandService;
  @Inject
  protected ConnectionService connectionService;
  @Inject
  protected CommandDao commandDao;
  @Inject
  protected ConnectionObjectManager connectionObjectManager;
  
  public <T> List<T> runListCommand(CommandConstant commandConstant, Server server) {
    Connection connection = getConnection();
    return commandService.runListCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection, server.getId());
  }

  public <T> List<T> runListCommand(CommandConstant commandConstant, Server server, String arg) {
    Connection connection = getConnection();
    return commandService.runListCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection, server.getId(), arg);
  }
  
  public <T> List<T> runListCommandType(CommandType commandType, Server server) {
    Connection connection = getConnection();
    return commandService.runListCommandType(commandType, connection, server.getServerType(), server.getId());
  }
  
  public <T> T runCommand(CommandConstant commandConstant, Server server) {
    Connection connection = getConnection();
    return commandService.runCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection, server.getId());
  }
  
  
  public String runUpdateCommand(String command, Server server, String updateValue) {
    Connection connection = getConnection();
    return commandService.runUpdateCommand(commandDao.findCommandByCommand(command, server.getServerType()), connection, updateValue, server);
  }

  public String runUpdateCommand(String command, Server server, List<String> updateValue) {
    Connection connection = getConnection();
    return commandService.runUpdateCommand(commandDao.findCommandByCommand(command, server.getServerType()), connection, updateValue, server);
  }
  
  public String runUpdateCommand(CommandConstant commandConstant, Server server, String updateValue) {
    Connection connection = getConnection();
    return commandService.runUpdateCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection, updateValue, server);
  }

  public String runUpdateCommand(CommandConstant commandConstant, Server server, List<String> updateValues) {
    Connection connection = getConnection();
    return commandService.runUpdateCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection, updateValues, server);
  }
  
  public String runUpdateCommand(CommandConstant commandConstant, Server server) {
    Connection connection = getConnection();
    return commandService.runUpdateCommand(commandDao.findCommandByCommandConstant(commandConstant, server.getServerType()), connection);
  }

  public List<String> runServerCommand(String command, String serverCommand, List<String> updateValues, Server server) {
    Connection connection = getConnection();
    return commandService.runServerCommand(command, serverCommand, connection, updateValues, server);
  }
  
  protected Connection getConnection() {
    return connectionObjectManager.getCurrentConnection();
  }
}
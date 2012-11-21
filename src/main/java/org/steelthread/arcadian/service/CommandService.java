package org.steelthread.arcadian.service;

import java.util.List;

import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.network.Connection;
import org.steelthread.arcadian.server.ServerType;

public interface CommandService {

  public <T> List<T> runListCommand(Command command, Connection connection, Long serverId);

  public <T> List<T> runListCommand(Command command, Connection connection, Long serverId, String arg);

  public <T> T runCommand(Command command, Connection connection, Long serverId);

  public String runUpdateCommand(Command command, Connection connection, String updateValue, Server server);

  public String runUpdateCommand(Command command, Connection connection, List<String> updateValues, Server server);

  public List<String> runServerCommand(String command, String serverCommand, Connection connection, List<String> updateValues, Server server);

  public List<String> runServerCommand(Command command, Connection connection, List<String> updateValues);

  public String runUpdateCommand(Command command, Connection connection);

  public <T> List<T> runListCommandType(CommandType commandType, Connection connection, ServerType serverType,
      Long serverId);

}
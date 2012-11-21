package org.steelthread.arcadian.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.dao.CommandDao;
import org.steelthread.arcadian.domain.mapper.ListMapper;
import org.steelthread.arcadian.domain.mapper.Mapper;
import org.steelthread.arcadian.domain.mapper.ServerResponseMapper;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.network.Connection;
import org.steelthread.arcadian.server.ServerType;
import org.steelthread.arcadian.util.ArcadianConstants;

@Service
public class CommandServiceImpl implements BeanFactoryAware, CommandService {

  private BeanFactory beanFactory;
  
  @Inject
  protected CommandDao commandDao;
  @Inject
  protected ServerResponseMapper serverResponseMapper;

  @Override
  public <T> List<T> runListCommand(Command command, Connection connection, Long serverId) {
    List<String> response = sendAndReceiveCommand(command, connection);
    // now marshall it into the correct datatype
    ListMapper<T> mapper = (ListMapper<T>) beanFactory.getBean(command.getMapperName());
    return mapper.map(response, command, serverId);
  }

  @Override
  public <T> List<T> runListCommand(Command command, Connection connection, Long serverId, String arg) {
    List<String> response = runServerCommand(command, connection, Arrays.asList(new String[] { arg }));
    // now marshall it into the correct datatype
    ListMapper<T> mapper = (ListMapper<T>) beanFactory.getBean(command.getMapperName());
    return mapper.map(response, command, serverId);
  }

  @Override
  public <T> T runCommand(Command command, Connection connection, Long serverId) {
    List<String> response = sendAndReceiveCommand(command, connection);
    // now marshall it into the correct datatype
    Mapper<T> mapper = (Mapper<T>) beanFactory.getBean(command.getMapperName());
    return mapper.map(response, command, serverId);
  }

  @Override
  @PreAuthorize(ArcadianConstants.HAS_PERMISSION_SERVER_COMMAND_ADMIN_COMMAND_CONSTANT)  
  public String runUpdateCommand(Command command, Connection connection, String updateValue, Server server) {
    return serverResponseMapper.map(sendAndReceiveUpdateCommand(command, connection, updateValue));
  }

  @Override
  @PreAuthorize(ArcadianConstants.HAS_PERMISSION_SERVER_COMMAND_ADMIN_COMMAND_CONSTANT)  
  public String runUpdateCommand(Command command, Connection connection, List<String> updateValues, Server server) {
    return serverResponseMapper.map(sendAndReceiveUpdateCommand(command, connection, updateValues));
  }

  @Override
  @PreAuthorize(ArcadianConstants.HAS_PERMISSION_SERVER_COMMAND_ADMIN_COMMAND_STRING)  
  public List<String> runServerCommand(String command, String serverCommand, Connection connection, List<String> updateValues, Server server) {
    return connection.sendServerCommand(serverCommand, updateValues);
  }

  @Override
  public List<String> runServerCommand(Command command, Connection connection, List<String> updateValues) {
    return connection.sendServerCommand(command, updateValues);
  }

  @Override
  public String runUpdateCommand(Command command, Connection connection) {
    return serverResponseMapper.map(sendAndReceiveUpdateCommand(command, connection));
  }
  
  @Override
  public <T> List<T> runListCommandType(CommandType commandType, Connection connection, ServerType serverType, Long serverId) {
    List<T> mappedCommands = new ArrayList<T>();
    // find all the Commands with a certain CommandType and return the values
    for (Command command : commandDao.findCommandsByCommandType(commandType, serverType)) {
      List<String> response = sendAndReceiveCommand(command, connection);      
      // now marshall it into the correct datatype
      Mapper<T> mapper = (Mapper<T>) beanFactory.getBean(command.getMapperName());
      mappedCommands.add(mapper.map(response, command, serverId));
    }
    return mappedCommands;
  }
  
  protected List<String> sendAndReceiveCommand(Command command, Connection connection) {
    // send the command to the server
    return connection.send(command);
  }

  protected List<String> sendAndReceiveUpdateCommand(Command command, Connection connection) {
    // send the command to the server
    return connection.sendUpdate(command);
  }
  
  protected List<String> sendAndReceiveUpdateCommand(Command command, Connection connection, String updateValue) {
    // send the command to the server
    return connection.sendUpdate(command, updateValue);
  }

  protected List<String> sendAndReceiveUpdateCommand(Command command, Connection connection, List<String> updateValues) {
    // send the command to the server
    return connection.sendUpdate(command, updateValues);
  }
  
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
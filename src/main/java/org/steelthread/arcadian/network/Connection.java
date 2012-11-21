package org.steelthread.arcadian.network;

import java.util.List;

import org.steelthread.arcadian.domain.relational.Command;


public interface Connection {

  public List<String> send(Command command);
  public List<String> sendUpdate(Command command, List<String> updateValue);
  public List<String> sendUpdate(Command command, String updateValue);
  public List<String> sendUpdate(Command command);
  public Boolean connect(String host, Integer port);
  public Boolean authenticate(String user, String password);
  public void close();
  public Boolean isConnectionValid();
  public List<String> sendServerCommand(String command, List<String> updateValues);
  public List<String> sendServerCommand(Command command, List<String> updateValues);
}

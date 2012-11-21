package org.steelthread.arcadian.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.event.EventTracker;

public class DiceConnectionImpl implements Connection {

  private static final Logger logger = LoggerFactory.getLogger(DiceConnectionImpl.class);
  private static final String SPACE = " ";

  private DiceConnectionHelper diceConnectionHelper = null;
  private EventTracker eventTracker = null;
  
  public DiceConnectionImpl(EventTracker eventTracker) {
    diceConnectionHelper = new DiceConnectionHelper();
    this.eventTracker = eventTracker;
  }
  
  protected List<String> sendCommand(List<String> commands) {
    List<String> response = diceConnectionHelper.sendCommand(commands);
    // track the event
    Event event = new Event(Integer.parseInt(response.get(0)), listToString(response), EventType.CONSOLE, listToString(commands), new Date());
    if(eventTracker != null) {
      eventTracker.addEvent(event);      
    }
    return response;
  }

  protected String listToString(List<String> list) {
    StringBuilder ret = new StringBuilder();
    for (String word : list) {
      ret.append(word);
      ret.append(SPACE);
    }
    return ret.toString();
  }
  
  @Override
  public List<String> send(Command command) {
    return sendCommand(Arrays.asList(new String[] { command.getCommand() }));

  }

  @Override
  public List<String> sendUpdate(Command command, String updateValue) {
    return sendCommand(Arrays.asList(new String[] { command.getCommand(), updateValue }));
  }
  
  @Override
  public List<String> sendUpdate(Command command, List<String> updateValues) {
    return sendServerCommand(command.getCommand(), updateValues);
  }

  @Override
  public List<String> sendUpdate(Command command) {
    return sendCommand(Arrays.asList(new String[] { command.getCommand() }));
  }

  @Override
  public List<String> sendServerCommand(Command command, List<String> updateValues) {
    return sendServerCommand(command.getCommand(), updateValues);
  }

  @Override
  public List<String> sendServerCommand(String command, List<String> updateValues) {
    List<String> args = new ArrayList<String>();
    args.add(command);
    if(updateValues != null) {
      args.addAll(updateValues);      
    }
    return sendCommand(args);
  }
  
  @Override
  public Boolean connect(String host, Integer port) {
    return diceConnectionHelper.connect(host, port);
  }

  @Override
  public void close() {
    diceConnectionHelper.close();
  }

  @Override
  public Boolean isConnectionValid() {
    boolean isConnectionValid = true;
    // use a simple command to verify if we still have a connection with the server
    try {
      sendCommand(Arrays.asList(new String[] { "version" }));
    } catch (ConnectionException e) {
      isConnectionValid = false;
    }
    return isConnectionValid;
  }
  
  @Override
  public Boolean authenticate(String user, String password) {
    return diceConnectionHelper.authenticate(user, password);
  }

  public DiceConnectionHelper getDiceConnectionHelper() {
    return diceConnectionHelper;
  }
  
}
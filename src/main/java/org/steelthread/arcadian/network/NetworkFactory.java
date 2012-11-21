package org.steelthread.arcadian.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.event.EventExecutor;
import org.steelthread.arcadian.event.EventTracker;
import org.steelthread.arcadian.server.ServerType;

@Component
public class NetworkFactory {
  
  @Autowired
  protected EventExecutor eventExecutor;

  public Connection createConnection(ServerType serverType, EventTracker eventTracker) {
    
    boolean proxyMode = Boolean.parseBoolean(System.getProperty("proxyMode", "false"));
    
    switch (serverType) {
    case BATTLEFIELD3:
      return proxyMode ? new DiceConnectionProxy() : new DiceConnectionImpl(eventTracker);
    default:
      throw new RuntimeException("ServerType not supported:" + serverType);
    }    
  }
  
  public EventConnection createEventConnection(ServerType serverType) {
    
    boolean proxyMode = Boolean.parseBoolean(System.getProperty("proxyMode", "false"));

    switch (serverType) {
    case BATTLEFIELD3:
      return proxyMode ? new DiceEventConnectionProxy() : new DiceEventConnectionImpl(eventExecutor);
    default:
      throw new RuntimeException("ServerType not supported:" + serverType);
    }    
  }
  
}
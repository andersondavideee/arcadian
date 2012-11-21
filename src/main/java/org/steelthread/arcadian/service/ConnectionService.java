package org.steelthread.arcadian.service;

import javax.inject.Inject;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.event.EventTracker;
import org.steelthread.arcadian.network.Connection;
import org.steelthread.arcadian.network.ConnectionObjectManager;
import org.steelthread.arcadian.network.EventConnection;
import org.steelthread.arcadian.network.NetworkFactory;

@Component
public class ConnectionService {

  @Inject
  protected NetworkFactory networkFactory;
  @Inject
  protected ConnectionObjectManager connectionObjectManager;
  
  protected Connection getConnection(Server server, EventTracker eventTracker) {
    Connection connection = networkFactory.createConnection(server.getServerType(), eventTracker);
    connection.connect(server.getHost(), server.getPort());
    connection.authenticate("", server.getPassword());
    return connection;
  }

  protected EventConnection getEventConnection(Server server) {
    EventConnection eventConnection = networkFactory.createEventConnection(server.getServerType());
    eventConnection.connect(server.getHost(), server.getPort());
    eventConnection.authenticate("", server.getPassword());
    // start receiving events from the server
    eventConnection.startReceivingEvents();
    return eventConnection;
  }
  
  public void seedConnection(Server server, MultiKey key) {
    EventConnection eventConnection = getEventConnection(server);
    // TODO leverage the event tracker from the event connection - this is too coupled - should have a factory
    // that builds the event tracker and is then set on both of these connections
    Connection connection = getConnection(server, eventConnection.getEventTracker());
    connectionObjectManager.addConnection(connection, eventConnection, key, server);
  }
  
  public void removeCurrentConnection() {
    connectionObjectManager.removeCurrentConnection();
  }
  
  public EventConnection getCurrentEventConnection() {
    return connectionObjectManager.getCurrentEventConnection();
  }
}
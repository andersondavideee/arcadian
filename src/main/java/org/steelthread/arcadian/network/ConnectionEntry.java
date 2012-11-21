package org.steelthread.arcadian.network;

import java.util.Date;

import org.steelthread.arcadian.domain.relational.Server;


public class ConnectionEntry {
  
  public ConnectionEntry(Connection connection, EventConnection eventConnection,  Date dateLastAccessed, Server server) {
    this.connection = connection;
    this.dateLastAccessed = dateLastAccessed;
    this.server = server;
    this.eventConnection = eventConnection;
  }

  private Connection connection;
  private Date dateLastAccessed;
  private EventConnection eventConnection;
  private Server server;
  
  public EventConnection getEventConnection() {
    return eventConnection;
  }
  public void setEventConnection(EventConnection eventConnection) {
    this.eventConnection = eventConnection;
  }
  public Connection getConnection() {
    return connection;
  }
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  public Date getDateLastAccessed() {
    return dateLastAccessed;
  }
  public void setDateLastAccessed(Date dateLastAccessed) {
    this.dateLastAccessed = dateLastAccessed;
  }
  public Server getServer() {
    return server;
  }
  public void setServer(Server server) {
    this.server = server;
  }
}
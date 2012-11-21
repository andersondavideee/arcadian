package org.steelthread.arcadian.network;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.util.ArcadianUtil;

@Component
public class ConnectionObjectManager {

  @Inject
  protected ArcadianUtil arcadianUtil;
  
  private Map<MultiKey, ConnectionEntry> connections = new ConcurrentHashMap<MultiKey, ConnectionEntry>();
  
  private static final Logger log = LoggerFactory.getLogger(ConnectionObjectManager.class);
 
  public boolean isConnected(String username) {
    for (Entry<MultiKey, ConnectionEntry> i : getConnections().entrySet()) {
      // The MultiKey is SessionID + Username. Check to see if the 
      // the user is connected (part of the key)
      if(StringUtils.equals(username, (String) i.getKey().getKey(1))) {
        return true;
      }
    }
    return false;
  }
  
  public Connection getCurrentConnection() {
    return getConnection(arcadianUtil.getCurrentConnectionKey());
  }

  public EventConnection getCurrentEventConnection() {
    return getEventConnection(arcadianUtil.getCurrentConnectionKey());
  }
  
  public ConnectionEntry getCurrentConnectionEntry() {
    return getConnections().get(arcadianUtil.getCurrentConnectionKey());
  }
  
  public Connection getConnection(MultiKey key) {
    return getConnectionEntry(key).getConnection();
  } 

  public EventConnection getEventConnection(MultiKey key) {
    return getConnectionEntry(key).getEventConnection();
  } 

  protected ConnectionEntry getConnectionEntry(MultiKey key) {
    ConnectionEntry connectionEntry = connections.get(key);
    if(connectionEntry == null) {
      throw new RuntimeException("Connection with key " + key + " is NULL");
    }
    // Update last accessed time
    connectionEntry.setDateLastAccessed(new Date());
    return connectionEntry;
  } 

  public void closeStaleConnections() {
    for (Entry<MultiKey, ConnectionEntry> i : getConnections().entrySet()) {
      MultiKey key = i.getKey();
      ConnectionEntry connectionEntry = i.getValue();
      if (isStale(connectionEntry)) {
        log.debug("Connection with key " + key + " is stale.");
        // remove the entry and close the connection
        removeAndCloseConnectionEntry(connectionEntry, key);
      }
    }
  }

  public void removeCurrentConnection() {
    MultiKey currentConnectionKey = arcadianUtil.getCurrentConnectionKey();
    ConnectionEntry connectionEntry = getConnections().get(currentConnectionKey);
    if(connectionEntry != null) {
      // remove the entry and close the connection
      removeAndCloseConnectionEntry(connectionEntry, currentConnectionKey);
    }
  }
  
  public void addConnection(Connection connection, EventConnection eventConnection, MultiKey key, Server server) {
    log.debug("adding connection with key " + key);
    if(getConnections().containsKey(key)) {
      // first remove/close existing connection
      removeAndCloseConnectionEntry(getConnections().get(key), key);
    }
    getConnections().put(key, new ConnectionEntry(connection, eventConnection, new Date(), server));
  }
  
  protected boolean isStale(ConnectionEntry connectionEntry) {
    Calendar lastAccessedTime = Calendar.getInstance();
    lastAccessedTime.setTime(connectionEntry.getDateLastAccessed());
    lastAccessedTime.add(Calendar.MINUTE, 20);
    return lastAccessedTime.before(Calendar.getInstance());
  }
  
  protected void removeAndCloseConnectionEntry (ConnectionEntry connectionEntry, MultiKey key) {
    try {
      getConnections().get(key).getConnection().close();
      getConnections().get(key).getEventConnection().close();
    } catch (Throwable e) {
      log.error("error closing connection for key:" + key, e);
    }      
    getConnections().remove(key);    
  }
  
  public Map<MultiKey, ConnectionEntry> getConnections() {
    return connections;
  }
}
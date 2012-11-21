package org.steelthread.arcadian.network;

import java.util.TimerTask;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectionResourceTimerTask extends TimerTask {

  private static final Logger log = LoggerFactory.getLogger(ConnectionResourceTimerTask.class);

  @Inject
  private ConnectionObjectManager connectionObjectManager;
  
  @Override
  public void run() {
    log.debug("Validating Connections");
    try {
      connectionObjectManager.closeStaleConnections();
    } catch (Throwable e) {
      log.error("Error closing stale connections", e);
    }
    log.debug("Connections validated");
  }
}
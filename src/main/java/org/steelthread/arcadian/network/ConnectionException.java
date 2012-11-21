package org.steelthread.arcadian.network;

public class ConnectionException extends RuntimeException {

  private static final long serialVersionUID = 572141584694925696L;

  public ConnectionException(String message) {
    super(message);
  }
  
  public ConnectionException(Throwable e) {
    super(e);
  }
  
}

package org.steelthread.arcadian.network;

public class ServerUpdateException extends RuntimeException {

  private static final long serialVersionUID = -562107600171406037L;

  public ServerUpdateException(String message) {
    super(message);
  }
  
  public ServerUpdateException(Throwable e) {
    super(e);
  }  
}

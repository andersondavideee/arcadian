package org.steelthread.arcadian.user;

public class BadPasswordException extends RuntimeException {

  private static final long serialVersionUID = -2079448210094258192L;

  public BadPasswordException(String message) {
    super(message);
  }

  public BadPasswordException(Throwable e) {
    super(e);
  }
}

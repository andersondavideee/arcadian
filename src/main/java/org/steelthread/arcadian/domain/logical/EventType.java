package org.steelthread.arcadian.domain.logical;


public enum EventType {
  SERVER, PLAYER, MISC, PUNKBUSTER, ADMIN, CONSOLE, LOGIN;
  
  public static EventType[] getDefaultFilterEvents() {
    return new EventType[] {EventType.CONSOLE};
  }
}

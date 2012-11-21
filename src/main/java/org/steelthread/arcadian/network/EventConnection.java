package org.steelthread.arcadian.network;

import java.util.List;

import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.event.EventTracker;

public interface EventConnection {

  public Boolean connect(String host, Integer port);
  public Boolean authenticate(String user, String password);
  public void close();
  public List<Event> getEvents();
  public List<Event> getEventsByType(EventType[] eventTypes);  
  public List<Event> getEventsUsingFilter();
  public void startReceivingEvents();
  public void setEventFilter(EventType[] eventTypes);
  public EventTracker getEventTracker();
}

package org.steelthread.arcadian.network;

import java.util.List;

import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.event.EventTracker;

public class DiceEventConnectionProxy implements EventConnection {

  @Override
  public Boolean connect(String host, Integer port) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Boolean authenticate(String user, String password) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Event> getEvents() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Event> getEventsByType(EventType[] eventTypes) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void startReceivingEvents() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Event> getEventsUsingFilter() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEventFilter(EventType[] eventTypes) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public EventTracker getEventTracker() {
    // TODO Auto-generated method stub
    return null;
  }  
}

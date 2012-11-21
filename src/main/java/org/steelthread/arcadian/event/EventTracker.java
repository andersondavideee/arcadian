package org.steelthread.arcadian.event;

import java.util.List;

import org.steelthread.arcadian.domain.relational.Event;

public interface EventTracker {

  public void addEvent(Event event);
  public List<Event> getEvents();
  public void stop();
}

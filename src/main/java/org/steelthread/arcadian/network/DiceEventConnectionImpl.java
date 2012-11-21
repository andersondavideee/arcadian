package org.steelthread.arcadian.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.event.DiceEventThread;
import org.steelthread.arcadian.event.EventExecutor;
import org.steelthread.arcadian.event.EventTracker;

public class DiceEventConnectionImpl implements EventConnection {

  private static final Logger logger = LoggerFactory.getLogger(DiceEventConnectionImpl.class);

  private DiceConnectionHelper diceConnectionHelper = null;
  private EventTracker eventTracker = null;
  private Thread thread = null;
  private EventType[] eventsFilter = EventType.getDefaultFilterEvents();

  public DiceEventConnectionImpl(EventExecutor eventExecutor) {
    diceConnectionHelper = new DiceConnectionHelper();
    eventTracker = new DiceEventThread(diceConnectionHelper, eventExecutor);
    // Create the thread supplying it with the runnable object
    thread = new Thread((Runnable)eventTracker);
  }
  
  @Override
  public Boolean connect(String host, Integer port) {
    return diceConnectionHelper.connect(host, port);
  }

  @Override
  public void close() {
    eventTracker.stop();
    thread.stop();
    diceConnectionHelper.close();
  }

  @Override
  public Boolean authenticate(String user, String password) {
    return diceConnectionHelper.authenticate(user, password);
  }  

  @Override
  public List<Event> getEvents() {
    return eventTracker.getEvents();
  }

  @Override
  public List<Event> getEventsByType(EventType[] eventTypes) {
    List<Event> typedEvents = new ArrayList<Event>();
    // this is so we do not get a ConcurrentModification Exception
    List<Event> copyOfEvents = new ArrayList<Event>(eventTracker.getEvents());
    for (Event event : copyOfEvents) {
      for (EventType eventType : eventTypes) {
        if(eventType.equals(event.getEventType())) {
          typedEvents.add(event);
        }
      }
    }
    return typedEvents;
  }
  
  @Override
  public List<Event> getEventsUsingFilter() {
    return getEventsByType(eventsFilter);
  }

  @Override
  public void setEventFilter(EventType[] eventTypes) {
    eventsFilter = eventTypes;
  }

  @Override
  public void startReceivingEvents() {
    // Start receiving events
    List<String> commands = Arrays.asList(new String[] { "admin.eventsEnabled", "true" });
    diceConnectionHelper.sendCommand(commands);
    // Start the thread
    thread.start();
  }

  @Override
  public EventTracker getEventTracker() {
    return eventTracker;
  }
}
package org.steelthread.arcadian.event;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.network.DiceConnectionHelper;

public class DiceEventThread implements Runnable, EventTracker {

  private static final Logger logger = LoggerFactory.getLogger(DiceEventThread.class);
  private DiceConnectionHelper diceConnectionHelper;
  private EventExecutor eventExecutor;
  private volatile boolean runTask = false;
  private List<Event> events = Collections.synchronizedList(new LinkedList<Event>());
  private static final int LIST_SIZE = 10000;
    
  public DiceEventThread(DiceConnectionHelper diceConnectionHelper, EventExecutor eventExecutor) {
    this.diceConnectionHelper = diceConnectionHelper;
    this.eventExecutor = eventExecutor;
  }

  @Override
  public void addEvent(Event event) {
    getEvents().add(event);
  }

  @Override
  public void run() {
    // This allows us to sit and receive commands 
    runTask = true;    
    while(runTask) {
       List<String> response = diceConnectionHelper.recv();   
       /* make sure response is valid */ 
       if (response == null || response.size() == 1)
       {
         logger.error("error while receiving server response");
         continue;
       }
       /* print the server response */
       logger.debug("server response:");
       for(String word : response) {
         logger.debug(word+" ");
       }
       int sequenceNum = Integer.parseInt(response.get(0));
       String eventName = response.get(1);
       StringBuilder eventMessage = new StringBuilder();
       ListIterator<String> listIterator= response.listIterator(2);
       for (Iterator<String> iterator = listIterator; listIterator.hasNext();) {
        String word = (String) iterator.next();
        eventMessage.append(word);
        eventMessage.append(" ");
       }
       EventType eventType = getMappedEventType(eventName);
       Event event = new Event(sequenceNum, eventMessage.toString(), eventType, eventName, new Date());         
       // keep the size manageable
       if(getEvents().size() > LIST_SIZE) {
         ((LinkedList) getEvents()).removeLast();
       }
       getEvents().add(event);
       eventExecutor.createEvent(event);
       logger.debug("events size:" + getEvents().size());
    }    
  }
  
  protected EventType getMappedEventType(String eventName) {
    if(StringUtils.startsWith(eventName, "player")) {
      return EventType.PLAYER;
    } else if (StringUtils.startsWith(eventName, "server")) {
      return EventType.SERVER;
    } else if (StringUtils.startsWith(eventName, "punkBuster")) {
      return EventType.PUNKBUSTER;
    } else if (StringUtils.startsWith(eventName, "admin")) {
      return EventType.ADMIN;
    } else if (StringUtils.startsWith(eventName, "login")) {
      return EventType.LOGIN;
    }
    return EventType.MISC;
  }
  
  public void stop() {
    runTask = false;
  }
  
  public List<Event> getEvents() {
    return events;
  }  
}
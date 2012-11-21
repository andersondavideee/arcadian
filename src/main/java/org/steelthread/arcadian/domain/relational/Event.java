package org.steelthread.arcadian.domain.relational;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.steelthread.arcadian.domain.logical.EventType;

@Entity
@Table(name="event")
@SequenceGenerator(name = "idSequence", sequenceName = "event_id_seq")
public class Event extends AbstractRelational implements Comparable<Event> {

  public Event(int sequenceNum, String event, EventType eventType, String eventName, Date date) {
    this.sequenceNum = sequenceNum;
    this.event = event;
    this.eventType = eventType;
    this.eventName = eventName;
    this.date = date;
  }
  
  public Event(){}

  private int sequenceNum;
  private String event;
  private String eventName;
  private EventType eventType;
  private Date date;

  @Transient
  public Date getDate() {
    return date;
  }

  @Column(name = "name")
  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  @Column(name = "type")
  @Enumerated(EnumType.STRING)  
  public EventType getEventType() {
    return eventType;
  }

  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  @Column(name = "sequence_num")
  public int getSequenceNum() {
    return sequenceNum;
  }

  public void setSequenceNum(int sequenceNum) {
    this.sequenceNum = sequenceNum;
  }

  @Column(name = "event")
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }
  
  public int compareTo(Event o) {
    //default is ascending
    return new CompareToBuilder().append(this.getSequenceNum(), o.getSequenceNum()).toComparison();
  }  
}
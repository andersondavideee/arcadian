package org.steelthread.arcadian.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.steelthread.arcadian.dao.EventDao;
import org.steelthread.arcadian.domain.relational.Event;

@Component
@Transactional
public class EventExecutor {

  @Autowired
  protected EventDao eventDao;
  
  public void createEvent(Event event) {
    eventDao.create(event);
  }
}

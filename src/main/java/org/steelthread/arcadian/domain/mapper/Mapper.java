package org.steelthread.arcadian.domain.mapper;

import java.util.List;

import org.steelthread.arcadian.domain.relational.Command;


public interface Mapper <T> {

  public T map(List<String> data, Command command, Long serverId);
  
}

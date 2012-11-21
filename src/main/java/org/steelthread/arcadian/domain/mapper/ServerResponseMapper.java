package org.steelthread.arcadian.domain.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ServerResponseMapper {

  public String map(List<String> response) {
    return response.get(1);
  }
  
}

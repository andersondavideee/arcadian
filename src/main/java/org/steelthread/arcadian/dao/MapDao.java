package org.steelthread.arcadian.dao;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.relational.Map;
import org.steelthread.arcadian.server.ServerType;

@Repository
public class MapDao extends GenericDao<Map, Long> {

  public Map findByName(String name, ServerType serverType) {
    Map map = new Map();
    map.setName(name);
    map.setServerType(serverType);
    return queryByExampleFindOne(map);
  } 

}

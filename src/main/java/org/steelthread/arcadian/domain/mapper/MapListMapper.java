package org.steelthread.arcadian.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.steelthread.arcadian.dao.MapDao;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.domain.logical.ServerMap;
import org.steelthread.arcadian.domain.relational.Command;

@Component
public class MapListMapper implements ListMapper<ServerMap> {

  @Inject
  protected MapDao mapDao;
  @Inject
  protected ServerDao serverDao;
  
  private static final int ARRAY_SIZE = 3;
  
  /**
   * 
     This describes the set of maps which the server rotates through :
     <number of maps: integer> - number of maps that follow
     <number of words per map: integer> - number of words per map
     <map name: string> - name of map
     <gamemode name: string> - name of gamemode
     <number of rounds: integer> - number of rounds to play on map before switching
     REQUEST: mapList.list
     RESPONSE:
     [3, OK, 3, 3, MP_001, ConquestSmall0, 2, MP_001, ConquestLarge0, 2, MP_001, RushLarge0, 2]
   */  
  public List<ServerMap> map(List<String> data, Command command, Long serverId) {
    List<ServerMap> serverMaps = new ArrayList<ServerMap>();
    // skip ahead of the first 4 positions (sequence number, status, number of maps, number of words per map)
    List<String> subDataList = data.subList(4, data.size());
    int tokenCount = 0;
    String[] tokens = new String[ARRAY_SIZE];
    // break into chunks of 3 - then map and add
    int indexCount = 0;
    for (String mapData : subDataList) {
      tokens[tokenCount] = mapData;
      tokenCount++;
      if(tokenCount == ARRAY_SIZE) {
        serverMaps.add(mapServerMap(tokens, serverId, indexCount));
        // reset
        tokenCount = 0;
        tokens = new String[ARRAY_SIZE];
        // increment index
        indexCount++;
      }
    }
    return serverMaps;
  }
  
  protected ServerMap mapServerMap(String[] tokens, Long serverId, int indexCount) {
    ServerMap serverMap = new ServerMap(serverId);
    int token = 0;
    String mapName = tokens[token++];
    serverMap.setName(mapName);
    serverMap.setGameMode(tokens[token++]);
    serverMap.setNumberOfRounds(Integer.parseInt(tokens[token++]));
    serverMap.setIndex(indexCount);
    serverMap.setExternalName(mapDao.findByName(mapName, serverDao.get(serverId).getServerType()).getExternalName());
    return serverMap;
  }
}
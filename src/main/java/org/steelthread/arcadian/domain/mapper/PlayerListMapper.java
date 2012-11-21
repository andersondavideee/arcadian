package org.steelthread.arcadian.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.logical.Player;
import org.steelthread.arcadian.domain.relational.Command;

@Component
public class PlayerListMapper implements ListMapper<Player> {

  private static final int ARRAY_SIZE = 7;
  private static final Logger logger = LoggerFactory.getLogger(PlayerListMapper.class);

  /**
   * Player info block
    The standard set of info for a group of players contains a lot of different fields. To reduce the risk of having to do
    backwards-incompatible changes to the protocol, the player info block includes some formatting information.
    <number of parameters> - number of parameters for each player
    N x <parameter type: string> - the parameter types that will be sent below
    <number of players> - number of players following
    M x N x <parameter value>
    - all parameter values for player 0, then all parameter values for player 1, etc.
    Current parameters:
    name - 
    guid
    teamId
    squadId
    kills - number of kills, as shown in the in-game scoreboard
    deaths - number of deaths, as shown in the in-game scoreboard
    score - current score
    
     REQUEST: admin.listPlayers
     RESPONSE:
     [3, OK, 7, name, guid, teamId, squadId, kills, deaths, score, 2, Pablo66666, EA_8BA9E29F46E173EBFF4997AC3FE54A06, 1, 2, 16, 6, 1886, HarzTod, EA_1AD3F41A635FB2371FD89AB7CB57D987, 1, 2, 7, 6, 700]
   */  
  public List<Player> map(List<String> data, Command command, Long serverId) {
    List<Player> players = new ArrayList<Player>();
    // skip ahead of the first 11 positions
    List<String> subDataList = data.subList(11, data.size());
    int tokenCount = 0;
    String[] tokens = new String[ARRAY_SIZE];
    // break into chunks of 7 - then map and add
    for (String mapData : subDataList) {
      tokens[tokenCount] = mapData;
      tokenCount++;
      if(tokenCount == ARRAY_SIZE) {
        players.add(mapPlayer(tokens, serverId));
        // reset
        tokenCount = 0;
        tokens = new String[ARRAY_SIZE];
      }
    }
    return players;
  }
  
  protected Player mapPlayer(String[] tokens, Long serverId) {
    int token = 0;
    String name = tokens[token++];
    String guid = tokens[token++];
    Player player = new Player(serverId, guid);
    player.setName(name);
    player.setGuid(guid);
    player.setTeamId(Long.parseLong(tokens[token++]));
    player.setSquadId(Long.parseLong(tokens[token++]));
    player.setKills(Long.parseLong(tokens[token++]));
    player.setDeaths(Long.parseLong(tokens[token++]));
    player.setScore(Long.parseLong(tokens[token++]));
    if(logger.isDebugEnabled()) {
      logger.debug(player.toString());      
    }
    return player;
  }
}
package org.steelthread.arcadian.domain.mapper;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.dao.MapDao;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.domain.logical.ServerInfo;
import org.steelthread.arcadian.domain.relational.Command;

@Component
public class ServerInfoMapper implements Mapper<ServerInfo> {

  private static final Logger logger = LoggerFactory.getLogger(ServerInfoMapper.class);
  
  @Inject
  protected MapDao mapDao;
  @Inject
  protected ServerDao serverDao;
  
  /**
     <serverName: string>
     <current playercount: integer>
     <max playercount: integer>
     <current gamemode: string>
     <current map: string>
     <roundsPlayed: integer>
     <roundsTotal: string>
     <scores: team scores>
     <onlineState: online state>
     <ranked: boolean>
     <punkBuster: boolean>
     <hasGamePassword: boolean>
     <serverUpTime: seconds>
     <roundTime: seconds>
     <gameIpAndPort: IpPortPair>
     <punkBusterVersion: string>
     <joinQueueEnabled: boolean>
     <region: string>
     <closestPingSite: string>
     <country: string>
   * REQUEST: serverinfo
   * RESPONSE:
   * [6, OK, KorovaMilkBar Wednesday Night Happy Hour, 0, 16, ConquestSmall0, MP_001, 0, 2, 2, 200, 200, 0, , true, true, false, 397227, 236259, , , , NAm, dfw, US, false]
   * 152 OK KorovaMilkBar Wednesday Night Happy Hour 0 16 SquadDeathMatch0 XP2_Skybar 0 1 4(team scores that follow) 0 0 0 0 40 true true false 160611 68610 NAm dfw US false 
   */ 
  public ServerInfo map(List<String> data, Command command, Long serverId) {
    ServerInfo serverInfo = new ServerInfo(serverId);
    String[] strings = data.toArray(new String[0]);
    int token = 2;
    int teamScoresThatFollow = 0;
    serverInfo.setName(strings[token++]);
    serverInfo.setCurrentPlayerCount(Integer.parseInt(strings[token++]));
    serverInfo.setMaxPlayerCount(Integer.parseInt(strings[token++]));
    serverInfo.setGamemode(strings[token++]);
    String currentMap = strings[token++];
    serverInfo.setCurrentMap(currentMap);
    serverInfo.setCurrentMapExternalName(mapDao.findByName(currentMap, serverDao.get(serverId).getServerType()).getExternalName());
    try {
      serverInfo.setRoundsPlayed(Integer.parseInt(strings[token++]) + 1); // for some reason this starts at zero - increment by 1 to make sense
      serverInfo.setRoundsTotal(Integer.parseInt(strings[token++]));
      teamScoresThatFollow = Integer.parseInt((strings[token++])); // # of team scores that follow 
      serverInfo.setTeam1Score(Integer.parseInt(strings[token++])); // Team 1 Score
      serverInfo.setTeam2Score(Integer.parseInt(strings[token++])); // Team 2 Score
      if(teamScoresThatFollow > 2) {
        serverInfo.setTeam3Score(Integer.parseInt(strings[token++])); // Team 3 Score
        serverInfo.setTeam4Score(Integer.parseInt(strings[token++])); // Team 4 Score        
      }
      token++; // # ?  <onlineState: online state> 
      token++; // # ?  <onlineState: online state>
      serverInfo.setRanked(Boolean.valueOf(strings[token++]));
      serverInfo.setPunkbuster(Boolean.valueOf(strings[token++]));
      serverInfo.setHasGamePassword(Boolean.valueOf(strings[token++]));
      serverInfo.setServerUpTime(Long.valueOf(strings[token++]));
      serverInfo.setRoundTime(Long.valueOf(strings[token++]));
    } catch (NumberFormatException e) {
      // when the server is changing maps not all information is available, so just catch and hope it doesnt happen next time
      logger.warn("NumberFormatException:" + e.getMessage());
    }
    return serverInfo;
  }
}
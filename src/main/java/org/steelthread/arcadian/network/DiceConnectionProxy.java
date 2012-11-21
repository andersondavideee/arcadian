package org.steelthread.arcadian.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steelthread.arcadian.domain.logical.GamemodeType;
import org.steelthread.arcadian.domain.logical.ServerMap;
import org.steelthread.arcadian.domain.relational.Command;

public class DiceConnectionProxy implements Connection {

  private static final Logger logger = LoggerFactory.getLogger(DiceConnectionProxy.class);
  private static final String COMMA = ",";

  private Command commandToSend;
  private List<String> updateValues;
  private String updateValue;
  private int nextMap = 0;
  private Map<String, ProxyPlayer> players = new ConcurrentHashMap<String, ProxyPlayer>();
  private List<String> addedPlayer = new ArrayList<String>();
  private List<ServerMap> serverMaps = new ArrayList<ServerMap>();
  
  public DiceConnectionProxy() {
    serverMaps.add(new ServerMap(1l, "MP_001", GamemodeType.ConquestAssaultLarge0.name(), 2, 0, "Foobar"));
    serverMaps.add(new ServerMap(1l, "MP_001", GamemodeType.ConquestAssaultSmall0.name(), 2, 0, "Foobar"));
    serverMaps.add(new ServerMap(1l, "MP_003", GamemodeType.ConquestLarge0.name(), 2, 0, "Foobar"));
    serverMaps.add(new ServerMap(1l, "XP1_001", GamemodeType.SquadDeathMatch0.name(), 2, 0, "Foobar"));
    rebuildPlayerList();
  }

  protected void rebuildPlayerList() {
    players.clear();
    String team0PlayerName = "team0Player---";
    String team1PlayerName = "team1Player---";
    String team2PlayerName = "team2Player---";
    // team 0 test players
    buildPlayers(team0PlayerName, 0, 1, 1);
    // team 1 test players
    buildPlayers(team1PlayerName, 1, 16, 8);
    // team 2 test players
    buildPlayers(team2PlayerName, 2, 16, 8);
  }

  protected void rebuildSquadDeathMatchPlayerList() {
    players.clear();
    String team0PlayerName = "team0Player---";
    String team1PlayerName = "squadAPlayer---";
    String team2PlayerName = "squadBPlayer---";
    String team3PlayerName = "squadCPlayer---";
    String team4PlayerName = "squadDPlayer---";
    // team 0 test players
    buildPlayers(team0PlayerName, 0, 1, 1);
    buildPlayers(team1PlayerName, 1, 1, 1);
    buildPlayers(team2PlayerName, 2, 3, 1);
    buildPlayers(team3PlayerName, 3, 2, 1);
    buildPlayers(team4PlayerName, 4, 4, 1);
  }

  protected void buildPlayers(String teamPlayerName, int teamId, int playerTeamSize, int squadSize) {
    int countSquadSize = 1;
    for (int i = 0; i < playerTeamSize; i++) {
      String playerName = teamPlayerName + i;
      if(countSquadSize > squadSize ) {
        countSquadSize = 1;
      }
      players.put(playerName, buildPlayer(playerName, teamId, countSquadSize));
      countSquadSize++;
    }    
  }
  
  protected void scramblePlayerInformation() {
    Map<String, ProxyPlayer> newPlayers =  new ConcurrentHashMap<String, ProxyPlayer>();
    for (Map.Entry<String,ProxyPlayer> entry : players.entrySet()) {
      ProxyPlayer proxyPlayer = entry.getValue();
      newPlayers.put(entry.getKey(), buildPlayer(entry.getKey(), proxyPlayer.teamId, proxyPlayer.squadId));
    }
    players.clear();
    players.putAll(newPlayers);
    // add 1 more player - remove a random player
    RandomData randomData = new RandomDataImpl( );
    int teamId = randomData.nextInt(1,2);
    int squadId = randomData.nextInt(1,8);
    String key = String.valueOf(randomData.nextInt(1,500));
    addedPlayer.add(key);
    players.put(key, buildPlayer(key, teamId, squadId));
    if(addedPlayer.size() > 1) {
      // remove the previous player
      players.remove(addedPlayer.get(0));
      addedPlayer.remove(0);      
    }
  }
  
  @Override
  public List<String> send(Command command) {
    logger.debug("Command to send:" + command.getCommand());
    setCommandToSend(command);
    return recv();
  }

  @Override
  public List<String> sendUpdate(Command command, String updateValue) {
    logger.debug("Command to send:" + command.getCommand() + " updateValue:" + updateValue);
    setCommandToSend(command);
    setUpdateValue(updateValue);
    return recv();
  }

  @Override
  public List<String> sendUpdate(Command command, List<String> updateValues) {
    logger.debug("Command to send:" + command.getCommand() + " updateValues:" + updateValues);
    setCommandToSend(command);
    setUpdateValues(updateValues);
    return recv();
  }
  
  @Override
  public List<String> sendUpdate(Command command) {
    logger.debug("Command to send:" + command.getCommand());
    setCommandToSend(command);
    return recv();
  }
  
  protected synchronized List<String> recv() {
    List<String> recv = new ArrayList<String>();
    String response = null;
    String playerName = null;
    switch (commandToSend.getCommandConstant()) {
    case PLAYERLIST:
      StringBuilder stringBuilder = new StringBuilder();
      if(GamemodeType.SquadDeathMatch0.equals(GamemodeType.valueOf(serverMaps.get(nextMap).getGameMode()))) {
        rebuildSquadDeathMatchPlayerList();
      } else {
        scramblePlayerInformation();
      }
      // build the list of players and represent as a string
      for (Map.Entry<String,ProxyPlayer> entry : players.entrySet()) {
        stringBuilder.append(entry.getValue().playerString);
      }        
      response = "3, OK, 7, name, guid, teamId, squadId, kills, deaths, score," + players.size() + COMMA + stringBuilder.toString();
      break;
    case MAPLIST:
      StringBuilder mapBuilder = new StringBuilder();
      int count  = 1;
      for (ServerMap serverMap : serverMaps) {
        mapBuilder.append(serverMap.getName());
        mapBuilder.append(",");
        mapBuilder.append(serverMap.getGameMode());
        mapBuilder.append(",");
        mapBuilder.append(serverMap.getNumberOfRounds());
        if(count < serverMaps.size()) {
          mapBuilder.append(",");          
        }
        count++;
      }
      response = "3, OK, "+ serverMaps.size() + ",3," + mapBuilder.toString();
      break;
    case SERVERINFO:
      String gamemode = serverMaps.get(nextMap).getGameMode();
      String mapName = serverMaps.get(nextMap).getName();
      response = "5,OK,KorovaMilkBar Wednesday Night Happy Hour,0,64," + gamemode + "," + mapName + ",0,2,2,240,240,0,0,true,true,false,487373,61900,0,0,0,NAm,dfw,US,false";
      break;
    case KICKPLAYER:
      playerName = updateValue;
      logger.debug("removing player:" + playerName);
      players.remove(playerName);
      response = "2, OK";
      break;
    case TEAMKILLCOUNTFORKICK:
      response = "2, OK";
      break;
    case BAN:
      playerName = updateValues.get(1);
      logger.debug("removing player:" + playerName);
      players.remove(playerName);
      response = "2, OK";
      break;
    case MOVEPLAYER:
      // update player list to reflect the move
      playerName = updateValues.get(0);
      int teamId = Integer.parseInt(updateValues.get(1));
      int squadId = Integer.parseInt(updateValues.get(2));
      // only allow a move if < 4 players on a squad and team
      int squadCount = 1;
      for (Map.Entry<String,ProxyPlayer> entry : players.entrySet()) {
        if(teamId == entry.getValue().teamId) {
          if(squadId == entry.getValue().squadId) {
            squadCount++;
          }
        }
      }
      if(squadCount > 4) {
        response = "2, InvalidSquadId";
        break;
      }
      players.put(playerName, buildPlayer(playerName, teamId, squadId));
      response = "2, OK";
      break;
    case NEXTMAP:
      nextMap = Integer.valueOf(updateValue);
    case ADDMAP:
    case REMOVEMAP:
    case RUNNEXTROUND:
    case RESTARTROUND:
      response = "2, OK";
      break;      
    default:
      throw new RuntimeException("CommandConstant Not Supported:" + commandToSend.getCommandConstant());
    }
    logger.debug("response:" + response);
    StringTokenizer stringTokenizer = new StringTokenizer(response, ",");
    while(stringTokenizer.hasMoreTokens()) {
      recv.add(StringUtils.trim(stringTokenizer.nextToken()));
    }
    return recv;
  }
  
  
  private class ProxyPlayer {
    private String playerString;
    private int teamId;
    private int squadId;
    
    public ProxyPlayer(String playerString, int teamId, int squadId) {
      this.playerString = playerString;
      this.teamId = teamId;
      this.squadId = squadId;
    }    
  }
  
  protected ProxyPlayer buildPlayer(String name, int teamId, int squadId) {
    /*
    name
    guid
    teamId
    squadId
    kills
    deaths
    score
    */
    Random randomGenerator = new Random();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(name);
    stringBuilder.append(COMMA);
    stringBuilder.append(name); // GUID
    stringBuilder.append(COMMA);
    stringBuilder.append(teamId);
    stringBuilder.append(COMMA);
    stringBuilder.append(squadId);
    stringBuilder.append(COMMA);
    stringBuilder.append(String.valueOf(randomGenerator.nextInt(20)));
    stringBuilder.append(COMMA);
    stringBuilder.append(String.valueOf(randomGenerator.nextInt(20)));
    stringBuilder.append(COMMA);
    stringBuilder.append(String.valueOf(randomGenerator.nextInt(5000))); 
    stringBuilder.append(COMMA);
    logger.debug("Player:" + stringBuilder.toString());
    return new ProxyPlayer(stringBuilder.toString(), teamId, squadId);
  }

  @Override
  public Boolean connect(String host, Integer port) {
    return true;
  }

  @Override
  public Boolean isConnectionValid() {
    return true;
  }
  
  @Override
  public Boolean authenticate(String user, String password) {
    return true;
  }
  
  @Override
  public void close() {
    logger.debug("Connection closed");
  }

  @Override
  public List<String> sendServerCommand(String command, List<String> updateValues) {
    throw new RuntimeException("operation not supported");
  }

  @Override
  public List<String> sendServerCommand(Command command, List<String> updateValues) {
    setCommandToSend(command);
    setUpdateValues(updateValues);
    return recv();
 }


  public void setCommandToSend(Command commandToSend) {
    this.commandToSend = commandToSend;
  }
  
  public void setUpdateValues(List<String> updateValues) {
    this.updateValues = updateValues;
  }
  
  public void setUpdateValue(String updateValue) {
    this.updateValue = updateValue;
  }
  
}
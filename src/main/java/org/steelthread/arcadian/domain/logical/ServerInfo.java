package org.steelthread.arcadian.domain.logical;

public class ServerInfo extends AbstractLogicalObject {

  public ServerInfo(Long serverId) {
    super(serverId);
  }
  
  public ServerInfo() {
  }  
  
  private String name;
  private int currentPlayerCount;
  private int maxPlayerCount;  
  private String currentMap;
  private String currentMapExternalName;
  private String gamemode;
  private int roundsPlayed;
  private int roundsTotal;
  private boolean ranked;
  private boolean punkbuster;
  private boolean hasGamePassword;
  private long serverUpTime;
  private long roundTime;
  private int team1Score;
  private int team2Score;
  private int team3Score;
  private int team4Score;
  
  public int getTeam1Score() {
    return team1Score;
  }

  public void setTeam1Score(int team1Score) {
    this.team1Score = team1Score;
  }

  public int getTeam2Score() {
    return team2Score;
  }

  public void setTeam2Score(int team2Score) {
    this.team2Score = team2Score;
  }

  public int getTeam3Score() {
    return team3Score;
  }

  public void setTeam3Score(int team3Score) {
    this.team3Score = team3Score;
  }

  public int getTeam4Score() {
    return team4Score;
  }

  public void setTeam4Score(int team4Score) {
    this.team4Score = team4Score;
  }
  
  public int getRoundsPlayed() {
    return roundsPlayed;
  }

  public void setRoundsPlayed(int roundsPlayed) {
    this.roundsPlayed = roundsPlayed;
  }

  public int getRoundsTotal() {
    return roundsTotal;
  }

  public void setRoundsTotal(int roundsTotal) {
    this.roundsTotal = roundsTotal;
  }

  public boolean isRanked() {
    return ranked;
  }

  public void setRanked(boolean ranked) {
    this.ranked = ranked;
  }

  public boolean isPunkbuster() {
    return punkbuster;
  }

  public void setPunkbuster(boolean punkbuster) {
    this.punkbuster = punkbuster;
  }

  public boolean isHasGamePassword() {
    return hasGamePassword;
  }

  public void setHasGamePassword(boolean hasGamePassword) {
    this.hasGamePassword = hasGamePassword;
  }

  public long getServerUpTime() {
    return serverUpTime;
  }

  public void setServerUpTime(long serverUpTime) {
    this.serverUpTime = serverUpTime;
  }

  public long getRoundTime() {
    return roundTime;
  }

  public void setRoundTime(long roundTime) {
    this.roundTime = roundTime;
  }

  public String getGamemode() {
    return gamemode;
  }

  public void setGamemode(String gamemode) {
    this.gamemode = gamemode;
  }

  public String getCurrentMapExternalName() {
    return currentMapExternalName;
  }

  public void setCurrentMapExternalName(String currentMapExternalName) {
    this.currentMapExternalName = currentMapExternalName;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getCurrentPlayerCount() {
    return currentPlayerCount;
  }
  public void setCurrentPlayerCount(int currentPlayerCount) {
    this.currentPlayerCount = currentPlayerCount;
  }
  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }
  public void setMaxPlayerCount(int maxPlayerCount) {
    this.maxPlayerCount = maxPlayerCount;
  }
  public String getCurrentMap() {
    return currentMap;
  }
  public void setCurrentMap(String currentMap) {
    this.currentMap = currentMap;
  }
}
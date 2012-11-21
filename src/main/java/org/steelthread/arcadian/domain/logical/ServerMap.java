package org.steelthread.arcadian.domain.logical;

public class ServerMap extends AbstractLogicalObject {

  public ServerMap(Long serverId) {
    super(serverId);
  }
  
  public ServerMap() {
  }  
  
  public ServerMap(Long serverId, String name, String gameMode, int numberOfRounds, int index, String externalName) {
    super(serverId);
    this.name = name;
    this.gameMode = gameMode;
    this.numberOfRounds = numberOfRounds;
    this.index = index;
    this.externalName = externalName;
  }

  private String name;
  private String gameMode;
  private int numberOfRounds; 
  private int index;
  private String externalName;
   
  public String getExternalName() {
    return externalName;
  }

  public void setExternalName(String externalName) {
    this.externalName = externalName;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getGameMode() {
    return gameMode;
  }
  public void setGameMode(String gameMode) {
    this.gameMode = gameMode;
  }
  public int getNumberOfRounds() {
    return numberOfRounds;
  }
  public void setNumberOfRounds(int numberOfRounds) {
    this.numberOfRounds = numberOfRounds;
  }
}
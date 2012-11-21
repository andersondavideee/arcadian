package org.steelthread.arcadian.domain.logical;

import java.text.DecimalFormat;

public class Player extends AbstractLogicalObject {

  public Player(Long serverId, String guid) {
    this.serverId = serverId;
    this.id = guid;
  }
  
  public Player() {
  }  
  
  public String name;
  public String guid;
  public Long teamId;
  public Long squadId;
  public Long kills;
  public Long deaths;
  public Long score;
  public String killDeathRatio;
  
  public String getKillDeathRatio() {
    Double value = deaths != null && deaths != 0 ? new Double(kills)/ new Double(deaths) : 0;
    DecimalFormat df = new DecimalFormat("###.##");
    return df.format(value);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public Long getTeamId() {
    return teamId;
  }

  public void setTeamId(Long teamId) {
    this.teamId = teamId;
  }

  public Long getSquadId() {
    return squadId;
  }

  public void setSquadId(Long squadId) {
    this.squadId = squadId;
  }

  public Long getKills() {
    return kills;
  }

  public void setKills(Long kills) {
    this.kills = kills;
  }

  public Long getDeaths() {
    return deaths;
  }

  public void setDeaths(Long deaths) {
    this.deaths = deaths;
  }

  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }
}
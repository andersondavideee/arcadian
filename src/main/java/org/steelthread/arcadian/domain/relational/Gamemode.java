package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.steelthread.arcadian.domain.logical.GamemodeType;
import org.steelthread.arcadian.server.ServerType;

@Entity
@Table(name="gamemode")
@SequenceGenerator(name = "idSequence", sequenceName = "gamemode_id_seq")
public class Gamemode extends AbstractRelational {

  public Gamemode(GamemodeType name, String externalName, ServerType serverType) {
    this.name = name;
    this.externalName = externalName;
    this.serverType = serverType;
  }

  public Gamemode() {}
  
  private GamemodeType name;
  private String externalName;
  private ServerType serverType;
  private Set<Map> maps = new HashSet<Map>(0);
  
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "gamemodes")
  @JsonIgnore
  public Set<Map> getMaps() {
    return maps;
  }

  public void setMaps(Set<Map> maps) {
    this.maps = maps;
  }

  @Column(name = "name")
  @Enumerated(EnumType.STRING)
  public GamemodeType getName() {
    return name;
  }
  
  public void setName(GamemodeType name) {
    this.name = name;
  }

  @Column(name = "external_name")
  public String getExternalName() {
    return externalName;
  }
  public void setExternalName(String externalName) {
    this.externalName = externalName;
  }
  
  @Column(name = "server_type")
  @Enumerated(EnumType.STRING)
  public ServerType getServerType() {
    return serverType;
  }
  public void setServerType(ServerType serverType) {
    this.serverType = serverType;
  }
}
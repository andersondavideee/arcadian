package org.steelthread.arcadian.domain.relational;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.steelthread.arcadian.server.ServerType;

@Entity
@Table(name="map")
@SequenceGenerator(name = "idSequence", sequenceName = "map_id_seq")
public class Map extends AbstractRelational {

  public Map(String name, String externalName, ServerType serverType, Set<Gamemode> gamemodes) {
    this.name = name;
    this.externalName = externalName;
    this.serverType = serverType;
    this.gamemodes = gamemodes;
  }

  public Map(String name, String externalName, ServerType serverType, List<Gamemode> gamemodes) {
    this(name, externalName, serverType, new HashSet<Gamemode>(gamemodes));
  }
  
  public Map() {}
  
  private String name;
  private String externalName;
  private ServerType serverType;
  
  private Set<Gamemode> gamemodes = new HashSet<Gamemode>(0);
 
  @Column(name = "name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
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

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "map_gamemode", joinColumns = { @JoinColumn(name = "map_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "gamemode_id", nullable = false, updatable = false) })
  @JsonManagedReference
  public Set<Gamemode> getGamemodes() {
    return gamemodes;
  }
  public void setGamemodes(Set<Gamemode> gamemodes) {
    this.gamemodes = gamemodes;
  } 
}
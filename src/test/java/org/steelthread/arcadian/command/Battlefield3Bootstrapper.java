package org.steelthread.arcadian.command;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.steelthread.arcadian.dao.AbstractDaoImpl;
import org.steelthread.arcadian.dao.CommandDao;
import org.steelthread.arcadian.dao.GamemodeDao;
import org.steelthread.arcadian.dao.MapDao;
import org.steelthread.arcadian.domain.logical.GamemodeType;
import org.steelthread.arcadian.domain.relational.AbstractRelational;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.domain.relational.Gamemode;
import org.steelthread.arcadian.domain.relational.Map;
import org.steelthread.arcadian.server.ServerType;
import org.steelthread.arcadian.util.ArcadianConstants;

import com.thoughtworks.xstream.XStream;

@ContextConfiguration(locations = { ArcadianConstants.APPLICATION_CONTEXT })
@TransactionConfiguration(defaultRollback = false)
public class Battlefield3Bootstrapper extends AbstractTransactionalJUnit4SpringContextTests {

  @Inject
  private CommandDao commandDao;
  @Inject
  private GamemodeDao gamemodeDao;
  @Inject
  private MapDao mapDao;
  
  /**
   * mvn test -Dtest=Battlefield3Bootstrapper#execute
   */
  @Test
  public void execute() throws IOException {
    // Remove all of the existing items for BF3
    removeByServerType(commandDao, ServerType.BATTLEFIELD3);
    commandDao.flush();
    // Get All the BF3 commands and insert
    XStream xStream = new XStream();
    ClassPathResource classPathResource = new ClassPathResource("Battlefield3Commands.xml");
    List<Command>commands = (List) xStream.fromXML(classPathResource.getInputStream());
    for (Command command : commands) {
      commandDao.create(command);
      commandDao.flush();
    }
    // remove gamemodes and maps
    removeByServerType(mapDao, ServerType.BATTLEFIELD3);    
    removeByServerType(gamemodeDao, ServerType.BATTLEFIELD3);
    gamemodeDao.flush();
    // create gamemodes
    createGamemodes(gamemodeDao);
    gamemodeDao.flush();
    // create maps
    createMaps(mapDao, gamemodeDao);
    createXPack1Maps(mapDao, gamemodeDao);
    createXPack2Maps(mapDao, gamemodeDao);
    createXPack3Maps(mapDao, gamemodeDao);
    createXPack4Maps(mapDao, gamemodeDao);
  }
  
  protected void createGamemodes(GamemodeDao gamemodeDao) {
    gamemodeDao.create(new Gamemode(GamemodeType.ConquestLarge0, "Conquest64", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.ConquestSmall0, "Conquest", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.RushLarge0, "Rush", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.SquadRush0, "Squad Rush", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.SquadDeathMatch0, "Squad Deathmatch", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.TeamDeathMatch0, "Team Deathmatch", ServerType.BATTLEFIELD3));

    //XP1
    gamemodeDao.create(new Gamemode(GamemodeType.ConquestAssaultLarge0, "ConquestAssault64", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.ConquestAssaultSmall0, "ConquestAssault", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.ConquestAssaultSmall1, "ConquestAssault Alternate 2", ServerType.BATTLEFIELD3));

    //XP2
    gamemodeDao.create(new Gamemode(GamemodeType.Domination0, "Conquest Domination", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.GunMaster0, "Gun master", ServerType.BATTLEFIELD3));
    gamemodeDao.create(new Gamemode(GamemodeType.TeamDeathMatchC0, "TDM Close Quarters", ServerType.BATTLEFIELD3));
    
    // XP3
    gamemodeDao.create(new Gamemode(GamemodeType.TankSuperiority0, "Tank Superiority", ServerType.BATTLEFIELD3));

    // XP4
    gamemodeDao.create(new Gamemode(GamemodeType.Scavenger0, "Scavenger", ServerType.BATTLEFIELD3));
}

  protected void createMaps(MapDao mapDao, GamemodeDao gamemodeDao) {
    mapDao.create(new Map("MP_001", "Grand Bazaar", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_003", "Teheran Highway", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_007", "Caspian Border", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_011", "Seine Crossing", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_012", "Operation Firestorm", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_013", "Damavand Peak", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_017", "Noshahr Canals", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_018", "Kharg Island", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
    mapDao.create(new Map("MP_Subway", "Operation Metro", ServerType.BATTLEFIELD3, getNormalGamemodes(gamemodeDao)));
  }

  protected void createXPack1Maps(MapDao mapDao, GamemodeDao gamemodeDao) {
    Set<Gamemode> gamemodes = new HashSet<Gamemode>();
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall1));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    mapDao.create(new Map("XP1_001", "Strike At Karkand", ServerType.BATTLEFIELD3, gamemodes));
    gamemodes.clear();
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    mapDao.create(new Map("XP1_002", "Gulf of Oman", ServerType.BATTLEFIELD3, gamemodes));
    gamemodes.clear();
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall1));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    mapDao.create(new Map("XP1_003", "Sharqi Peninsula", ServerType.BATTLEFIELD3, gamemodes));
    gamemodes.clear();
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestAssaultSmall1));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    mapDao.create(new Map("XP1_004", "Wake Island", ServerType.BATTLEFIELD3, gamemodes));
  }
  
  protected void createXPack2Maps(MapDao mapDao, GamemodeDao gamemodeDao) {
    Set<Gamemode> gamemodes = new HashSet<Gamemode>();
    addStandardXpack2Gamemodes(gamemodes);
    mapDao.create(new Map("XP2_Factory", "Scrapmetal", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP2_Office", "Operation 925", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP2_Palace", "Donya Fortress", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP2_Skybar", "Ziba Tower", ServerType.BATTLEFIELD3, gamemodes));
  }

  protected void createXPack3Maps(MapDao mapDao, GamemodeDao gamemodeDao) {
    Set<Gamemode> gamemodes = new HashSet<Gamemode>();
    addStandardXpack3Gamemodes(gamemodes);
    mapDao.create(new Map("XP3_Desert", "Bandar Desert", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP3_Alborz", "Alborz Mountains", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP3_Shield", "Armored Shield", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP3_Valley", "Death Valley", ServerType.BATTLEFIELD3, gamemodes));
  }

  protected void createXPack4Maps(MapDao mapDao, GamemodeDao gamemodeDao) {
    Set<Gamemode> gamemodes = new HashSet<Gamemode>();
    addStandardXpack4Gamemodes(gamemodes);
    mapDao.create(new Map("XP4_Quake", "Epicenter", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP4_FD", "Markaz Monolith", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP4_Parl", "Azadi Palace", ServerType.BATTLEFIELD3, gamemodes));
    mapDao.create(new Map("XP4_Rubble", "Talah Market", ServerType.BATTLEFIELD3, gamemodes));
  }
  
  protected void addStandardXpack2Gamemodes(Set<Gamemode> gamemodes) {
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatchC0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.GunMaster0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.Domination0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));    
  }

  protected void addStandardXpack3Gamemodes(Set<Gamemode> gamemodes) {
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));    
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TankSuperiority0));    
  }

  protected void addStandardXpack4Gamemodes(Set<Gamemode> gamemodes) {
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));    
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));    
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.GunMaster0));    
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.Scavenger0));    
  }
  
  protected Set<Gamemode> getNormalGamemodes(GamemodeDao gamemodeDao) {
    Set<Gamemode> gamemodes = new HashSet<Gamemode>();
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.ConquestSmall0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.RushLarge0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadRush0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.SquadDeathMatch0));
    gamemodes.add(gamemodeDao.findByGamemodeType(GamemodeType.TeamDeathMatch0));
    return gamemodes;
  }

  protected void removeByServerType(AbstractDaoImpl repository, ServerType serverType) {
    List<AbstractRelational> itemsToRemove = repository.findByServerType(serverType);
    for (AbstractRelational item : itemsToRemove) {
      repository.remove(item);
    }    
  }
}
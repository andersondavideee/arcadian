package org.steelthread.arcadian.dao;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.logical.GamemodeType;
import org.steelthread.arcadian.domain.relational.Gamemode;

@Repository
public class GamemodeDao extends GenericDao<Gamemode, Long> {

  public Gamemode findByGamemodeType(GamemodeType gamemodeType) {
    Gamemode gamemode = new Gamemode();
    gamemode.setName(gamemodeType);
    return queryByExampleFindOne(gamemode);
  }  
}

package org.steelthread.arcadian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.steelthread.arcadian.command.CommandConstant;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.domain.logical.Player;
import org.steelthread.arcadian.network.ServerUpdateException;
import org.steelthread.arcadian.service.CommandRunnerService;
import org.steelthread.arcadian.util.ArcadianConstants;

/**
 * Loosely based on REST design patterns
 * http://architects.dzone.com/news/common-rest-design-pattern
 */
@Controller
@RequestMapping("/players")
@Transactional
public class PlayerController extends AbstractBaseController {

  private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
  
  @Inject
  protected ServerDao serverDao;
  
  @Inject
  protected CommandRunnerService commandRunnerService;
  
  @RequestMapping(value="{id}", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<Player> findAllPlayers(@PathVariable("id") Long id) {
    logger.debug("finding players for server:" + id);
    return commandRunnerService.runListCommand(CommandConstant.PLAYERLIST, serverDao.get(id), "all"); 
  }
  
  @RequestMapping(value="{id}/{name}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void kickPlayer(@PathVariable("id") Long id, @PathVariable("name") String name) {
    logger.debug("kicking player for server:" + id + " with name:" + name);
    commandRunnerService.runUpdateCommand(CommandConstant.KICKPLAYER, serverDao.get(id), name); 
  }  
  
  @RequestMapping(value="{id}/{name}", method = RequestMethod.PUT, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseStatus(HttpStatus.OK)
  public void movePlayer(@RequestBody Player player) {
    List<String> args = new ArrayList<String>(3);
    args.add(String.valueOf(player.getName()));
    args.add(String.valueOf(player.getTeamId()));
    args.add(String.valueOf(player.getSquadId()));
    // forcekill?
    args.add("true");
    logger.debug("moving player:" + player.getName() + " to team:" + player.getTeamId() + " and squad:" + player.getSquadId());
    // if the response is not a success from the server than send an error
    String response = commandRunnerService.runUpdateCommand(CommandConstant.MOVEPLAYER, serverDao.get(player.getServerId()), args);
    if(!StringUtils.equals(ArcadianConstants.RESPONSE_OK, response)) {
      throw new ServerUpdateException("moving player:" + player.getName() + " to team:" + player.getTeamId() + " and squad:" + player.getSquadId() + " failed with server message " + response);
    } else {
      // we do not return a value since Backbone only considers success with status of 200 and no content
      // http://stackoverflow.com/questions/7836574/how-to-get-a-good-response-from-saving-a-model      
    }    
  }    
}
package org.steelthread.arcadian.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import org.steelthread.arcadian.command.CommandType;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.domain.logical.Action;
import org.steelthread.arcadian.domain.logical.Admin;
import org.steelthread.arcadian.domain.logical.EventType;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.logical.ServerInfo;
import org.steelthread.arcadian.domain.logical.ServerMap;
import org.steelthread.arcadian.domain.logical.ServerVar;
import org.steelthread.arcadian.domain.relational.Event;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.network.ServerUpdateException;
import org.steelthread.arcadian.service.CommandRunnerService;
import org.steelthread.arcadian.service.ConnectionService;
import org.steelthread.arcadian.service.ServerService;
import org.steelthread.arcadian.util.ArcadianConstants;
import org.steelthread.arcadian.util.ArcadianUtil;

/**
 * Loosely based on REST design patterns
 * http://architects.dzone.com/news/common-rest-design-pattern
 */
@Controller
@RequestMapping("/servers")
@Transactional
public class ServerController extends AbstractBaseController {

  private static final Logger logger = LoggerFactory.getLogger(ServerController.class);
  private static final String SPACE = " ";

  @Inject
  protected ServerDao serverDao;
  @Inject
  protected CommandRunnerService commandRunnerService;
  @Inject
  protected ServerService serverService;
  @Inject
  protected ConnectionService connectionService;
  @Inject
  protected ArcadianUtil arcadianUtil;
 
  /**
   * Page accessed with this URL:
   * http://localhost:8080/servers
   */
  @RequestMapping(method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public Set<org.steelthread.arcadian.domain.logical.Server> findAllServers(Principal principal) {
    logger.debug("finding all of the servers");
    /**
     * Example of casting to something more meaningful:
     * 
     * ((UsernamePasswordAuthenticationToken) principal).getName());
     * 
     */
    return serverService.findAllServers(principal.getName());
  }

  @RequestMapping(value="/action", method = RequestMethod.POST, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseStatus(HttpStatus.OK)
  public void runServerAction(@RequestBody Action action) {
    logger.debug("running server action:" + action.getAction());
    commandRunnerService.runUpdateCommand(CommandConstant.valueOf(action.getAction()), serverDao.get(action.getServerId()), action.getUpdateValues()); 
  }      

  @RequestMapping(value="/command", method = RequestMethod.POST, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public String runServerCommand(@RequestBody Action action) {
    logger.debug("running server action:" + action.getAction());
    List<String> response = commandRunnerService.runServerCommand(PermissionName.RUN_SERVER_COMMAND.getName(), action.getAction(), action.getUpdateValues(), serverDao.get(action.getServerId()));
    StringBuilder stringBuilder = new StringBuilder();
    for (String word : response) {
      stringBuilder.append(word);
      stringBuilder.append(SPACE);
    }
    return stringBuilder.toString();
  }      
  
  @RequestMapping(value="{id}", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public org.steelthread.arcadian.domain.logical.Server findServer(@PathVariable("id") Long id, Principal principal) {
    logger.debug("finding server:" + id);
    return serverService.findServer(id, principal.getName()); 
  }
  
  @RequestMapping(value="{id}", method = RequestMethod.PUT, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseStatus(HttpStatus.OK)
  public void updateServer(@RequestBody org.steelthread.arcadian.domain.logical.Server server) {
    logger.debug("updating server.");
    serverService.editServer(server);
  }  
  
  @RequestMapping(method = RequestMethod.POST, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public Server createServer(@RequestBody Server server, Principal principal) {
    serverService.createServer(server, principal.getName());
    logger.debug("created server:" + server.getId());
    return server;
  }
  
  @RequestMapping(value="{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void removeServer(@PathVariable("id") Long id, Principal principal) {
    logger.debug("removing server:" + id);
    serverService.removeServer(id, principal.getName());
  }
  
  @RequestMapping(value="{id}/maps", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<ServerMap> findAllMaps(@PathVariable("id") Long id) {
    logger.debug("finding maps for server:" + id);
    List<ServerMap> serverMaps = commandRunnerService.runListCommand(CommandConstant.MAPLIST, serverDao.get(id)); 
    return serverMaps;
  }
  
  @RequestMapping(value="{id}/maps/{index}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void removeMap(@PathVariable("id") Long id, @PathVariable("index") String index) {
    logger.debug("removing map from server:" + id + " with index:" + index);
    commandRunnerService.runUpdateCommand(CommandConstant.REMOVEMAP, serverDao.get(id), index);
    // persist the changes to file
    commandRunnerService.runUpdateCommand(CommandConstant.MAPLIST_SAVE,serverDao.get(id));    
  }      

  @RequestMapping(value="{id}/maps/{index}", method = RequestMethod.POST, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public ServerMap addMap(@RequestBody ServerMap serverMap) {
    List<String> args = new ArrayList<String>(3);
    args.add(serverMap.getName());
    args.add(serverMap.getGameMode());
    args.add(String.valueOf(serverMap.getNumberOfRounds()));
    // add the map to the server
    commandRunnerService.runUpdateCommand(CommandConstant.ADDMAP,serverDao.get(serverMap.getServerId()), args);
    // persist the map to file
    commandRunnerService.runUpdateCommand(CommandConstant.MAPLIST_SAVE,serverDao.get(serverMap.getServerId()));
    return serverMap;
  }
  
  @RequestMapping(value="{id}/maps/{index}/setNextMap", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void setNextMap(@PathVariable("id") Long id, @PathVariable("index") String index) {
    logger.debug("updating next map with server:" + id + " with index:" + index);
    commandRunnerService.runUpdateCommand(CommandConstant.NEXTMAP, serverDao.get(id), index); 
  }      
  
  @RequestMapping(value="{id}/info", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public ServerInfo findServerInfo(@PathVariable("id") Long id) {
    logger.debug("finding server info for server:" + id);
    ServerInfo serverInfo = commandRunnerService.runCommand(CommandConstant.SERVERINFO, serverDao.get(id)); 
    return serverInfo;
  }
  
  @RequestMapping(value="{id}/vars", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<ServerVar> findAllVars(@PathVariable("id") Long id) {
    logger.debug("finding vars for server:" + id);
    // for now just return all the vars. if we ever have more server types this could be looked up by server id
    return commandRunnerService.runListCommandType(CommandType.SERVER_VAR, serverDao.get(id)); 
  }
  
  @RequestMapping(value="/vars", method = RequestMethod.PUT, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseStatus(HttpStatus.OK)
  public void updateServerVar(@RequestBody ServerVar serverVar) {
    logger.debug("updating serverVar:" + serverVar.getCommand() + " with value " + serverVar.getValue());
    String response = commandRunnerService.runUpdateCommand(serverVar.getCommand(), serverDao.get(serverVar.getServerId()), serverVar.getValue());
    // if the response is not a success from the server than send an error
    if(!StringUtils.equals(ArcadianConstants.RESPONSE_OK, response)) {
      throw new ServerUpdateException("Updating serverVar:" + serverVar.getCommand() + " with value " + serverVar.getValue() + " failed with server message " + response);
    } else {
      // we do not return a value since Backbone only considers success with status of 200 and no content
      // http://stackoverflow.com/questions/7836574/how-to-get-a-good-response-from-saving-a-model      
    }
  }
  
  @RequestMapping(value="{id}/events", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<Event> findAllEvents(@PathVariable("id") Long id) {
    logger.debug("finding events for server:" + id);
    List<Event> events = connectionService.getCurrentEventConnection().getEventsUsingFilter();
    // show latest first
    Collections.reverse(events);
    return events;
  }

  @RequestMapping(value="{id}/events", method = RequestMethod.POST, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public void setEventFilters(@PathVariable("id") Long id, @RequestBody EventType[] eventTypes) {
    logger.debug("set event filter for:" + id);
    connectionService.getCurrentEventConnection().setEventFilter(eventTypes);
  }      

  @RequestMapping(value="{id}/admins", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public Set<Admin> findAllAdmins(@PathVariable("id") Long id) {
    return serverService.findAllAdmins(id);
  }

  @RequestMapping(value="{id}/admin", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public Admin findAdmin(@PathVariable("id") Long id, Principal principal) {
    return serverService.findAdmin(id, principal.getName());
  }

  @RequestMapping(value="{id}/admins", method = RequestMethod.POST, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public void addAdmin(@RequestBody Admin admin) {
    logger.debug("adding admin for server:" + admin.getServerId());
    serverService.addAdmin(admin);
  }

  @RequestMapping(value="{serverId}/admins/{username}/remove/{permissionName}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void removePermission(@PathVariable("serverId") Long serverId, @PathVariable("username")  String username, @PathVariable("permissionName")  PermissionName permissionName) {
    serverService.removeAdminPermission(serverId, username, permissionName);
  }

  @RequestMapping(value="{serverId}/admins/{username}/add/{permissionName}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void addPermission(@PathVariable("serverId") Long serverId, @PathVariable("username")  String username, @PathVariable("permissionName")  PermissionName permissionName) {
    serverService.addAdminPermission(serverId, username, permissionName);
  }
 
  @RequestMapping(value="{id}/admins/{username}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void removeAdmin(@PathVariable("id") Long id, @PathVariable("username")  String adminUsername) {
    logger.debug("removing admin " + adminUsername + " from server:" + id);
    serverService.removeAdmin(id, adminUsername);
  }
  
  @RequestMapping(value="{id}/seedServerConnection", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void seedServerConnection(@PathVariable("id") Long id) {
    connectionService.seedConnection(serverDao.get(id), arcadianUtil.getCurrentConnectionKey());
  }
  
  @RequestMapping(value="{id}/removeServerConnection", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void removeServerConnection(@PathVariable("id") Long id) {
    connectionService.removeCurrentConnection();
  }  
}
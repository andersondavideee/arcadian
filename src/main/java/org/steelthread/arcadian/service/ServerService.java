package org.steelthread.arcadian.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.steelthread.arcadian.dao.PermissionDao;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.dao.UserServerPermissionDao;
import org.steelthread.arcadian.domain.logical.Admin;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.domain.relational.UserServerPermission;
import org.steelthread.arcadian.network.ConnectionEntry;
import org.steelthread.arcadian.network.ConnectionObjectManager;
import org.steelthread.arcadian.util.ArcadianUtil;

@Service
public class ServerService {

  private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

  @Inject
  protected ServerDao serverDao;
  @Inject
  protected ConnectionService connectionService;
  @Inject
  protected UserDao userDao;  
  @Inject
  protected ConnectionObjectManager connectionObjectManager;
  @Inject
  protected DozerBeanMapper dozerBeanMapper;
  @Inject
  protected UserServerPermissionDao userServerPermissionDao;
  @Inject
  protected PermissionDao permissionDao;
  @Inject
  protected MailSender mailSender;
  @Inject
  protected ArcadianUtil arcadianUtil;

  @PreAuthorize("isAuthenticated() and hasPermission(#server.id, 'ADD_SERVER')")
  public void createServer(Server server, String username) {
    User user = userDao.findByUsername(username);
    // set this user as the owner so who know who can edit/delete
    server.setOwner(user);
    serverDao.create(server);
    user.addServer(server);
    userDao.update(user);
  }

  @PreAuthorize("isAuthenticated() and hasPermission(#editableServer.id, 'EDIT_SERVER')")
  public void editServer(org.steelthread.arcadian.domain.logical.Server editableServer) {
    Server server = serverDao.get(editableServer.getId());
    server.setHost(editableServer.getHost());
    server.setPort(editableServer.getPort());
    server.setName(editableServer.getName());
    // only change the password if one was set
    if(StringUtils.isNotBlank(editableServer.getPassword())) {
      server.setPassword(editableServer.getPassword());      
    }
    serverDao.update(server);
  }
  
  @PreAuthorize("isAuthenticated() and hasPermission(#id, 'REMOVE_SERVER')")
  public void removeServer(Long id, String username) {
    User user = userDao.findByUsername(username);    
    serverDao.remove(serverDao.get(id));
    user.removeServer(id);
    userDao.update(user);
  }
  
  public Set<org.steelthread.arcadian.domain.logical.Server> findAllServers(String username) {
    User user = userDao.findByUsername(username);
    Set<org.steelthread.arcadian.domain.logical.Server> mappedServers = new HashSet<org.steelthread.arcadian.domain.logical.Server>();
    Set<Server> servers = user.getServers();
    for (Server server : servers) {
      org.steelthread.arcadian.domain.logical.Server mappedServer = dozerBeanMapper.map(server, org.steelthread.arcadian.domain.logical.Server.class);
      decorateServer(mappedServer, user, server.getOwner().getId());
      mappedServers.add(mappedServer);
    }
    return mappedServers;
  }

  public org.steelthread.arcadian.domain.logical.Server findServer(Long serverId, String username) {
    User user = userDao.findByUsername(username);    
    Server server = serverDao.get(serverId);
    org.steelthread.arcadian.domain.logical.Server mappedServer = dozerBeanMapper.map(server, org.steelthread.arcadian.domain.logical.Server.class);
    decorateServer(mappedServer, user, server.getOwner().getId());
    return mappedServer;
  }
  
  protected void decorateServer(org.steelthread.arcadian.domain.logical.Server server, User user, Long ownerId) {
    // determine if we are connected and if we are the owner
    server.setActive(isServerActive(server.getId()));
    if(ownerId == user.getId()) {
      server.setCurrentOwner(true);
    }
  }

  @PreAuthorize("isAuthenticated() and hasPermission(#serverId, 'ADD_ADMIN_PERMISSION')")
  public void addAdminPermission(Long serverId, String username, PermissionName permissionName) {
    userServerPermissionDao.addPermissionForUser(userDao.findByUsername(username), serverDao.get(serverId), permissionDao.findByPermissionName(permissionName));    
  }

  @PreAuthorize("isAuthenticated() and hasPermission(#serverId, 'REMOVE_ADMIN_PERMISSION')")
  public void removeAdminPermission(Long serverId, String username, PermissionName permissionName) {
    userServerPermissionDao.removePermissionForUser(userDao.findByUsername(username), serverDao.get(serverId), permissionDao.findByPermissionName(permissionName));
  }
 
  @PreAuthorize("isAuthenticated() and hasPermission(#admin.serverId, 'ADD_ADMIN')")
  public void addAdmin(Admin admin) {
    // find the server to add the admin to
    Server server = serverDao.get(admin.getServerId());
    // the user we are adding
    User user;
    try {
      user = userDao.findByUsername(admin.getUsername());
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new EntityNotFoundException("username not found. please have the user sign-up.");
    }
    // create all of the server permissions for the admin
    for (PermissionName permissionName : PermissionName.getDefaultServerPermissions()) {
      userServerPermissionDao.create(new UserServerPermission(user, server, permissionDao.findByPermissionName(permissionName)));      
    }
    user.addServer(server);
    userDao.update(user);
    arcadianUtil.sendEmail(user.getEmail(), "Arcadian Server enabled for Administration", "Congratulations! The server " + server.getName() + " has been enabled for your administration.");
  }
  
  @PreAuthorize("isAuthenticated() and hasPermission(#serverId, 'REMOVE_ADMIN')")
  public void removeAdmin(Long serverId, String adminUsername) {
    // the user we are removing from
    User user = userDao.findByUsername(adminUsername);
    Server server = serverDao.get(serverId);
    user.removeServer(serverId);
    userDao.update(user);
    // remove all the UserServerPermissions
    userServerPermissionDao.removePermissionsForUser(user, serverDao.get(serverId));
    arcadianUtil.sendEmail(user.getEmail(), "Arcadian Server disabled for Administration", "Sorry. The server " + server.getName() + " has been disabled for your administration.");
  }

  @PreAuthorize("isAuthenticated() and hasPermission(#serverId, 'FIND_ADMINS')")
  public Set<Admin> findAllAdmins(Long serverId) {
    Set<Admin> admins = new HashSet<Admin>();
    Server server = serverDao.get(serverId);
    for (User user : server.getUsers()) {
      // do not include the server 'owner'
      if(!StringUtils.equals(server.getOwner().getUsername(), user.getUsername())) {
        admins.add(mapAdmin(user, server));
      }
    }
    return admins;
  }

  public Admin findAdmin(Long serverId, String username) {
    Server server = serverDao.get(serverId);
    User user = userDao.findByUsername(username);
    return mapAdmin(user, server);
  }

  protected Admin mapAdmin(User user, Server server) {
    Admin admin = dozerBeanMapper.map(user, Admin.class);
    admin.setServerId(server.getId());
    List<String> permissions = new ArrayList<String>();
    for (UserServerPermission userServerPermission : userServerPermissionDao.findAllServerPermissionsForUser(user, server)) {
      permissions.add(userServerPermission.getPermission().getName().getName());
    }
    admin.setPermissions(permissions);
    admin.setConnected(connectionObjectManager.isConnected(admin.getUsername()));
    return admin;
  }
  
  protected boolean isServerActive(Long serverId) {
    boolean isServerActive = false;
    ConnectionEntry connectionEntry = connectionObjectManager.getCurrentConnectionEntry();
    if(connectionEntry != null) {
      if(serverId == connectionEntry.getServer().getId()) {
        // now test the validity of the connection
        if(connectionEntry.getConnection().isConnectionValid()) {
          isServerActive = true;          
        } else {
          // if the connection is invalid removed from the pool
          connectionObjectManager.removeCurrentConnection();
        }
      }
    }
    return isServerActive;
  }
}
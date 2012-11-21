package org.steelthread.arcadian.security;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.command.CommandConstant;
import org.steelthread.arcadian.dao.PermissionDao;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.dao.UserServerPermissionDao;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.relational.Command;
import org.steelthread.arcadian.domain.relational.Permission;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.util.ArcadianConstants;

@Component
public class ServerCommandPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  protected ServerDao serverDao;
  @Autowired
  protected UserDao userDao;
  @Autowired
  protected PermissionDao permissionDao;
  @Autowired
  protected UserServerPermissionDao userServerPermissionDao;
  @Autowired
  protected ServerOwnerPermissionEvaluator serverOwnerPermissionEvaluator;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    AuthorityListTargetDomainObject authorityListTargetDomainObject = (AuthorityListTargetDomainObject) targetDomainObject;
    // retrieve stored values in list
    String command = (String) authorityListTargetDomainObject.get(0);
    Long serverId = (Long) authorityListTargetDomainObject.get(1);
    // normally we would rely on the Spring EL expresssions to handle (hasPermission OR hasPermission).
    // since we have short circuited that process in PermissionEvaluatorImpl (we throw an exception directly from there)
    // unfortunately we have to couple PermissionEvaluators

    // see if this user is the server owner - if they are no need to further evaluate
    if(serverOwnerPermissionEvaluator.hasPermission(authentication, serverId, permission)) {
      return true;
    }
    
    Server server = serverDao.get(serverId);
    User user = userDao.findByUsername(authentication.getName());
    // verify we have a mapping between the command and permission
    CommandConstant commandConstant = null;
    PermissionName commandPermissionName = null;
    try {
      commandConstant = CommandConstant.valueOf(command);
    } catch (IllegalArgumentException e) {
      // this is an edge-case and no logical mapping to a command. must be a standalone permission
    }
    if(commandConstant != null) {
      commandPermissionName = PermissionName.findPermissionNameForCommandConstant(commandConstant);      
    } else {
      // let's try one more match to see if this is truly a permission
      try {
        commandPermissionName = PermissionName.valueOf(command);
      } catch (IllegalArgumentException e) {
        // if there is no 'matching' PermissionName --> CommandConstant that means this command is not access-controlled
      }
    }
    if(commandPermissionName == null) {
      // if there is no 'matching' PermissionName --> CommandConstant that means this command is not access-controlled
      return true;      
    }
    Permission commandPermission = permissionDao.findByPermissionName(commandPermissionName);
    // verify that we can find the permission the user is trying to access
    try {
      userServerPermissionDao.findServerPermissionForUser(user, server, commandPermission);
    } catch (NoResultException e) {
      // we did not find the permission for this user
      return false;
    }
    // we found the permission and are good to go
    return true;
  }

  @Override
  public String getErrorMessage() {
    return ArcadianConstants.ACCESS_DENIED_MESSAGE_ADMIN_PERMISSION;
  }
}

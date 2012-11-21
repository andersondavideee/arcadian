package org.steelthread.arcadian.security;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.domain.logical.RoleName;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.util.ArcadianConstants;

@Component
public class AddServerPermissionEvaluator implements PermissionEvaluator {

  @Inject
  protected SecurityUtil securityUtil;
  @Inject
  protected ServerDao serverDao;
  @Inject
  protected UserDao userDao;
  
  private final int STANDARD_SERVER_CREATION = 2;
  
  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    // determine which account they have - then determine how many servers they can create versus how many they already have
    if(securityUtil.containsRole(RoleName.STANDARD_ACCOUNT, authentication)) {
      // count how many servers they currently have
      List<Server> servers = serverDao.getServersByOwner(userDao.findByUsername(authentication.getName()));
      if(servers.size() > STANDARD_SERVER_CREATION) {
        return false;
      } else {
        return true;        
      }
    // Premium accounts can add unlimited servers
    } else if (securityUtil.containsRole(RoleName.PREMIUM_ACCOUNT, authentication)) {
      return true;      
    } else {
      return false;
    }
  }

  @Override
  public String getErrorMessage() {
    return ArcadianConstants.ACCESS_DENIED_MESSAGE_UPGRADE_ACCOUNT + " Only " + STANDARD_SERVER_CREATION + " server allowed on a standard account.";
  }
}
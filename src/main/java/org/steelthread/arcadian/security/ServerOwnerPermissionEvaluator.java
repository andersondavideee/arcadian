package org.steelthread.arcadian.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.util.ArcadianConstants;

@Component
public class ServerOwnerPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  protected ServerDao serverDao;
  @Autowired
  protected UserDao userDao;
  
  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    Server server = serverDao.get((Long) targetDomainObject);
    User user = userDao.findByUsername(authentication.getName());
    if(server.getOwner().getId() == user.getId()) {
      return true;
    } else {
      return false;      
    }
  }

  @Override
  public String getErrorMessage() {
    return ArcadianConstants.ACCESS_DENIED_MESSAGE_SERVER_OWNER;
  }

}

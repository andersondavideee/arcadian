package org.steelthread.arcadian.user;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.domain.relational.Permission;
import org.steelthread.arcadian.domain.relational.Role;
import org.steelthread.arcadian.domain.relational.User;

public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * http://blog.solidcraft.eu/2011/03/spring-security-by-example-securing.html
   * http://springinpractice.com/2010/10/27/quick-tip-spring-security-role-based-authorization-and-permissions/
   * http://static.springsource.org/spring-security/site/docs/3.1.x/reference/el-access.html
   * http://stackoverflow.com/questions/6357579/spring-security-with-roles-and-permissions
   */
  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  protected UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.debug("username:" + username);
    User user;
    try {
      user = userDao.findByUsername(username);
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new UsernameNotFoundException("Username not found: " + username);
    }
    Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
    // retrieve the roles for the user
    Set<Role> roles = user.getRoles();
    for (Role role : roles) {
      // every role becomes a granted authority
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
      // roles have permissions
      Set<Permission> permissions = role.getPermissions();
      for (Permission permission : permissions) {
        // every permission becomes a granted authority
        grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().name()));
      }
    }
    return new SaltedUser(user.getUsername(), user.getPassword(), user.getSalt(), user.isEnabled(), true, true, true, grantedAuthorities);
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}
package org.steelthread.arcadian.dao;

import org.steelthread.arcadian.domain.relational.User;

public class UserDao extends GenericDao<User, Long> {

  public User findByUsername(String username) {
    User user = new User();
    user.setUsername(username);
    return queryByExampleFindOne(user);
  }  

  public User findByEmail(String email) {
    User user = new User();
    user.setEmail(email);
    return queryByExampleFindOne(user);
  }  

  public User findByUUID(String uuid) {
    User user = new User();
    user.setAuthenticationUUID(uuid);
    return queryByExampleFindOne(user);
  }  
}
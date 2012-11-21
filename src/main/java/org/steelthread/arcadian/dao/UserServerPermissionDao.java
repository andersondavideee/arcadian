package org.steelthread.arcadian.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.relational.Permission;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.domain.relational.UserServerPermission;

@Repository
public class UserServerPermissionDao extends GenericDao<UserServerPermission, Long> {

  public List<UserServerPermission> findAllServerPermissionsForUser(User user, Server server) {
    Query query = createNamedQuery("findAllServerPermissionsForUser");
    query.setParameter("user", user);
    query.setParameter("server", server);
    return query.getResultList();
  }

  public void removePermissionForUser(User user, Server server, Permission permission) {
    UserServerPermission foundUserServerPermission = findServerPermissionForUser(user, server, permission);
    // now that we found it remove it
    remove(foundUserServerPermission);
  }

  public UserServerPermission findServerPermissionForUser(User user, Server server, Permission permission) {
    Query query = createNamedQuery("findServerPermissionForUser");
    query.setParameter("user", user);
    query.setParameter("server", server);
    query.setParameter("permission", permission);
    return (UserServerPermission) query.getSingleResult();
  }

  public void removePermissionsForUser(User user, Server server) {
    List<UserServerPermission> userServerPermissions = findAllServerPermissionsForUser(user, server);
    for (UserServerPermission userServerPermission : userServerPermissions) {
      remove(userServerPermission);      
    }
  }
  
  public void addPermissionForUser(User user, Server server, Permission permission) {
    UserServerPermission userServerPermission = new UserServerPermission();
    userServerPermission.setUser(user);
    userServerPermission.setServer(server);
    userServerPermission.setPermission(permission);
    create(userServerPermission);
  }
}
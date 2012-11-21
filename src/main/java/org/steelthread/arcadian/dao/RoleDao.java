package org.steelthread.arcadian.dao;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.logical.RoleName;
import org.steelthread.arcadian.domain.relational.Role;

@Repository
public class RoleDao extends GenericDao<Role, Long> {

  public Role findByRoleName(RoleName roleName) {
    Role role = new Role();
    role.setName(roleName);
    return queryByExampleFindOne(role);
  }  
}

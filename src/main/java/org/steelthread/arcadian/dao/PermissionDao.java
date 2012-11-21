package org.steelthread.arcadian.dao;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.relational.Permission;

@Repository
public class PermissionDao extends GenericDao<Permission, Long> {

  public Permission findByPermissionName(PermissionName permissionName) {
    Permission permission = new Permission();
    permission.setName(permissionName);
    return queryByExampleFindOne(permission);
  }  
}
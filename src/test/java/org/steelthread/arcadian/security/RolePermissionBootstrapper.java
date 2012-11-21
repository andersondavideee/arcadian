package org.steelthread.arcadian.security;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.steelthread.arcadian.dao.PermissionDao;
import org.steelthread.arcadian.dao.RoleDao;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.logical.RoleName;
import org.steelthread.arcadian.domain.relational.Permission;
import org.steelthread.arcadian.domain.relational.Role;
import org.steelthread.arcadian.util.ArcadianConstants;

@ContextConfiguration(locations = { ArcadianConstants.APPLICATION_CONTEXT })
@TransactionConfiguration(defaultRollback = false)
public class RolePermissionBootstrapper extends AbstractTransactionalJUnit4SpringContextTests {
  

  @Inject
  private RoleDao roleDao;
  @Inject
  private PermissionDao permissionDao;
  
  /**
   * mvn test -Dtest=RolePermissionBootstrapper#createRoles
   */
  @Test
  public void createRoles() {
    for (RoleName roleName : RoleName.values()) {
      Role role = new Role();
      role.setName(roleName);
      roleDao.create(role);
    }
  }

  /**
   * mvn test -Dtest=RolePermissionBootstrapper#deletePermissions
   */
  @Test
  public void deletePermissions() {
    for (PermissionName permissionName : PermissionName.values()) {
      Permission permission = new Permission();
      permission.setName(permissionName);
      permissionDao.remove(permission);
    }
  }

  /**
   * mvn test -Dtest=RolePermissionBootstrapper#createPermissions
   */
  @Test
  public void createPermissions() {
    for (PermissionName permissionName : PermissionName.values()) {
      Permission permission = new Permission();
      permission.setName(permissionName);
      permissionDao.create(permission);
    }
  }

  /**
   * mvn test -Dtest=RolePermissionBootstrapper#addPermission
   */
  @Test
  public void addPermission() {
    Permission permission = new Permission();
    permission.setName(PermissionName.RUN_SERVER_COMMAND);
    permissionDao.create(permission);
  }

}

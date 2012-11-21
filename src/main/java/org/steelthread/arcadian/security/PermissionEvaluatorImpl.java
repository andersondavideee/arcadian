package org.steelthread.arcadian.security;

import java.io.Serializable;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.steelthread.arcadian.domain.logical.PermissionName;

public class PermissionEvaluatorImpl implements org.springframework.security.access.PermissionEvaluator {

  Map<PermissionName, PermissionEvaluator> permissionEvaluators;
  
  public PermissionEvaluatorImpl(Map<PermissionName, PermissionEvaluator> permissionEvaluators) {
    this.permissionEvaluators = permissionEvaluators;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    PermissionEvaluator permissionEvaluator = permissionEvaluators.get(PermissionName.valueOf((String) permission));
    boolean retVal = permissionEvaluator.hasPermission(authentication, targetDomainObject, permission);
    if(!retVal) {
      throw new AccessDeniedException(permissionEvaluator.getErrorMessage());
    }
    return retVal;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    return false;
  }
}
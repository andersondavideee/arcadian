package org.steelthread.arcadian.security;

import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.security.core.Authentication;

public interface PermissionEvaluator extends AopInfrastructureBean {

  /**
  *
  * @param authentication represents the user in question. Should not be null.
  * @param targetDomainObject the domain object for which permissions should be checked. May be null
  *          in which case implementations should return false, as the null condition can be checked explicitly
  *          in the expression.
  * @param permission a representation of the permission object as supplied by the expression system. Not null.
  * @return true if the permission is granted, false otherwise
  */
 boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission);
 
 String getErrorMessage();
}
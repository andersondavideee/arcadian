package org.steelthread.arcadian.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.logical.RoleName;

@Component
public class SecurityUtil {

  boolean containsRole(RoleName roleName, Authentication authentication) {
    Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
    if(grantedAuthorities.contains(new SimpleGrantedAuthority(roleName.name()))) {
      return true;
    } else {
      return false;
    }
  }
}

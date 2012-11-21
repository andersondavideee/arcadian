package org.steelthread.arcadian.user;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class RandomSaltSource implements SaltSource {

  @Override
  public Object getSalt(UserDetails userDetails) {
    SaltedUser saltedUser = (SaltedUser) userDetails;
    return saltedUser.getSalt();
  }
}

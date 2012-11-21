package org.steelthread.arcadian.security;

import java.util.ArrayList;

public class AuthorityListTargetDomainObject extends ArrayList<Object> {

  private static final long serialVersionUID = 6282721941722845803L;

  public AuthorityListTargetDomainObject(Object... objects) {
    super();
    for (Object object : objects) {
      super.add(object);
    }
  }
}
package org.steelthread.arcadian.user;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class SaltFactory {

  public String newSalt() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] saltBytes = new byte[32];
    secureRandom.nextBytes(saltBytes);
    Base64 base64 = new Base64( 76, new byte[]{});
    return base64.encodeToString(saltBytes);
  }
}

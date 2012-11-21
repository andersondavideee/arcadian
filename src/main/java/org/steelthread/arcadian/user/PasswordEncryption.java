package org.steelthread.arcadian.user;

import javax.inject.Inject;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.relational.User;

@Component
public class PasswordEncryption {

  @Inject
  protected SaltFactory saltFactory;
  @Inject
  protected ShaPasswordEncoder encoder;
  
  public void encryptPassword(User user) {
    String salt = saltFactory.newSalt();
    String encodedPassword = encoder.encodePassword( user.getPassword(), salt);
    user.setSalt(salt);
    user.setPassword(encodedPassword);    
  }

  public String encryptPassword(String password, String salt) {
    return encoder.encodePassword(password, salt);
  }  
}
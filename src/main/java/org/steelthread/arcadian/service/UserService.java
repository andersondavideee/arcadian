package org.steelthread.arcadian.service;

import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.steelthread.arcadian.dao.RoleDao;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.domain.logical.RoleName;
import org.steelthread.arcadian.domain.relational.Role;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.user.BadPasswordException;
import org.steelthread.arcadian.user.PasswordEncryption;
import org.steelthread.arcadian.util.ArcadianUtil;

@Component
public class UserService {

  @Inject
  protected UserDao userDao;  
  @Inject
  protected RoleDao roleDao;
  @Inject
  protected PasswordEncryption passwordEncryption;
  @Inject
  protected DaoAuthenticationProvider daoAuthenticationProvider;
  @Inject
  protected ArcadianUtil arcadianUtil;
  
  public String createUser(User user) throws EntityExistsException {
    try { 
      userDao.findByUsername(user.getUsername());
      throw new EntityExistsException();
    } catch (IncorrectResultSizeDataAccessException e) {
      // this is good. we should not find a user
    }
    try {
      userDao.findByEmail(user.getEmail());
      throw new EntityExistsException();
    } catch (IncorrectResultSizeDataAccessException e) {
      // this is good. we should not find a user
    }
    // an email will be sent with the corresponding UUID which will only enable the user after clicking on it
    user.setAuthenticationUUID(UUID.randomUUID().toString());
    passwordEncryption.encryptPassword(user); 
    user.setEnabled(false);
    // start the user with basic 'free' account role access
    Role role = roleDao.findByRoleName(RoleName.STANDARD_ACCOUNT);
    user.addRole(role);
    
    userDao.create(user);
    arcadianUtil.sendEmail(user.getEmail(), "Welcome to Arcadian!", "Click on this link to enable your account:" + " http://arcadian.herokuapp.com/users/enableUser/" + user.getAuthenticationUUID());
    // send email to me so I can track easily
    arcadianUtil.sendEmail("fgarsombke@yahoo.com", "New User Created!", "User " + user.getUsername() + " with email " + user.getEmail() + " has joined!");
    return "An email has been sent to the email provided. To enable your account click on the URL in the email";
  }
  
  public void updateUser(User user, org.steelthread.arcadian.domain.logical.User externalUser) {
    if(StringUtils.isNotBlank(externalUser.getEmail())) {
      user.setEmail(externalUser.getEmail());      
    }
    if(StringUtils.isNotBlank(externalUser.getOldPassword()) && StringUtils.isNotBlank(externalUser.getNewPassword()) && StringUtils.isNotBlank(externalUser.getConfirmNewPassword())) {
      if(!StringUtils.equals(externalUser.getNewPassword(), externalUser.getConfirmNewPassword())) {
        throw new BadPasswordException("Passwords do not match.");        
      }
      // try to load the user first to see if the incoming password was valid
      Authentication authentication = null;
      try {
        authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(externalUser.getUsername(), externalUser.getOldPassword()));
      } catch (BadCredentialsException e) {
        throw new BadPasswordException("Old Password does not match existing password.");
      }
      if(authentication.isAuthenticated()) {
        user.setPassword(externalUser.getNewPassword());
        passwordEncryption.encryptPassword(user);
      }
    }
    userDao.update(user);
  }
}

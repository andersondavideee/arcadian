package org.steelthread.arcadian.dao;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.util.ArcadianConstants;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = { ArcadianConstants.APPLICATION_CONTEXT })
@TransactionConfiguration(defaultRollback = true)
public class UserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Inject 
  private UserDao userDao;
  
  @Test
  public void test() {    
    User user = new User();
    user.setUsername("username");
    user.setAuthenticationUUID("authenticationUUID");
    user.setEmail("email");
    user.setEnabled(true);
    user.setPassword("password");
    user.setSalt("salt");
    userDao.create(user);
    userDao.flush();
    assertNotNull(userDao.get(user.getId()));
  }
}
package org.steelthread.arcadian.service;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.steelthread.arcadian.util.ArcadianConstants;

@ContextConfiguration(locations = { ArcadianConstants.APPLICATION_CONTEXT })
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailTest {

  @BeforeClass
  public static void beforeClass() {
    System.setProperty("email.password", "XXXXXXXXXX");    
  }
  
  @Inject
  protected MailSender mailSender;
  
  @Test
  public void testEmail() {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo("fgarsombke@yahoo.com");
    msg.setFrom("admin@steelthreadsoftware.com");
    msg.setText("Testing email!");
    msg.setSubject("Welcome to Arcadian!");
    mailSender.send(msg);  
  }
}
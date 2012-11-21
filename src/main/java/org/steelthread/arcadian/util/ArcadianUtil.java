package org.steelthread.arcadian.util;

import javax.inject.Inject;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class ArcadianUtil {

  @Inject
  protected MailSender mailSender;

  public void sendEmail(String toEmail, String subject, String text) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(toEmail);
    msg.setFrom("admin@steelthreadsoftware.com");
    msg.setSubject(subject);
    msg.setText(text);
    mailSender.send(msg);
  }

  public MultiKey getCurrentConnectionKey() {
    return new MultiKey(new Object[] {RequestContextHolder.currentRequestAttributes().getSessionId(), SecurityContextHolder.getContext().getAuthentication().getName()} );    
  }
}

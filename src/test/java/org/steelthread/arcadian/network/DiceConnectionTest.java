package org.steelthread.arcadian.network;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.steelthread.arcadian.domain.relational.Command;

import com.thoughtworks.xstream.XStream;

public class DiceConnectionTest {

  private static final Logger logger = LoggerFactory.getLogger(DiceConnectionTest.class);

  DiceConnectionImpl connection;

  @Before
  public void before() {
    connection = new DiceConnectionImpl(null);
    connection.connect("207.210.252.116", 47200);
    connection.authenticate("", "xega9usp");
    
  }
  
  @After
  public void after() {
  }
  
  @Test
  public void testCommands() throws Exception {
    // Get All the commands and insert
    XStream xStream = new XStream();
    ClassPathResource classPathResource = new ClassPathResource("Battlefield3Commands.xml");
    List<Command>commands = (List) xStream.fromXML(classPathResource.getInputStream());
    for (Command command : commands) {
      logger.debug("running command:" + command.getCommand());
      connection.send(command);
      List<String> response = connection.getDiceConnectionHelper().recv();
    }
  }

  @Test
  public void testServerVars() throws Exception {
    Command command = new Command();
    command.setCommand("vars.teamKillCountForKick");
    connection.send(command);
    List<String> response = connection.getDiceConnectionHelper().recv();
  }
  
  @Test
  public void testServerVarsUpdate() throws Exception {
    Command command = new Command();
    command.setCommand("vars.teamKillCountForKick");
    connection.sendUpdate(command, "10");
    List<String> response = connection.getDiceConnectionHelper().recv();
  }
  
  @Test
  public void testAdminSay() throws Exception {
    Command command = new Command();
    command.setCommand("admin.say");
    connection.sendUpdate(command, Arrays.asList(new String[] { "foobar!", "all" }));
    List<String> response = connection.getDiceConnectionHelper().recv();
  }   
}
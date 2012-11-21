package org.steelthread.arcadian.util;

public class ArcadianConstants {

  public static final String APPLICATION_CONTEXT = "/applicationContext.xml";
  public static final String APPLICATION_JSON = "application/json";
  public static final String RESPONSE_OK = "OK";
  public static final String ACCESS_DENIED_MESSAGE_UPGRADE_ACCOUNT = "Upgrade to a premium account for this functionality.";
  public static final String ACCESS_DENIED_MESSAGE_SERVER_OWNER = "Only the server owner is allowed that functionality.";
  public static final String ACCESS_DENIED_MESSAGE_ADMIN_PERMISSION = "As an admin you do not have the correct permission. Please notify the server owner for access to this permission.";
  public static final String HAS_PERMISSION_SERVER_COMMAND_ADMIN_COMMAND_CONSTANT = "isAuthenticated() and hasPermission(new org.steelthread.arcadian.security.AuthorityListTargetDomainObject(#command.commandConstant.name, #server.id), 'SERVER_COMMAND_ADMIN')";
  public static final String HAS_PERMISSION_SERVER_COMMAND_ADMIN_COMMAND_STRING = "isAuthenticated() and hasPermission(new org.steelthread.arcadian.security.AuthorityListTargetDomainObject(#command, #server.id), 'SERVER_COMMAND_ADMIN')";
}

package org.steelthread.arcadian.domain.logical;

import java.util.ArrayList;
import java.util.List;

import org.steelthread.arcadian.command.CommandConstant;


public enum PermissionName {

  ADD_SERVER("Add Server", PermissionType.ACCOUNT, null),
  EDIT_SERVER("Edit Server", PermissionType.ACCOUNT, null),
  FIND_ADMINS("Find Admins for Server", PermissionType.ACCOUNT, null),
  ADD_ADMIN("Add Admin", PermissionType.ACCOUNT, null),
  ADD_ADMIN_PERMISSION("Add Admin Permission", PermissionType.ACCOUNT, null),
  REMOVE_ADMIN("Remove Admin", PermissionType.ACCOUNT, null),
  REMOVE_ADMIN_PERMISSION("Remove Admin Permission", PermissionType.ACCOUNT, null),
  REMOVE_SERVER("Remove Server", PermissionType.ACCOUNT, null),
  SERVER_COMMAND_OWNER("Run server commands as server owner", PermissionType.ACCOUNT, null),
  SERVER_COMMAND_ADMIN("Run server commands as server admin", PermissionType.ACCOUNT, null),
  CHANGE_VAR("Change Server Vars", PermissionType.SERVER, new CommandConstant[] { 
      CommandConstant.GAMEPASSWORD,
      CommandConstant.AUTOBALANCE,
      CommandConstant.ROUNDSTARTPLAYERCOUNT,
      CommandConstant.ROUNDRESTARTPLAYERCOUNT,
      CommandConstant.FRIENDLYFIRE,
      CommandConstant.MAXPLAYERS,
      CommandConstant.BANNERURL,
      CommandConstant.SERVERDESCRIPTION,
      CommandConstant.KILLCAM,
      CommandConstant.MINIMAP,
      CommandConstant.HUD,
      CommandConstant.CROSSHAIR,
      CommandConstant.THREEDSPOTTING,
      CommandConstant.MINIMAPSPOTTING,
      CommandConstant.NAMETAG,
      CommandConstant.THREEPCAM,
      CommandConstant.REGENERATEHEALTH,
      CommandConstant.TEAMKILLCOUNTFORKICK,
      CommandConstant.TEAMKILLVALUEFORKICK,
      CommandConstant.TEAMKILLVALUEINCREASE,
      CommandConstant.TEAMKILLVALUEDECREASEPERSECOND,
      CommandConstant.TEAMKILLVALUEKICKFORBAN,
      CommandConstant.IDLETIMEOUT,
      CommandConstant.IDLEBANROUNDS,
      CommandConstant.VEHICLESPAWNALLOWED,
      CommandConstant.VEHICLESPAWNDELAY,
      CommandConstant.SOLDIERHEALTH,
      CommandConstant.PLAYERRESPAWNTIME,
      CommandConstant.PLAYERMANDOWNTIME,
      CommandConstant.BULLETDAMAGE,
      CommandConstant.ONLYSQUADLEADERSPAWN,
  }),
  KICKPLAYER("Kick Player", PermissionType.SERVER, new CommandConstant[] {CommandConstant.KICKPLAYER}),
  MOVEPLAYER("Move Player", PermissionType.SERVER, new CommandConstant[] {CommandConstant.MOVEPLAYER}),
  NEXTMAP("Next Map", PermissionType.SERVER, new CommandConstant[] {CommandConstant.NEXTMAP}),
  ADDMAP("Add Map", PermissionType.SERVER, new CommandConstant[] {CommandConstant.ADDMAP}),
  REMOVEMAP("Remove Map", PermissionType.SERVER, new CommandConstant[] {CommandConstant.REMOVEMAP}),
  RUNNEXTROUND("Run Next Round", PermissionType.SERVER, new CommandConstant[] {CommandConstant.RUNNEXTROUND}),
  RESTARTROUND("Restart Round", PermissionType.SERVER, new CommandConstant[] {CommandConstant.RESTARTROUND}),
  BAN("Ban Player", PermissionType.SERVER, new CommandConstant[] {CommandConstant.BAN}),
  SAY("Say (chat) on server", PermissionType.SERVER, new CommandConstant[] {CommandConstant.SAY}),
  YELL("Yell (chat) on server", PermissionType.SERVER, new CommandConstant[] {CommandConstant.YELL}),
  RUN_SERVER_COMMAND("Run commands on the server (this could potentially bypass all the other permissions)", PermissionType.SERVER, null);
  
  private String description;
  private PermissionType permissionType;
  private CommandConstant[] commandConstants;

  private PermissionName(String description, PermissionType permissionType, CommandConstant[] commandConstants) {
    this.description = description;
    this.permissionType = permissionType;
    this.commandConstants = commandConstants;
  }
     
  public CommandConstant[] getCommandConstants() {
    return commandConstants;
  }

  public void setCommandConstants(CommandConstant[] commandConstants) {
    this.commandConstants = commandConstants;
  }


  public PermissionType getPermissionType() {
    return permissionType;
  }

  public String getDescription() {
    return description;
  }
  
  public String getName() {
    return name();
  }
  
  public static List<PermissionName> getServerPermissions() {
    List<PermissionName> permissionNames = new ArrayList<PermissionName>();
    for (PermissionName permission : values()) {
      if(PermissionType.SERVER.equals(permission.getPermissionType())) {
        permissionNames.add(permission);        
      }
    }
    return permissionNames;
  }

  public static List<PermissionName> getDefaultServerPermissions() {
    List<PermissionName> permissionNames = getServerPermissions();
    permissionNames.remove(PermissionName.RUN_SERVER_COMMAND);
    return permissionNames;
  }

  public static PermissionName findPermissionNameForCommandConstant(CommandConstant commandConstant) {
    for (PermissionName permission : values()) {
      CommandConstant[] commandConstants = permission.getCommandConstants();
      if(commandConstants != null) {
        for (CommandConstant command : commandConstants) {
          if(command.equals(commandConstant)) {
            return permission;
          }
        }
      }
    }
    return null;
  }
}
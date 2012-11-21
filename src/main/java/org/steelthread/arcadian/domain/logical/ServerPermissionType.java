package org.steelthread.arcadian.domain.logical;

import org.steelthread.arcadian.command.CommandConstant;

public enum ServerPermissionType {

  BAN("Ban Player", CommandConstant.BAN),
  MOVEPLAYER("Move Player", CommandConstant.MOVEPLAYER),
  KICKPLAYER("Kick Player", CommandConstant.KICKPLAYER),
  NEXTMAP("Next Map", CommandConstant.NEXTMAP),
  ADDMAP("ADD Map", CommandConstant.ADDMAP),
  REMOVEMAP("Remove Map", CommandConstant.REMOVEMAP),
  RUNNEXTROUND("Run Next Round", CommandConstant.RUNNEXTROUND),
  RESTARTROUND("Run Next Round", CommandConstant.RESTARTROUND);
  
  private String description;
  private CommandConstant commandConstant;

  private ServerPermissionType(String description, CommandConstant commandConstant) {
    this.description = description;
    this.commandConstant = commandConstant;
  }
  
  public CommandConstant getCommandConstant() {
    return commandConstant;
  }

  public String getDescription() {
    return description;
  }

}

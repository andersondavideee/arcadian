package org.steelthread.arcadian.domain.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import org.steelthread.arcadian.domain.logical.ServerVar;
import org.steelthread.arcadian.domain.relational.Command;

@Component
public class ServerVarMapper implements Mapper<ServerVar> {
  /**
   * <value: string>
   * REQUEST: vars.XXX
   * RESPONSE:
   * 4, OK, false
   */ 
  public ServerVar map(List<String> data, Command command, Long serverId) {
    ServerVar serverVar = new ServerVar(serverId);
    String[] strings = data.toArray(new String[0]);
    // check to make sure we received a return value from the server
    if(strings.length == 3) {
      serverVar.setValue(strings[2]);      
    }
    serverVar.setCommand(command.getCommand());
    serverVar.setDescription(command.getDescription());
    serverVar.setDatatype(command.getDatatype());
    return serverVar;
  }
}
package org.steelthread.arcadian.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.steelthread.arcadian.domain.relational.Server;
import org.steelthread.arcadian.domain.relational.User;

@Repository
public class ServerDao extends GenericDao<Server, Long> {

  public List<Server> getServersByOwner(User owner) {
    Server server = new Server();
    server.setOwner(owner);
    return queryByExample(server);
  }
}

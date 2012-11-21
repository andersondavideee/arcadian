package org.steelthread.arcadian.domain.logical;

import java.util.UUID;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class AbstractLogicalObject {

  public AbstractLogicalObject(Long serverId) {
    this.serverId = serverId;
    // id has to be unique for backbone collection        
    this.id = UUID.randomUUID().toString();
  }

  /**
   * You must have a no-arg constructor if using this as a JSON object being passed back from the client.
   * In order to be marshalled into a Java object you need a no-arg constructor.
   */
  public AbstractLogicalObject() {
  }
  
  protected String id;

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  protected Long serverId;

  public Long getServerId() {
    return serverId;
  }

  public void setServerId(Long serverId) {
    this.serverId = serverId;
  }
  
  @Override
  public String toString() {
    return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}

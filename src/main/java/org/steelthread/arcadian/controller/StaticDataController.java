package org.steelthread.arcadian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.steelthread.arcadian.dao.MapDao;
import org.steelthread.arcadian.dao.ServerDao;
import org.steelthread.arcadian.domain.logical.Permission;
import org.steelthread.arcadian.domain.logical.PermissionName;
import org.steelthread.arcadian.domain.relational.Map;
import org.steelthread.arcadian.util.ArcadianConstants;

@Controller
@RequestMapping("/data")
@Transactional
public class StaticDataController extends AbstractBaseController {

  private static final Logger logger = LoggerFactory.getLogger(StaticDataController.class);
  
  @Inject
  protected MapDao mapDao;
  @Inject
  protected ServerDao serverDao;
  
  @RequestMapping(value="{id}/maps", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<Map> findAllMaps(@PathVariable("id") Long id) {
    List<Map> maps = mapDao.findByServerType(serverDao.get(id).getServerType()); 
    return maps;
  }
  
  @RequestMapping(value="{id}/permissions", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public List<Permission> findAllServerPermissions(@PathVariable("id") Long id) {
    List<Permission> permissions = new ArrayList<Permission>();
    for (PermissionName permissionName : PermissionName.getServerPermissions()) {
      Permission permission = new Permission(id);
      permission.setName(permissionName.getName());
      permission.setDescription(permissionName.getDescription());
      permissions.add(permission);
    }
    return permissions;
  }  
}
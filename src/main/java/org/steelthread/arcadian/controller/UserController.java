package org.steelthread.arcadian.controller;

import java.security.Principal;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.steelthread.arcadian.dao.UserDao;
import org.steelthread.arcadian.domain.relational.User;
import org.steelthread.arcadian.service.UserService;
import org.steelthread.arcadian.util.ArcadianConstants;

@Controller
@RequestMapping("/users")
@Transactional
public class UserController extends AbstractBaseController  {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  
  @Inject
  protected UserDao userDao;  
  @Inject
  protected UserService userService;  
  @Inject
  protected DozerBeanMapper dozerBeanMapper;
  
  @RequestMapping(value="user", method = RequestMethod.GET, produces = ArcadianConstants.APPLICATION_JSON)
  @ResponseBody
  public org.steelthread.arcadian.domain.logical.User findAuthenticatedUser(Principal principal) {
    String username = principal.getName();
    User user = userDao.findByUsername(username);
    // map to an external user object that has far less data than the internal representation
    return dozerBeanMapper.map(user, org.steelthread.arcadian.domain.logical.User.class);
  }
  
  @RequestMapping(value="user", method = RequestMethod.PUT, produces = ArcadianConstants.APPLICATION_JSON, consumes = ArcadianConstants.APPLICATION_JSON)
  @ResponseStatus(HttpStatus.OK)
  public void updateServerVar(@RequestBody org.steelthread.arcadian.domain.logical.User editableUser, Principal principal) {
    logger.debug("updating user.");  
    userService.updateUser(userDao.findByUsername(principal.getName()), editableUser);
  }
  
  @RequestMapping(value="create", method = RequestMethod.POST)
  @ResponseBody
  public ModelAndView createUser(@ModelAttribute User user) {
    logger.debug("creating user:" + user.getUsername());
    String message = null;
    boolean statusSuccess = true;
    try {
      message = userService.createUser(user);
    } catch (EntityExistsException e) {
      message = "Username or email already taken.";
      statusSuccess = false;
    }
    ModelAndView mav = new ModelAndView();
    mav.setViewName("redirect:/signin.jsp");
    // there is no way to pass objects in a redirect
    mav.addObject("statusMsg", message);
    mav.addObject("statusSuccess", statusSuccess);
    return mav;
  }
  
  @RequestMapping(value="enableUser/{uuid}", method = RequestMethod.GET)
  @ResponseBody
  public ModelAndView enableUser(@PathVariable("uuid") String uuid) {
    User user = userDao.findByUUID(uuid);
    user.setEnabled(true);
    userDao.update(user);
    logger.debug("user:" + user.getUsername() + " enabled.");
    ModelAndView mav = new ModelAndView();
    mav.setViewName("redirect:/signin.jsp");
    // there is no way to pass objects in a redirect
    mav.addObject("statusMsg", "Welcome to Arcadian, Where dreams come true!");
    mav.addObject("statusSuccess", true);
    return mav;
  }
}
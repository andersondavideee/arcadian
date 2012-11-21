package org.steelthread.arcadian.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.steelthread.arcadian.network.ConnectionException;
import org.steelthread.arcadian.network.ServerUpdateException;
import org.steelthread.arcadian.user.BadPasswordException;

public abstract class AbstractBaseController {

  private static final Logger logger = LoggerFactory.getLogger(AbstractBaseController.class);

  /**
   * example JSON returned
   * response:{"readyState":4,"responseText":"{\"error\":\"Testing!!!!\"}","status":500,"statusText":"Server Error"}
   * 
   * example usage in Backbone:
   * 
       this.collection.fetch( {
          error: function(model, response){
            // convert the responseText to a JSON object
            var responseJson = eval('(' + response.responseText + ')');
            // show the text associated with the error object
            console.log('msg:' + responseJson.error);
            // re-direct somewhere
          }}
        );
     
     Coffeescript example:
     
    refreshData: ->
      @collection.fetch error:(model, response)->
        ## convert the responseText to a JSON object
        responseJson = eval '(' + response.responseText + ')'
        ## show the text associated with the error object
        console.log 'msg:' + responseJson.error
        ## redirect somewhere
      @     
     
     Currently these errors are handled in arcadian-error-handling.coffee
   */

  @ExceptionHandler (ServerUpdateException.class)
  @ResponseStatus (HttpStatus.METHOD_FAILURE)
  public ModelAndView handleServerUpdateException(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView(ex.getMessage());
  } 

  @ExceptionHandler (AccessDeniedException.class)
  @ResponseStatus (HttpStatus.METHOD_FAILURE)
  public ModelAndView handleAccessDeniedException(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView(ex.getMessage());
  } 

  @ExceptionHandler (BadPasswordException.class)
  @ResponseStatus (HttpStatus.METHOD_FAILURE)
  public ModelAndView handleBadPasswordException(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView(ex.getMessage());
  } 

  @ExceptionHandler (EntityNotFoundException.class)
  @ResponseStatus (HttpStatus.METHOD_FAILURE)
  public ModelAndView handleEntityNotFoundException(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView(ex.getMessage());
  } 

  @ExceptionHandler (ConnectionException.class)
  @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView handleConnectionException(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView("Connection refused to server");
  } 

  @ExceptionHandler (Throwable.class)
  @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView handleAllExceptions(Exception ex) {
    logger.warn("Handling exception:", ex);
    return asModelAndView(ex.getMessage());
  } 

  protected ModelAndView asModelAndView(String message) {
    MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
    Map<String, String> map = new HashMap<String, String>();
    map.put("error", message);
    return new ModelAndView(jsonView, map);
  }
}
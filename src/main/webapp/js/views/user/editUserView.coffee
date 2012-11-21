define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  userModel = require 'cs!models/userModel'
  jqueryValidationEngine = require 'jqueryValidationEngine'
  jqueryValidationEngineTranslate = require 'jqueryValidationEngineTranslate'  
  editUserTemplate = require 'text!templates/user/editUserTemplate.html'  
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  
  
  class editUserView extends Backbone.View
  
    el: $("#page")
  
    init: (options)->
      @userModel = new userModel()
      @userModel.bind 'change', @render, @
      @refreshData()
      @
    
    events:
      "click .saveProfile": "saveProfile" 
      "click .changePassword": "changePassword" 
      
    refreshData: ->
      @userModel.fetch()
      
    saveProfile: ->
      @userModel.save { email:$("#email").val() }, success: (model, response) ->
        arcadianUtil.renderSuccessAlertMessage 'User Update Success'
      return false

    changePassword: ->
      @userModel.save { oldPassword:$("#oldPassword").val(), newPassword:$("#newPassword").val(), confirmNewPassword:$("#confirmNewPassword").val() }, success: (model, response) =>
        arcadianUtil.renderSuccessAlertMessage 'Password Change Success'
      return false
      
    render: ->
      data = 
          user : @userModel,
          _: _  
      
      compiledTemplate = _.template editUserTemplate, data
      @$el.html compiledTemplate
    
    clear: ->
      @$el.empty()
      
  return editUserView
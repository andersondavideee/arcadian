define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverModel = require 'cs!models/serverModel'
  jqueryValidationEngine = require 'jqueryValidationEngine'
  jqueryValidationEngineTranslate = require 'jqueryValidationEngineTranslate'  
  editServerTemplate = require 'text!templates/server/editServerTemplate.html'  
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  
  
  class editServerView extends Backbone.View
  
    el: $("#page")
  
    init: (options)->
      @serverModel = new serverModel()
      @serverModel.id = options.serverId
      @serverModel.bind 'change', @render, @
      @refreshData()
      @
    
    events:
      "click .saveServer": "saveServer" 
      
    refreshData: ->
      ## use the server id to help the GET retrieve the server
      @serverModel.url = 'servers/' + @serverModel.id
      @serverModel.fetch()
      
    saveServer: ->
      @serverModel.save { name:$("#serverName").val(), host:$("#serverHost").val(), port:$("#serverPort").val(), password:$("#serverPassword").val() }, success: (model, response) ->
        arcadianUtil.renderSuccessAlertMessage 'Server Update Success'
        ## redirect to home page after success
        $(location).attr('href','/arcadian.html#')
        return false
      @

    render: ->
      data = 
          server : @serverModel,
          _: _  
      
      compiledTemplate = _.template editServerTemplate, data
      @$el.html compiledTemplate
    
    clear: ->
      @$el.empty()
      
  return editServerView
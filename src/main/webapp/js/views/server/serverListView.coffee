define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  jqueryValidationEngine = require 'jqueryValidationEngine'
  jqueryValidationEngineTranslate = require 'jqueryValidationEngineTranslate'
  serverCollection = require 'cs!collections/serverCollection'
  serverModel = require 'cs!models/serverModel'    
  serverListTemplate = require 'text!templates/server/serverListTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'

  class serverListView extends Backbone.View

    el: $("#page")

    init: (options)->
      @collection = new serverCollection()
      @collection.bind 'reset', @render, @      
      ## render the page after any changes in the collection - refresh the page to reflect the server that was removed
      @collection.bind 'remove', @render, @
      @refreshData()
      @
 
    events:
      ## .addServer corresponds to anything defined with the class 'addServer' (i.e. <button class="btn-mini btn-danger addServer">)
      "click .addServer": "addServer" 
      "click .connect": "connect" 
      "click .disconnect": "disconnect" 

    connect: (event) ->
      ## retrieve the id from clicked icon
      serverId = $(event.currentTarget).attr 'id'
      $.ajax
        url: 'servers/' + serverId + '/seedServerConnection'
        success: (data, textStatus, jqXHR) =>
          ## now that we have successfully seeded our connection let the games begin
          ## refresh the server list and it will reflect that we are connected to the server
          @refreshData()
          @

    disconnect: (event) ->
      ## retrieve the id from clicked icon
      serverId = $(event.currentTarget).attr 'id'
      $.ajax
        url: 'servers/' + serverId + '/removeServerConnection'
        success: (data, textStatus, jqXHR) =>
          ## now that we have successfully seeded our connection let the games begin
          ## refresh the server list and it will reflect that we are connected to the server
          @refreshData()
          @
          
    refreshData: ->
      @collection.fetch()
      @
        
    addServer: (model)->
      ## Create a new server model instance and set the fields from the form
      newServerModel = new serverModel()
      newServerModel.set "name", $("#serverName").val()
      newServerModel.set "port", $("#serverPort").val()
      newServerModel.set "password", $("#serverPassword").val()
      newServerModel.set "host", $("#serverHost").val()
      newServerModel.set "serverType", $("#serverTypeSelect").val()     
      
      ## Fix this defect - should be able to not have a URL on the model since the collection keeps track
      ## Should not have to override like this
      newServerModel.url = 'servers'
      
      ## persist the model
      @collection.create newServerModel, success: (model, response) =>
        arcadianUtil.renderSuccessAlertMessage 'Server added success'
        @refreshData()
        error: (model, response) ->        
          console.log 'ERROR' + response
      return false
      
    removeServer: (id) ->
      ## remove the model from the collection
      @collection.remove(modelToDestroy);
      ## destroy the model
      modelToDestroy = @collection.get id
      modelToDestroy.url = 'servers/' + id
      modelToDestroy.destroy()
      ## this seems to help with the delete action firing twice?
      return false
    
    render: ->
      sortedByNameServers = _.sortBy @collection.models, (model) ->
        model.get 'name'
      
      data = 
        servers : sortedByNameServers,
        _: _  
      
      compiledTemplate = _.template serverListTemplate, data
      @$el.html compiledTemplate      
      @
    
    clear: ->
      ## clear out any intervals and el information
      clearInterval(@intervalId)
      @$el.empty()
  
  return serverListView
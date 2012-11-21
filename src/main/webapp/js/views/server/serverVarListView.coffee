define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverVarCollection = require 'cs!collections/serverVarCollection'
  arcadianUtil = require 'cs!arcadianUtil'
  serverVarListTemplate = require 'text!templates/server/serverVarListTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  

  class serverVarListView extends Backbone.View

    el: $("#page")

    serverId:null,
    
    init: (options)->
      @varCollection = new serverVarCollection()
      ## collection.fetch() is asynchronous - once the fetch is done it will callback to the render method
      @varCollection.bind 'reset', @render, @
      @varCollection.serverId = options.serverId
      ## passed into the constructor
      @serverId = options.serverId
      @refreshData()
      @

    refreshData: ->
      @varCollection.fetch()

    events:
      "click .editServerVar": "editServerVar"          

    editServerVar: (event) ->
      ## retrieve the command name from clicked icon
      id = $(event.currentTarget).attr 'id'
      ## now get the value that we need to update the model with - this is the input text using a combo for the id of the input text
      idName = '#' + id + 'UpdateValue'
      valueToUpdateWith = $(idName).val()
      ## retrieve the model to update
      modelToUpdate = @varCollection.get id
      ## if there is a validation error display on console
      modelToUpdate.on 'error', (model, error) ->
        arcadianUtil.renderFailureAlertMessage error
   	  ## allow backbone to sync the model - display success message when done
   	  modelToUpdate.save 'value', valueToUpdateWith, success: (model, response) ->
        arcadianUtil.renderSuccessAlertMessage ((model.get 'command') + ' updated to ' + (model.get 'value'))
      error: (model, response) =>
        ## since we failed in the update refresh the page to re-load correct data
        @refreshData()
        return false
      @

    render: ->
      sortedByNameVars = _.sortBy @varCollection.models, (model) ->
        model.get 'command'
      
      data = 
        serverVars : sortedByNameVars,
        _: _  
      
      compiledTemplate = _.template serverVarListTemplate, data
      @$el.html compiledTemplate      
      @

    clear: ->
      ## clear el information
      @$el.empty()
 
  return serverVarListView
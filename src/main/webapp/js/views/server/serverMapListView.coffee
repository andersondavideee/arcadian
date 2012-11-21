define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverMapCollection = require 'cs!collections/serverMapCollection'
  serverMapListTemplate = require 'text!templates/server/serverMapListTemplate.html'
  mapCollection = require 'cs!collections/mapCollection'
  serverMapModel = require 'cs!models/serverMapModel'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'

  ## example of extending a different class that extends a backbone class
  class serverMapListView extends ArcadianView
  
    el: $("#page")

    serverId:null
      
    init: (options) ->
      @serverMapCollection = new serverMapCollection()
      @serverMapCollection.serverId = options.serverId 
      ## collection.fetch() is asynchronous - once the fetch is done it will callback to the render method
      @serverMapCollection.bind 'reset', @render, @
      @serverMapCollection.bind 'remove', @render, @      
      ## static list of maps that @ server supports
      @mapCollection = new mapCollection()
      @mapCollection.serverId = options.serverId 
      ## passed into the constructor
      @serverId = options.serverId
      @refreshData()
      @

    refreshData: ->
      @serverMapCollection.fetch()
      ## get the static maps for display
      @mapCollection.fetch()
      return false

    events:      
      "click .addMap": "addMap" 
      "click .setNextMap": "setNextMap"      
      "click .runNextRound": "runNextRound"      
      "click .restartRound": "restartRound"      
      "change #mapNameSelect" : "changeMapOptions" 

    addMap: ->
      mapId = $('#mapNameSelect').val()
      ## if empty return error message
      if mapId is ''
        arcadianUtil.renderFailureAlertMessage 'No map selected'
        return false
        
      selectedMap = @mapCollection.get mapId
      ## create a new server map from this information
      newServerMapModel = new serverMapModel()
      newServerMapModel.set "name", selectedMap.get 'name'
      newServerMapModel.set "gameMode",  $('#mapModeSelect').val()
      newServerMapModel.set "externalName", selectedMap.get 'externalName'
      newServerMapModel.set "numberOfRounds", $('#mapRoundsSelect').val()
      ## put the map at the bottom of the list
      index = (@serverMapCollection.length) + 1
      newServerMapModel.set "index", index
      newServerMapModel.set "serverId", @serverId
      ## add the server to the collection - @ will trigger add event on the serverMapListView
      @serverMapCollection.create newServerMapModel, success: (model, response) =>
        ## notice the 'fat' arrow (=>) - allows us to reference 'this' scoped items in this view
        ## we need to refresh the view since we depend on having IDs that are used in the template
        ## when a model is created we do not have an ID until the map has successfully added it to the database
        ## when a user would try to delete the map there would be no ID associated with it (unless the page was refreshed)
        arcadianUtil.renderSuccessAlertMessage 'map ' + (model.get 'externalName') + ' add to server'
        @refreshData()
        return false
      return false

    removeMap: (id) ->
      modelToDestroy = @serverMapCollection.get id
      modelToDestroy.destroy success: (model, response) ->
        arcadianUtil.renderSuccessAlertMessage 'map ' + (model.get 'externalName') + ' removed from server'
      ## remove the model from the collection after destroying it
      @serverMapCollection.remove modelToDestroy
      return false;
    
    changeMapOptions: ->
      mapId = $('#mapNameSelect').val()
      ## if empty just return and clear the select
      if mapId is ''
        ## clear out the gamemode select box
        $('#mapModeSelect').empty()
        return true;
      ## retrieve the map that is bound with gamemodes
      selectedMap = @mapCollection.get mapId
      ## append the gamemodes to the dropdown
      gameModes = selectedMap.get 'gamemodes'
      
      ## sort the gamemodes
      sortedByNameGamemodes = _.sortBy gameModes, (gamemode) ->
        gamemode.externalName

        ## clear out the existing map modes
      $('#mapModeSelect').empty()
      ## load the new ones
      _.each sortedByNameGamemodes, (gamemode) ->
        $('#mapModeSelect').append '<option value=' + gamemode.name + '>' + gamemode.externalName + '</option>'
      return false

    setNextMap: (event) ->
      id = $(event.currentTarget).attr 'id'
      modelToSetNextMap = @serverMapCollection.get id
      nextMapUrl = modelToSetNextMap.url() + '/setNextMap'
      ## use ajax to setNextMap on the server
      $.ajax
        url: nextMapUrl
        success: (data, textStatus, jqXHR) ->
          arcadianUtil.renderSuccessAlertMessage 'map ' + (modelToSetNextMap.get 'externalName') + ' with game mode ' + (modelToSetNextMap.get 'gameMode') + ' set for next map'
      return false

    runNextRound: (event) ->
      @runAction event, 'RUNNEXTROUND'
      return false

    restartRound: (event) ->
      @runAction event, 'RESTARTROUND'
      return false

    runAction: (event, actionName) ->
      id = $(event.currentTarget).attr 'id'
      ## use ajax to runNextRound
      action = JSON.stringify( {action: actionName, serverId: id } )
      $.ajax
        type: 'POST',
        url: 'servers/action'
        contentType: "application/json"
        data: action
        dataType: "json"
        success: (data, textStatus, jqXHR) ->
          arcadianUtil.renderSuccessAlertMessage 'action success'
      return false
       
    ## pass server data from the 'fetch' into the serverMapListTemplate which uses jQuery to iterate through the model objects
    render: ->
      sortedByNameMaps = _.sortBy @mapCollection.models, (model) ->
        model.get 'externalName'
        
      data =
        serverMaps : @serverMapCollection.models
        _: _
        serverId : @serverId
        maps : sortedByNameMaps

      compiledTemplate = _.template serverMapListTemplate, data
      @$el.html compiledTemplate
      @

    clear: ->
      ## clear el information
      @$el.empty()

  return serverMapListView
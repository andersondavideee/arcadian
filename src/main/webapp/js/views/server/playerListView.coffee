define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  jqueryUI = require 'jqueryUI'
  serverInfoModel = require 'cs!models/serverInfoModel'
  playerCollection = require 'cs!collections/playerCollection'
  playerListTemplate = require 'text!templates/server/playerListTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  
  datatables = require 'datatables'  
  poller = require 'backbone-poller'  

  class playerListView extends Backbone.View
  
    el: $("#page")
  
    serverId:null
    poller:null
    
    initialize: ->
      @collection = new playerCollection()
      @collection.bind 'reset', @render, @      
          
    init: (options)->
      @serverInfoModel = new serverInfoModel { serverId : options.serverId }
      @collection.serverId = options.serverId       
      @serverId = options.serverId
      _.bindAll(@)
      @refreshData()
      @
  
    events: 
      "click .kickPlayer": "kickPlayer" 
      "click .movePlayer": "movePlayer" 
      "click #banPlayer": "banPlayer" 

    banPlayer: (event) ->
      action = 'BAN'
      banType = $('#banType').val()
      banValue = $('#banValue').val()
      banPlayerId = $('#banPlayerId').val()
      modelToBan = @collection.get banPlayerId
      ## this can happen if the player drops from the server, etc.
      if(_.isUndefined(modelToBan))
        arcadianUtil.renderFailureAlertMessage 'Player banned failure'
        return false
        
      playerName = modelToBan.get 'name'
      args = ''
      ## 'perm' ban type does not have a banValue (i.e. rounds, seconds)
      if banType == 'perm'
        args = [ 'name', playerName, banType]
      else 
        args = [ 'name', playerName, banType, banValue]
      action = JSON.stringify( { action: action, serverId: @serverId, updateValues:  args } )
      $.ajax
        type: 'POST'
        url: 'servers/action'
        contentType: "application/json"
        data: action
        dataType: "json"
        success: (data, textStatus, jqXHR) =>
          arcadianUtil.renderSuccessAlertMessage 'Player banned success'
          ## re-render
          @render()    
          	  
    movePlayer: (event)->
      ## id is structured like playerId:teamId:squadId
      command = $(event.currentTarget).attr 'id'
      parsedCommand = command.split ':'
      modelToMove = @collection.get parsedCommand[0]
      ## Change the team/squad the player is on and save the information
      ## type conversion needed since we compare based on int later
      teamId = parseInt parsedCommand[1]
      squadId = parseInt parsedCommand[2]
      modelToMove.save { teamId:teamId, squadId:squadId }, success: (model, response) =>
        arcadianUtil.renderSuccessAlertMessage 'Player move success'
        ## re-render the player list
        @render()
      @      

    kickPlayer: (event)->
      id = $(event.currentTarget).attr 'id'
      modelToDestroy = @collection.get id
      ## this can happen if the player drops from the server, etc.
      if(_.isUndefined(modelToDestroy))
        arcadianUtil.renderFailureAlertMessage 'Player kicked failure'
        return false      
      playerName = modelToDestroy.get 'name'
      modelToDestroy.destroy success: (model, response) =>
        arcadianUtil.renderSuccessAlertMessage 'Player Kicked:' + playerName
        ## re-render
        @render()    
      @      
          
    refreshData: ->
      if(_.isNull(@poller))
        ## polling manager to start a poller for player changes
        options =
          delay: 10000
          success: ->
          error: ->
            console.error "error polling player list"
        @poller = PollingManager.getPoller @collection, options        
        @poller.start()
      @

    getMoveList: (teamId, squadsPerSide, name) ->
      ## data structure so we can build move list on UI
      class Move
        constructor: (@teamId, @squadId, @squadCharacter, @name) ->
      
      # array to hold these values to be iterated through on UI
      moveArray = new Array()
      ## Create Squads for each team
      squadCharacter = 'A'
      for i in [0...squadsPerSide]
        squadId = i+1
        moveArray[squadId] = new Move teamId, squadId, squadCharacter, name
        ## increment the letter (i.e. A,B,C,D etc.)
        squadCharacter = String.fromCharCode(squadCharacter.charCodeAt() + 1)
      
      return moveArray
    
    isSquadDeathMatchGameMode: ->
      ## this will determine the structure of the page (regular vs squad = 2 teams vs 4)
      gameMode = @serverInfoModel.get 'gamemode'
      if !_.isUndefined(gameMode) && !_.isNull(gameMode) && gameMode is 'SquadDeathMatch0' 
        return true
      else
        return false

    render: ->
      @serverInfoModel.fetch success: (model, response) =>
        @renderData()

    renderData: ->
      ## this will determine the structure of the page (regular vs squad = 2 teams vs 4)
      isSquadDeathMatch = @isSquadDeathMatchGameMode()
      
      ## data structure so we can build team list on UI
      class TeamContainer
        constructor: (@team, @name, @tableName) ->      
      ## create the container class to hold squad data
      class Team
        constructor: (@modelCollection) ->
        
      ## Seperate the players into different buckets
      ## Players can be on one of 2-4 teams (and one un-assigned team) and a large number of squads per team
      ## 4 teams is only possible when playing Squad Deathmatch
      ## These arrays hold the squads for each team
      teamArray = new Array()
      team0 = new Team new playerCollection()
      team1 = new Team new playerCollection()
      team2 = new Team new playerCollection()
      team3 = new Team new playerCollection()
      team4 = new Team new playerCollection()
      teamMoveArray = new Array()

      if isSquadDeathMatch
        squadsPerSide = 1
        name = 'Squad'
        teamArray[0] = new TeamContainer team1, 'Squad A', 'team1Table'
        teamMoveArray[0] = @getMoveList(1, squadsPerSide, name)
        teamArray[1] = new TeamContainer team2, 'Squad B', 'team2Table'
        teamMoveArray[1] = @getMoveList(2, squadsPerSide, name)
        teamArray[2] = new TeamContainer team3, 'Squad C', 'team3Table'
        teamMoveArray[2] = @getMoveList(3, squadsPerSide, name)
        teamArray[3] = new TeamContainer team4, 'Squad D', 'team4Table'
        teamMoveArray[3] = @getMoveList(4, squadsPerSide, name)
      else
        squadsPerSide = 8
        name = 'Team'
        teamArray[0] = new TeamContainer team1, 'Team 1', 'team1Table'
        teamMoveArray[0] = @getMoveList(1, squadsPerSide, name)
        teamArray[1] = new TeamContainer team2, 'Team 2', 'team2Table'
        teamMoveArray[1] = @getMoveList(2, squadsPerSide, name)
      
      ## iterate through the players and add to the respective squad collections
      _.each @collection.models, (model) ->
        teamId = model.get 'teamId' 
        squadId = model.get 'squadId'
        name = model.get 'name'
        console.log 'name:' + name + ':teamId:' + teamId + ":squadId:" + squadId
        if teamId is 1
          team1.modelCollection.add model
        else if teamId is 2
          team2.modelCollection.add model          
        else if teamId is 3
          team3.modelCollection.add model          
        else if teamId is 4
          team4.modelCollection.add model          
        else if teamId is 0
          team0.modelCollection.add model          

      data =                
        team0 : team0
        teamMoveArray : teamMoveArray
        teamArray : teamArray
        _: _  
      
      compiledTemplate = _.template playerListTemplate, data
      @$el.empty()
      @$el.html compiledTemplate
      @
      
    clear: ->
      ## clear out any intervals and el information
      if(!_.isNull(@poller))
        @poller.stop()
        @poller = null
      @$el.empty()

  return playerListView
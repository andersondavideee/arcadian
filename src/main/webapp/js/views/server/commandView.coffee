define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  chatTemplate = require 'text!templates/server/chatTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'

  class commandView extends Backbone.View
  
    el: $("#page")
  
    serverId:null

    init: (options)->
      @serverId = options.serverId
      @render()
      @
      
    events:      
      "click .chat" : "chat" 
      "click .sendCommand" : "sendCommand" 
      
    chat: ->
      yellDuration = '15'
      action = $("#chatSelectAction").val()
      group = $("#chatSelectGroup").val()
      chatMessage = $("#chatMessage").val()
      args = ''
      ## the action YELL takes a duration, SAY does not
      if action == 'YELL' 
        if group == 'all'
          args = [ chatMessage, yellDuration, group]
        else
          ## parse 'team x' as 'team' 'x'
          parsedGroup = group.split ' '
          args = [ chatMessage, yellDuration, parsedGroup[0], parsedGroup[1]]
      ## Must be a SAY
      else
        if group == 'all'
          args = [ chatMessage, group]
        else
          ## parse 'team x' as 'team' 'x'
          parsedGroup = group.split ' '
          args = [ chatMessage, parsedGroup[0], parsedGroup[1]]
      
      action = JSON.stringify( { action: action, serverId: @serverId, updateValues:  args } )
      $.ajax
        type: 'POST'
        url: 'servers/action'
        contentType: "application/json"
        data: action
        dataType: "json"
        success: (data, textStatus, jqXHR) ->
          arcadianUtil.renderSuccessAlertMessage 'Chat Success'
      ## clear out the original request
      $('#chatMessage').val('')
      @

    sendCommand: ->
      commandArgs = []
      command = $("#command").val()
      ## parse the command and send to the server 
      parsedCommands = command.split ' '
      ## first parameter is the action name
      actionName = parsedCommands[0]
      ## remove the first item
      parsedCommands.splice(0, 1)
      ## iterate and pull out all the args for the command
      _.each parsedCommands, (parsedCommand) ->
        commandArgs.push parsedCommand
      ## create the JSON object to issue the command
      action = JSON.stringify( { action: actionName, serverId: @serverId, updateValues:  commandArgs } )
      ## POST the command to the server
      $.ajax
        type: 'POST'
        url: 'servers/command'
        contentType: "application/json"
        data: action
        dataType: 'text'
        success: (data, textStatus, jqXHR) =>
          ## print out the response from the server command
          $('#response').html ('<div class="alert alert-success"><a class="close" data-dismiss="alert">x</a>' + data + '</div>')
          ## clear out the original request
          $('#command').val('')
          return false
      return false
    
    render: ->   
      compiledChatTemplate = _.template chatTemplate
      @$el.html compiledChatTemplate
      @
    
    clear: ->
      @$el.empty()
      
  return commandView
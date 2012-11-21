define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverInfoModel = require 'cs!models/serverInfoModel'  
  serverInfoTemplate = require 'text!templates/server/serverInfoTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  
  poller = require 'backbone-poller'  

  class serverInfoView extends Backbone.View

    el: $("#server-info")
  
    serverId: null  
    serverInfoModel:null
    poller:null    

    init: (options)->
      @serverInfoModel = new serverInfoModel { serverId : options.serverId }
      ## serverInfoModel.fetch() is asynchronous - once the fetch is done it will callback to the render method
      ## on serverInfoModel.fetch() a 'change' event is triggered (unlike collection.fetch() which triggers a 'reset' event)
      @serverInfoModel.bind "change", @render, @
      ## passed into the constructor
      @serverId = options.serverId
      @refreshData()
      @
  
    refreshData: ->      
      if(_.isNull(@poller))
        ## polling manager
        options =
          delay: 20000
          success: ->
          error: ->
            console.error "error polling server info"
        @poller = PollingManager.getPoller @serverInfoModel, options        
        @poller.start()
      @
          
    ## utility function that converts seconds to days/hours/minutes/seconds
    getDateObject: (seconds) ->
      dateObj = new Object()
      dateObj.days = Math.floor(seconds / 86400)
      dateObj.hours = Math.floor((seconds % 86400) / 3600)
      dateObj.minutes = Math.floor(((seconds % 86400) % 3600) / 60)
      dateObj.seconds = ((seconds % 86400) % 3600) % 60
      return dateObj
      
    render: ->
      data = 
        serverInfo : @serverInfoModel
        _: _
        serverUpTime : @getDateObject (@serverInfoModel.get 'serverUpTime')
        roundTime : @getDateObject (@serverInfoModel.get 'roundTime')
      
      compiledTemplate = _.template serverInfoTemplate, data
      @$el.html compiledTemplate      
      @

    clear: ->
      if(!_.isNull(@poller))
        @poller.stop()
        @poller = null
      @$el.empty()

  return serverInfoView
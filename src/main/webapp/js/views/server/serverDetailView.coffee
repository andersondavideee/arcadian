define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverModel = require 'cs!models/serverModel'  
  serverDetailTemplate = require 'text!templates/server/serverDetailTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  

  class serverDetailView extends Backbone.View
  
    el: $("#details")
  
    init: (options)->
      @serverModel = new serverModel { id : options.serverId }
      @serverModel.bind 'change', @render, @
      @refreshData()
      @
  
    refreshData: ->
      @serverModel.fetch()
      @

    render: ->
      data = 
        server : @serverModel,
        _: _  
      
      compiledTemplate = _.template serverDetailTemplate, data
      @$el.html compiledTemplate      
      @

    clear: ->
      ## clear el information
      @$el.empty()

  return serverDetailView
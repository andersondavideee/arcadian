define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  adminModel = require 'cs!models/adminModel'
  adminPermissionsTemplate = require 'text!templates/server/adminPermissionsTemplate.html'
  permissionCollection = require 'cs!collections/permissionCollection'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  

  class adminPermissionsView extends Backbone.View
  
    el: $("#page")
  
    serverId:null
    
    init: (options)->
      @adminModel = new adminModel()
      @serverId = options.serverId
      @adminModel.bind 'change', @render, @  
      @permissionCollection = new permissionCollection()
      @permissionCollection.serverId = options.serverId      
      @refreshData()

    refreshData: ->
      @adminModel.url = 'servers/' + @serverId + '/admin'
      @adminModel.fetch()
      ## static server permission list
      @permissionCollection.fetch()      
     
    render: ->      
      data =
        admin : @adminModel
        permissions : @permissionCollection.models
        
      compiledTemplate = _.template adminPermissionsTemplate, data
      @$el.html compiledTemplate

    clear: ->
      @$el.empty()

  return adminPermissionsView
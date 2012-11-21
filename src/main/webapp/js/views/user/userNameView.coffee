define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  userModel = require 'cs!models/userModel'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  
  
  class userNameView extends Backbone.View
  
    el: $("#username")
    renderedUsername:false
  
    init: (options)->
      @model = new userModel()
      ## model.fetch() is asynchronous - once the fetch is done it will callback to the render method
      ## on model.fetch() a 'change' event is triggered (unlike collection.fetch() which triggers a 'reset' event)
      @model.bind 'change', @render, @
      @refreshData()
      @
  
    refreshData: ->
      @model.fetch()
      @

    render: ->
      if(!@rederedUsername)
        @$el.append (@model.get 'username')
        @renderedUsername = true        
      @
    
    clear: ->
      ## clear el information
      @$el.empty()
      
  return userNameView
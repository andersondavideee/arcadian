define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  aboutTemplate = require 'text!templates/home/aboutTemplate.html'
  
  class aboutView extends Backbone.View
  
    el: $("#page")

    render: ->
      compiledTemplate = _.template aboutTemplate, $
      @$el.html compiledTemplate
  
  return aboutView
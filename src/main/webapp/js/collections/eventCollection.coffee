define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  eventModel = require 'cs!models/eventModel'

  class eventCollection extends Backbone.Collection
    url: -> 'servers/' + @serverId + '/events',
    serverId:null,
    model: eventModel

  return eventCollection
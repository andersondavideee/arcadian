define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  playerModel = require 'cs!models/playerModel'

  class playerCollection extends Backbone.Collection
    url: -> 'players/' + @serverId,
    serverId:null,
    model: playerModel

  return playerCollection
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class playerModel extends Backbone.Model

    url: ->
      'players/' + (@get 'serverId') + '/' + (@get 'name')

    getSquadCharacter: ->
      squadCharacter = 'A'      
      return String.fromCharCode(squadCharacter.charCodeAt() + ((@get 'squadId') - 1))
      
  return playerModel
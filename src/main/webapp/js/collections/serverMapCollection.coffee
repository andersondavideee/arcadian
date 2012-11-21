define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverMapModel = require 'cs!models/serverMapModel'

  class serverMapCollection extends Backbone.Collection
    url: -> 
      'servers/' + @serverId + '/maps'
    
    serverId:
      null
    
    model: serverMapModel

  return serverMapCollection
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverVarModel = require 'cs!models/serverVarModel'

  class serverVarCollection extends Backbone.Collection
    url: -> 
      'servers/' + @serverId + '/vars'
    
    serverId:
      null
    
    model: serverVarModel

  return serverVarCollection
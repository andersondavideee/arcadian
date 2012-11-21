define (require) ->

  ## Imports
  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  mapModel = require 'cs!models/mapModel'

  ## Module
  class mapCollection extends Backbone.Collection
    url: -> 'data/' + @serverId + '/maps',
    serverId:null,
    model: mapModel,
    initialize: ->

  ## Export
  return mapCollection
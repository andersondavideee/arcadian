define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  serverModel = require 'cs!models/serverModel'

  class serverCollection extends Backbone.Collection
    url: -> 'servers',
    model: serverModel

  return serverCollection
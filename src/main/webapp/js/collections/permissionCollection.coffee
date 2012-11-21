define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  permissionModel = require 'cs!models/permissionModel'

  class permissionCollection extends Backbone.Collection
    
    url: -> 'data/' + @serverId + '/permissions'
    serverId: null
    model: permissionModel

  return permissionCollection
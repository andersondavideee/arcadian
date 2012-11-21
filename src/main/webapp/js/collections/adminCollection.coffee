define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  adminModel = require 'cs!models/adminModel'

  adminCollection = Backbone.Collection.extend
    
    url: -> 'servers/' + @serverId + '/admins'
    serverId:null
    model: adminModel

  return adminCollection
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class serverInfoModel extends Backbone.Model
  
    serverId:null,
    url: ->
      'servers/' + (@get 'serverId') + '/info'

  return serverInfoModel
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class serverMapModel extends Backbone.Model

    url: -> 'servers/' + (@get('serverId')) + '/maps/' + (@get('index'))

  return serverMapModel
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class serverModel extends Backbone.Model

    url: -> 'servers/' + @id

  return serverModel
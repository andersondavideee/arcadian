define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class adminModel extends Backbone.Model

    url: -> 'servers/' + (@get 'serverId') + '/admins'

  return adminModel
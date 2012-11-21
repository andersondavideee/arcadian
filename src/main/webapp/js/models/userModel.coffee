define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class userModel extends Backbone.Model

    url: ->
      'users/user'

  return userModel
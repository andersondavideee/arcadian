define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  class serverVarModel extends Backbone.Model

    isNumber:(n) ->
      return !isNaN(parseFloat(n)) && isFinite(n)
  
    validate: (attrs) ->
      ## validate that a datatype of number has an input value that is a number
      datatype = @get 'datatype'
      value = attrs.value
      if(datatype == 'INTEGER' or datatype == 'DOUBLE' or datatype == 'LONG')
        isANumber = @isNumber value
        if(isANumber == true)
          ## validation successful
          return
        else 
          return value + ' can not be used updating this server var'
      ## validation successful
      return
    
    url: ->
      'servers/vars'

  return serverVarModel
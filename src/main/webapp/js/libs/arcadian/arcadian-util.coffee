define (require) ->

  $ = require 'jquery'

  ## define the function so it can be used globally later
  renderAlertMessage = renderAlertMessage: (alertType, alertMessage, duration) ->
    $("#update-alert").append "<div class='alert " + alertType + "'><strong>" + alertMessage + "</strong></div>"
    $("#update-alert").show()
    $("html, body").animate
      scrollTop: 0
    , "slow"
    $("." + alertType).delay(duration).fadeOut "slow", ->
      $(this).remove()
      $("#update-alert").hide()
      return
    return
    
  renderSuccessAlertMessage: (alertMessage) ->
    @renderAlertMessage 'alert-success', alertMessage, 5000
    return false

  renderFailureAlertMessage: (alertMessage) ->
    @renderAlertMessage 'alert-error', alertMessage, 10000
    return false
    
  ## If we wanted to extend a Backbone View this is how it would be done
  class ArcadianView extends Backbone.View
    
    initialize: ->
        
  ## this is done so the base class is global accessible
  ## http://stackoverflow.com/questions/8081438/declaring-backbone-extension-class-in-another-file-coffeescript
  @ArcadianView = ArcadianView
  ## define this function so it can be used globally later
  @renderAlertMessage = renderAlertMessage
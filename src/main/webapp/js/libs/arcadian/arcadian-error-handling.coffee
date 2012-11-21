define (require) ->

  $ = require 'jquery'
  arcadianUtil = require 'cs!arcadianUtil'

  ## Global error handling for the entire application - redirect to home page showing error from server
  $(document).ajaxError (event, xhr, settings, exception) ->
    ## convert the responseText to a JSON object
    responseJson = eval('(' + xhr.responseText + ')')
    ## show the text associated with the error object
    errorMsg = responseJson.error
    status = xhr.status
    console.log 'error msg:' + errorMsg + ' and status:' + status
    ## We throw a 420 (METHOD_FAILURE) if there is a problem updating the server (this is not a HARD failure so let the Backbone View handle this error condition in the callback)
    redirectToHomePage = if status == 420 then false else true
    ## clear the server info, etc. - render error message on home page
    url = "/arcadian.html#"
    ## redirect to home page only if this is a failure that needs to go back to the main page
    if redirectToHomePage
      $(location).attr('href',url)
    ## display the error message
    arcadianUtil.renderAlertMessage 'alert-error', errorMsg, 10000
    return false
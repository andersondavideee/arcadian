define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  eventCollection = require 'cs!collections/eventCollection'  
  eventListTemplate = require 'text!templates/server/eventListTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'

  class eventListView extends Backbone.View
  
    el: $("#page")
  
    serverId:null
    filterEvents:null
    poller:null

    initialize: ->
      @eventCollection = new eventCollection()
      @eventCollection.bind 'reset', @render, @      
      @filterEvents = new Array()
      @initFilterEvents()
        
    init: (options)->
      @eventCollection.serverId = options.serverId       
      ## passed into the constructor
      @serverId = options.serverId
      @refreshData()
      @
      
    initFilterEvents: ->
      class FilterEvent
        constructor: (@name, @value, @checked) ->
      eventId = 0
      @filterEvents[eventId++] = new FilterEvent 'Player Events', 'PLAYER', false
      @filterEvents[eventId++] = new FilterEvent 'Server Events', 'SERVER', false
      @filterEvents[eventId++] = new FilterEvent 'Admin Events', 'ADMIN', false
      @filterEvents[eventId++] = new FilterEvent 'Punkbuster Events', 'PUNKBUSTER', false
      @filterEvents[eventId++] = new FilterEvent 'Misc Events', 'MISC', false
      @filterEvents[eventId++] = new FilterEvent 'Console', 'CONSOLE', true

    events:      
      "click .filterEvent" : "filterEvent"
      
    mutateFilterEvents: (checkboxList, checkedValue)->
      ## for each checked/unchecked value update accordingly in our stateful filter event list
      ## shows filterEvents being passed in as the context which becomes 'this'
      _.each checkboxList, ((checkboxListValue) ->
        ## first find the filterEvent related to the checked value
        foundFilterEvent = _.find @, (filterEvent) ->
          filterEvent.value is checkboxListValue
        ## set the found filter event to 'checked' or 'unchecked' so we will have this when the user comes back to the page
        foundFilterEvent.checked = checkedValue
      ), @filterEvents
      
    filterEvent: ->
      checkedValues = []
      notCheckedValues = []
      ## iterate through the list of checked values
      $('#eventCheckbox :checked').each ->
        ## build a list of checked values that can be submitted to the server
        checkedValues.push $(@).val()
      ## iterate through the list of not checked values
      $('#eventCheckbox :input:not(:checked)').each ->
        ## build a list of not checked values
        notCheckedValues.push $(@).val()
      
      ## this function will iterate over the filter events and set checked/not checked value based on user input
      @mutateFilterEvents(checkedValues, true)
      @mutateFilterEvents(notCheckedValues, false)
      
      ## submit the list of checked values to the server
      checkedFilterEvents = JSON.stringify( checkedValues )
      ## build the url so we can filter events
      url = 'servers/' + @serverId + '/events'
      ## apply the event filters
      $.ajax
        type: 'POST'
        url: url
        contentType: "application/json"
        data: checkedFilterEvents
        dataType: "json"
        success: (data, textStatus, jqXHR) =>
          ## after the filter is applied re-render the event list
          @eventCollection.fetch()
            
    render: ->
      data = 
        events : @eventCollection.models
        filterEvents : @filterEvents        
        _: _       
      compiledEventTemplate = _.template eventListTemplate, data
      @$el.html compiledEventTemplate
            
    refreshData: ->
      if(_.isNull(@poller))
        ## polling manager to start a poller for player changes
        options =
          delay: 10000
          success: ->
          error: ->
            console.error "error polling event list"
        @poller = PollingManager.getPoller @eventCollection, options        
        @poller.start()
      @

    clear: ->
      ## clear out any intervals and el information
      if(!_.isNull(@poller))
        @poller.stop()
        @poller = null
      @$el.empty()
      
  return eventListView
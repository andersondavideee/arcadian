define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  adminCollection = require 'cs!collections/adminCollection'
  adminModel = require 'cs!models/adminModel'
  permissionCollection = require 'cs!collections/permissionCollection'
  adminListTemplate = require 'text!templates/server/adminListTemplate.html'
  arcadianUtil = require 'cs!arcadianUtil'
  arcadianErrorHandling = require 'cs!arcadianErrorHandling'  

  class adminListView extends Backbone.View
  
    el: $("#page")
  
    serverId:null
    
    init: (options)->
      @adminCollection = new adminCollection()
      @adminCollection.bind 'remove', @render, @
      @adminCollection.bind 'reset', @render, @
      @adminCollection.serverId = options.serverId       
      @serverId = options.serverId
      @permissionCollection = new permissionCollection()
      @permissionCollection.serverId = options.serverId      
      @refreshData()
      @
  
    events:
      "click .addAdmin": "addAdmin" 
      "click .removeAdmin": "removeAdmin" 
      "click .admin" : "showPermissions"
      "click .filterPermission" : "filterPermission"
        
    filterPermission: (event) ->
      permission = $(event.currentTarget).val()
      adminId = $(event.currentTarget).attr 'id'      
      isChecked = $(event.currentTarget).is(":checked")
      ## if it is checked add the permission, if not checked remove the permission
      adminToMutateModel = @adminCollection.get adminId
      modifyPermissionUrl = null
      if isChecked
        modifyPermissionUrl = adminToMutateModel.url() + '/' + (adminToMutateModel.get 'username') + '/add/' + permission
      else
        modifyPermissionUrl = adminToMutateModel.url() + '/' + (adminToMutateModel.get 'username') + '/remove/' + permission
      ## use ajax to mutate the permission
      $.ajax
        url: modifyPermissionUrl
      
    showPermissions: (event) ->
      adminId = $(event.currentTarget).attr 'id'      
      ## open the permissions correlated to the admin
      $('#permissions' + adminId).slideToggle()
        
    addAdmin: ->
      adminUsername = $('#adminUsername').val()
      adminExists = false
      ## make sure that admin is not already a member
      _.each @adminCollection.models, (admin) ->
        if adminUsername == admin.get 'username'
          adminExists = true
      if adminExists
        arcadianUtil.renderFailureAlertMessage 'user already an admin'
        return false      
      newAdminModel = new adminModel()
      newAdminModel.set 'username', adminUsername
      newAdminModel.set 'serverId', @serverId
      @adminCollection.create newAdminModel, success: (model, response) =>
        arcadianUtil.renderSuccessAlertMessage 'admin successfully added to server'
        @refreshData()
        @
      error: (model, response) =>
        ## the user was not added successfully to the database, make sure it does not show up in the collection
        @adminCollection.remove model
        @

    removeAdmin: (adminId) ->
      modelToDestroy = @adminCollection.get adminId
      modelToDestroy.url = modelToDestroy.url() + '/' + modelToDestroy.get 'username'
      modelToDestroy.destroy success: (model, response) ->
        arcadianUtil.renderSuccessAlertMessage 'successfully removed admin'
      ## remove the model from the collection after destroying it
      @adminCollection.remove modelToDestroy
      return false
  
    refreshData: ->
      @adminCollection.fetch()
      ## static server permission list
      @permissionCollection.fetch()
      @

    render: ->
      sortedByNameAdmins = _.sortBy @adminCollection.models, (model) ->
        model.get 'username'
      sortedByDescriptionPermissions = _.sortBy @permissionCollection.models, (model) ->
        model.get 'description'
      
      data =
        admins : sortedByNameAdmins
        permissions : sortedByDescriptionPermissions
        
      compiledTemplate = _.template adminListTemplate, data
      @$el.html compiledTemplate
      ## hide all of the admin permissions until one is clicked
      $('.permissions').hide()
      @

    clear: ->
      @$el.empty()

  return adminListView
define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  editUserView = require 'cs!views/user/editUserView'
  editServerView = require 'cs!views/server/editServerView'
  serverDetailView = require 'cs!views/server/serverDetailView'
  serverListView = require 'cs!views/server/serverListView'
  serverMapListView = require 'cs!views/server/serverMapListView'
  serverInfoView = require 'cs!views/server/serverInfoView'
  serverVarListView = require 'cs!views/server/serverVarListView'
  playerListView = require 'cs!views/server/playerListView'
  aboutView = require 'cs!views/home/aboutView'
  eventView = require 'cs!views/server/eventView'
  userNameView = require 'cs!views/user/userNameView'
  adminListView = require 'cs!views/server/adminListView'
  adminPermissionsView = require 'cs!views/server/adminPermissionsView'
  commandView = require 'cs!views/server/commandView'

  createEditUserView = ->
    return new editUserView()
  
  createEditServerView = ->
    return new editServerView()
  
  createServerDetailView = ->
    return new serverDetailView()
    
  createServerListView = ->
    return new serverListView()
  
  createServerMapListView = ->
    return new serverMapListView()

  createServerInfoView = ->
    return new serverInfoView()

  createServerVarListView = ->
    return new serverVarListView()

  createPlayerListView = ->
    return new playerListView()

  createAboutView = ->
    return new aboutView()

  createEventView = ->
    return new eventView()

  createUserNameView = ->
    return new userNameView()

  createAdminListView = ->
    return new adminListView()

  createAdminPermissionsView = ->
    return new adminPermissionsView()

  createCommandView = ->
    return new commandView()
    
  getEditUserView : _.once(createEditUserView)
  getEditServerView : _.once(createEditServerView)  
  getServerDetailView : _.once(createServerDetailView)  
  getServerListView : _.once(createServerListView)  
  getServerMapListView : _.once(createServerMapListView)
  getServerInfoView : _.once(createServerInfoView)
  getServerVarListView : _.once(createServerVarListView)
  getPlayerListView : _.once(createPlayerListView)
  getAboutView : _.once(createAboutView)
  getEventView : _.once(createEventView)
  getUserNameView : _.once(createUserNameView)
  getAdminListView : _.once(createAdminListView)
  getAdminPermissionsView : _.once(createAdminPermissionsView)  
  getCommandView : _.once(createCommandView)  
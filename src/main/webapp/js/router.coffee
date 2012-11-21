define (require) ->

  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'
  once = require 'cs!once'
  serverListView = require 'cs!views/server/serverListView'
  serverDetailView = require 'cs!views/server/serverDetailView'
  serverMapListView = require 'cs!views/server/serverMapListView'
  serverVarListView = require 'cs!views/server/serverVarListView'
  serverInfoView = require 'cs!views/server/serverInfoView'
  playerListView = require 'cs!views/server/playerListView'
  aboutView = require 'cs!views/home/aboutView'
  eventView = require 'cs!views/server/eventView'
  userNameView = require 'cs!views/user/userNameView'
  editUserView = require 'cs!views/user/editUserView'
  editServerView = require 'cs!views/server/editServerView'
  adminListView = require 'cs!views/server/adminListView'
  adminPermissionsView = require 'cs!views/server/adminPermissionsView'
  commandView = require 'cs!views/server/commandView'

  AppRouter = Backbone.Router.extend
  
    serverListView: null
    serverMapListView: null
    serverDetailView: null
    serverVarListView : null
    playerListView : null
    eventView : null
    aboutView : null
    mainView : null
    userNameView : null
    serverInfoView: null
    editUserView: null
    editServerView: null
    adminListView: null
    adminPermissionsView: null
    commandView: null

    routes:
      '/servers': 'showServers'
      '/removeServer/:id': 'removeServer'
      '/removeMap/:id': 'removeMap'
      '/removeAdmin/:id': 'removeAdmin'
      '/serverDetail/:id': 'loadMainServerPage'
      '/servers/:id/maps': 'showServerMaps'
      '/serverVars/:id': 'showServerVars' 
      '/players/:id': 'showPlayers' 
      '/events/:id': 'showEvents' 
      '/about': 'showAboutView'
      '/profile': 'editUser'
      '/editServer/:id': 'editServer'
      '/admins/:id': 'showAdmins' 
      '/adminPermissions/:id': 'showAdminPermissions' 
      '/commands/:id': 'showCommands' 
      '*actions': 'defaultAction'
    
    editServer: (id) ->
      @editServerView = once.getEditServerView()
      @editServerView.init({ serverId: id })
  
    editUser: ->
      @clearExternalViews()
      @editUserView = once.getEditUserView()
      @editUserView.init()
  
    loadMainServerPage: (id) ->
      @loadSidebarAndServerInfo(id)
      ## default by showing server maps
      @showServerMaps(id)
  
    loadSidebarAndServerInfo: (id)->
      @showServerDetail(id)
      @showServerInfo(id)      
    	  
    showServers: ->
      @clearExternalViews()      
      @serverListView = once.getServerListView()
      @serverListView.init()

    showServerMaps: (id) ->
      @clearPollers()
      @serverMapListView = once.getServerMapListView()
      @serverMapListView.init({ serverId: id })

    showServerDetail: (id)->
      @serverDetailView = once.getServerDetailView()
      @serverDetailView.init({ serverId: id })
      
    showServerInfo: (id) ->
      @serverInfoView = once.getServerInfoView()
      @serverInfoView.init({ serverId: id })

    showServerVars: (id) ->
      @clearPollers()
      @serverVarListView = once.getServerVarListView()
      @serverVarListView.init({ serverId: id })

    showEvents: (id) ->
      @clearPollers()
      @eventView = once.getEventView()
      @eventView.init({ serverId: id })

    showCommands: (id) ->
      @clearPollers()
      @commandView = once.getCommandView()
      @commandView.init({ serverId: id })
      
    showPlayers: (id) ->
      @clearPollers()
      @playerListView = once.getPlayerListView()
      @playerListView.init({ serverId: id })

    showAboutView: ->
      @clearExternalViews()
      @aboutView = once.getAboutView()
      @aboutView.render()

    showAdmins: (id) ->
      @clearPollers()
      @adminListView = once.getAdminListView()
      @adminListView.init({ serverId: id })

    showAdminPermissions: (id) ->
      @clearPollers()
      @adminPermissionsView = once.getAdminPermissionsView()
      @adminPermissionsView.init({ serverId: id })
      
    defaultAction: (actions) ->
      @clearExternalViews()
      @createUserNameView()
      ## We have no matching route, lets display the servers
      @showServers()

    createUserNameView: ->
      @userNameView = once.getUserNameView()
      @userNameView.init()

    removeServer: (id) ->
      @serverListView.removeServer(id)

    removeMap: (id) ->
      @serverMapListView.removeMap(id)

    removeAdmin: (id) ->
      @adminListView.removeAdmin(id)
      
    clearView:(view) ->
      if(!_.isUndefined(view) && !_.isNull(view))
        view.clear()

    clearPollers: ->
      @clearView(@playerListView)
      @clearView(@eventView)
        
    clearInternalViews: ->
      ## used to clear all #page and pollers
      @clearView(@serverMapListView)
      @clearView(@serverVarListView)
      @clearView(@playerListView)
      @clearView(@eventView)
      @clearView(@editUserView)
      @clearView(@commandView)
      @

    clearExternalViews: ->
      ## used to clear all #page and intervals as well as sidebar, server info bar, etc.
      @clearInternalViews()
      @clearView(@serverDetailView)
      @clearView(@serverListView)
      @clearView(@serverInfoView)
      @
    
  initialize: ->
    app_router = new AppRouter()
    Backbone.history.start()
    @
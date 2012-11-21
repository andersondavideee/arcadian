package org.steelthread.arcadian.command;

public enum CommandConstant {

    MAPLIST,
    MAPLIST_SAVE,
    SERVERINFO,
    SERVERNAME,
    GAMEPASSWORD,
    AUTOBALANCE,
    ROUNDSTARTPLAYERCOUNT,
    ROUNDRESTARTPLAYERCOUNT,
    FRIENDLYFIRE,
    MAXPLAYERS,
    BANNERURL,
    SERVERDESCRIPTION,
    KILLCAM,
    MINIMAP,
    HUD,
    CROSSHAIR,
    THREEDSPOTTING,
    MINIMAPSPOTTING,
    NAMETAG,
    THREEPCAM,
    REGENERATEHEALTH,
    TEAMKILLCOUNTFORKICK,
    TEAMKILLVALUEFORKICK,
    TEAMKILLVALUEINCREASE,
    TEAMKILLVALUEDECREASEPERSECOND,
    TEAMKILLVALUEKICKFORBAN,
    IDLETIMEOUT,
    IDLEBANROUNDS,
    VEHICLESPAWNALLOWED,
    VEHICLESPAWNDELAY,
    SOLDIERHEALTH,
    PLAYERRESPAWNTIME,
    PLAYERMANDOWNTIME,
    BULLETDAMAGE,
    ONLYSQUADLEADERSPAWN,
    PLAYERLIST,
    KICKPLAYER,
    REMOVEMAP,
    NEXTMAP,
    ADDMAP,
    RUNNEXTROUND,
    RESTARTROUND,
    MOVEPLAYER,
    VERSION,
    SAY,
    YELL,
    BAN;
    
    public String getName() {
      return name();
    }
}
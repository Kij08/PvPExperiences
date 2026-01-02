package org.shinsha.pvpexperiences.gamemodes;

import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.sessions.Session;

public abstract class GameModeBase {

    protected Session owningSession;

    GameModeBase(Session s){
        owningSession = s;
    }

    public abstract void StartGame();

    public abstract void EndGame();

    //Checks whether the lobby is in a state to start the game
    public abstract boolean CanStartGame();

    protected PvPMap GetMap() {
        return owningSession.GetMap();
    }
}

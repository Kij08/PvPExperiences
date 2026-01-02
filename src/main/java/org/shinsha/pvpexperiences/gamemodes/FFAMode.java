package org.shinsha.pvpexperiences.gamemodes;

import org.shinsha.pvpexperiences.sessions.Session;

public class FFAMode extends GameModeBase {

    public FFAMode(Session s){
        super(s);
    }

    @Override
    public void StartGame(){

    }

    @Override
    public void EndGame(){

    }

    @Override
    public boolean CanStartGame(){
        return true;
    }
}

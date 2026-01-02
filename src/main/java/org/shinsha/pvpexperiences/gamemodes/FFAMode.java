package org.shinsha.pvpexperiences.gamemodes;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.sessions.Session;

public class FFAMode extends GameModeBase {

    public FFAMode(Session s){
        super(s);
    }

    @Override
    public void StartGame(){
        for(Player p : owningSession.GetActivePlayers()){
            p.setGameMode(GameMode.ADVENTURE);
        }
        for(Player p : owningSession.GetSpectators()){
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void EndGame(){

    }

    @Override
    public boolean CanStartGame(){
        return true;
    }
}

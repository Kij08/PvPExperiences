package org.shinsha.pvpexperiences.sessions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.files.FileFactory;

import java.util.HashMap;

public class SessionManager {
    private HashMap<Player, Session> activeSessions;

    public SessionManager(){
        activeSessions = new HashMap<>();
    }

    public void CreateSession(Player p){
        activeSessions.put(p, new Session(p));
    }

    public void JoinSession(Player sessionOwner, Player joiningPlayer){
        activeSessions.get(sessionOwner).JoinSession(joiningPlayer);
    }

    public void DestroySession(Player sessionOwner){
        activeSessions.get(sessionOwner).setSessionState(SessionState.ENDING);
        for(Player p : activeSessions.get(sessionOwner).GetPlayerList()){
            p.closeInventory();
            p.sendMessage(ChatColor.RED + "Lobby has been closed by host.");
        }
        activeSessions.remove(sessionOwner);
    }

    public boolean isPlayerInSession(Player p){
        boolean playerInSession = false;
        for(var s : activeSessions.entrySet()){
            if(s.getValue().isPlayerInSession(p)){
                playerInSession = true;
                break;
            }
        }
        return playerInSession;
    }

    public void EndSessionGamemode(Player owner){
        activeSessions.get(owner).EndSessionGamemode();
    }

    public void EndSession(Player owner){
        activeSessions.get(owner).EndSession();
    }

}

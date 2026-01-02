package org.shinsha.pvpexperiences.sessions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.files.FileFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Set<String> GetMapList() throws IOException {
        Stream<Path> maps = Files.list(Paths.get(FileFactory.mapFileDir));
        return maps.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).collect(Collectors.toSet());
    }

}

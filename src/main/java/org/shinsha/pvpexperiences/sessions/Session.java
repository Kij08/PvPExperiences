package org.shinsha.pvpexperiences.sessions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.MapManager;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.files.FileFactory;
import org.shinsha.pvpexperiences.gamemodes.FFAMode;
import org.shinsha.pvpexperiences.gamemodes.GameModeBase;

import java.util.ArrayList;
import java.util.HashMap;

enum SessionState{
    NULL,
    STARTING,
    LOBBY,
    INGAME,
    ENDING
}

enum GameModes{
    FFA,
    TeamBattle,
    Jousting1v1,
    JoustingTournament,
    MAX
}

public class Session {

    private SessionState state = SessionState.NULL;
    private Player owner;
    private Lobby activeLobby;
    private ArrayList<Player> activePlayers;
    private ArrayList<Player> spectators;
    private GameModeBase runningGamemode;
    private PvPMap activeMap;

    //Original player inventories
    public HashMap<Player, ItemStack[]> InventoryMap;


    protected Session(Player p){
        state = SessionState.LOBBY;
        activePlayers = new ArrayList<>();
        spectators = new ArrayList<>();
        spectators.add(p);
        owner = p;
        activeLobby = new Lobby(this);
        p.openInventory(activeLobby.getInv());
        activeLobby.UpdatePlayerLists(activePlayers, spectators);
    }

    //Gets the player that owns this session
    protected Player getOwner(){
        return owner;
    }

    protected void setSessionState(SessionState ns){
        state = ns;
    }

    protected SessionState getSessionState() {
        return state;
    }

    //Gets the lobby UI that represents this session
    protected Lobby getActiveLobby(){
        return activeLobby;
    }

    protected void JoinSession(Player p){
        spectators.add(p);
        p.openInventory(activeLobby.getInv());
    }

    protected void JoinActivePlayers(Player p){
        spectators.remove(p);
        activePlayers.add(p);
        activeLobby.UpdatePlayerLists(activePlayers, spectators);
    }

    protected void JoinSpectatingPlayers(Player p){
        activePlayers.remove(p);
        spectators.add(p);
        activeLobby.UpdatePlayerLists(activePlayers, spectators);
    }

    protected void StartSession(String mapName, GameModes mode){
        if(mapName != null && mode != null){

            activeMap = PvPExperiences.getPlugin().mapManager.GetMapFromName(mapName);
            switch (mode){
                case FFA -> {
                    runningGamemode = new FFAMode(this);
                }
                case TeamBattle -> {
                }
                case Jousting1v1 -> {
                }
                case JoustingTournament -> {
                }
                case MAX -> {
                }
            }


            if(activeMap != null && runningGamemode != null && runningGamemode.CanStartGame()){
                for(Player p : GetPlayerList()){
                    p.closeInventory();
                }
                state = SessionState.INGAME;
                runningGamemode.StartGame();
            }
        }
        else{
            owner.sendMessage("Please select a map or mode");
        }
    }

    protected boolean isPlayerInSession(Player p){
        return activePlayers.contains(p) || spectators.contains(p);
    }

    protected ArrayList<Player> GetPlayerList() {
        ArrayList<Player> players = new ArrayList<>();
        players.addAll(activePlayers);
        players.addAll(spectators);
        Bukkit.broadcastMessage(players.size() + " players");
        return players;
    }

    public PvPMap GetMap() {
        return activeMap;
    }

    public ArrayList<Player> GetActivePlayers(){
        return activePlayers;
    }

    public ArrayList<Player> GetSpectators(){
        return spectators;
    }

}

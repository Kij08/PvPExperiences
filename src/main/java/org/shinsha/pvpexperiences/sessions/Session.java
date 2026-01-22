package org.shinsha.pvpexperiences.sessions;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.gamemodes.FFAMode;
import org.shinsha.pvpexperiences.gamemodes.GameModeBase;
import org.shinsha.pvpexperiences.gamemodes.Quake;
import org.shinsha.pvpexperiences.gamemodes.modifiers.HideNametags;
import org.shinsha.pvpexperiences.gamemodes.modifiers.ModifierBase;

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
    Quake,
    MAX
}


//Data for the player before they joined the session
class PrevPlayerData{

    PrevPlayerData(ItemStack[] i, Location loc, float e, GameMode m){
        inventory = i;
        location = loc;
        expLvl = e;
        mode = m;
    }

    ItemStack[] inventory;
    Location location;
    float expLvl;
    GameMode mode;
}

public class Session {

    private SessionState state = SessionState.NULL;
    private Player owner;
    private Lobby activeLobby;
    private ArrayList<Player> activePlayers;
    private ArrayList<Player> spectators;
    private GameModeBase runningGamemode;
    private PvPMap activeMap;
    private Kit activeKit;

    //Original player data
    public HashMap<Player, PrevPlayerData> playerData;

    protected Session(Player p){
        state = SessionState.LOBBY;
        activePlayers = new ArrayList<>();
        spectators = new ArrayList<>();
        playerData = new HashMap<>();
        owner = p;
        activeLobby = new Lobby(this);

        //Join the session for the owner
        JoinSession(p);
    }

    //Gets the player that owns this session
    protected Player getOwner(){
        return owner;
    }

    protected void setSessionState(SessionState ns){
        state = ns;
    }

    public SessionState getSessionState() {
        return state;
    }

    //Gets the lobby UI that represents this session
    protected Lobby getActiveLobby(){
        return activeLobby;
    }

    protected void JoinSession(Player p){
        //Store previous info
        playerData.put(p, new PrevPlayerData(p.getInventory().getContents(), p.getLocation(), p.getExp(), p.getGameMode()));


        spectators.add(p);
        p.openInventory(activeLobby.getInv());
        p.setInvulnerable(true);
        p.getInventory().clear();

        activeLobby.UpdatePlayerLists(activePlayers, spectators);
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

    protected void StartSession(String mapName, GameModes mode, String kitName, ArrayList<Modifiers> mods){
        if(mapName != null && mode != null){

            activeMap = PvPExperiences.getPlugin().mapManager.GetMapFromName(mapName);
            switch (mode){
                case FFA -> {
                    runningGamemode = new FFAMode(this, mods);
                }
                case TeamBattle -> {
                }
                case Jousting1v1 -> {
                }
                case JoustingTournament -> {
                }
                case Quake -> {
                    runningGamemode = new Quake(this, mods);
                }
                case MAX -> {
                }
            }

            activeKit = runningGamemode.GetKitOverride();
            if(runningGamemode.GetKitOverride() == null){
                //Set a kit if we selected one and there is no kit override. If not then kit is null and the players inventory will not be changed.
                if(!kitName.equals("No Kit")) {
                    activeKit = PvPExperiences.getPlugin().kitManager.GetKitFromName(kitName);
                }
            }

            if(activeMap != null && runningGamemode != null && runningGamemode.CanStartGame()){
                for(Player p : GetPlayerList()){
                    p.closeInventory();
                    p.setInvulnerable(false);
                }
                state = SessionState.INGAME;
                runningGamemode.StartGame();
            }
        }
        else{
            owner.sendMessage("Please select a map or mode");
        }
    }

    protected void EndSessionGamemode(){
        runningGamemode.EndGame();
        setSessionState(SessionState.LOBBY);

        for(Player p : activePlayers){
            ResetPlayer(p);
        }
        for(Player p : spectators){
            ResetPlayer(p);
        }

        for(Player p : activePlayers){
            p.openInventory(activeLobby.getInv());
        }
        for(Player p : spectators){
            p.openInventory(activeLobby.getInv());
        }
    }

    protected void EndSession(){
        runningGamemode.EndGame();
        for(Player p : activePlayers){
            ResetPlayer(p);
        }
        for(Player p : spectators){
            ResetPlayer(p);
        }
    }

    private void ResetPlayer(Player p){
        PrevPlayerData data = playerData.get(p);
        Bukkit.broadcastMessage("Data" + playerData.size());
        if(data != null){
            p.getInventory().setContents(data.inventory);
            p.teleport(data.location);
            p.setExp(data.expLvl);
            p.setGameMode(data.mode);
        }
    }

    public boolean isPlayerInSession(Player p){
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

    public Kit GetKit(){
        return activeKit;
    }

    public ArrayList<Player> GetActivePlayers(){
        return activePlayers;
    }

    public ArrayList<Player> GetSpectators(){
        return spectators;
    }

}

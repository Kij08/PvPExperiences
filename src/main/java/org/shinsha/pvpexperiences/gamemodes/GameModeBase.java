package org.shinsha.pvpexperiences.gamemodes;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.gamemodes.modifiers.HideNametags;
import org.shinsha.pvpexperiences.gamemodes.modifiers.ModifierBase;
import org.shinsha.pvpexperiences.gamemodes.modifiers.OldPvP;
import org.shinsha.pvpexperiences.sessions.Modifiers;
import org.shinsha.pvpexperiences.sessions.Session;

import java.util.ArrayList;

public abstract class GameModeBase {

    protected Session owningSession;
    private ArrayList<Modifiers> modList;
    protected ArrayList<ModifierBase> modifiers;

    GameModeBase(Session s){
        owningSession = s;
    }

    GameModeBase(Session s, ArrayList<Modifiers> mods){
        owningSession = s;

        modifiers = new ArrayList<>();
        modList = mods;

    }

    public abstract void StartGame();

    public abstract void EndGame();

    //Checks whether the lobby is in a state to start the game
    public abstract boolean CanStartGame();

    public abstract Scoreboard GetModeScoreboard();

    protected PvPMap GetMap() {
        return owningSession.GetMap();
    }

    public Kit GetKitOverride(){
        return null;
    }

    protected void SetupModifiers(){
        for(Modifiers mod : modList){
            switch (mod) {
                case HideNametags -> {
                    ModifierBase modObj = new HideNametags(owningSession, this);
                    modObj.SetupModifier();
                    modifiers.add(modObj);
                }
                case OldPvp -> {
                    ModifierBase modObj = new OldPvP(owningSession, this);
                    modObj.SetupModifier();
                    modifiers.add(modObj);
                }
            }
        }
    }

    protected void SetupModifiers(ArrayList<Modifiers> blacklist){
        for(Modifiers mod : modList){
            if(!blacklist.isEmpty() && blacklist.contains(mod)){
                continue;
            }
            switch (mod) {
                case HideNametags -> {
                    ModifierBase modObj = new HideNametags(owningSession, this);
                    modObj.SetupModifier();
                    modifiers.add(modObj);
                }
                case OldPvp -> {
                    ModifierBase modObj = new OldPvP(owningSession, this);
                    modObj.SetupModifier();
                    modifiers.add(modObj);
                }
            }
        }
    }

    protected void GivePlayerKits() {
        for (Player p : owningSession.GetActivePlayers()) {
            Kit k = owningSession.GetKit();
            if (k != null) {
                k.PopulatePlayerInventory(p.getInventory());
            }
        }
    }
}

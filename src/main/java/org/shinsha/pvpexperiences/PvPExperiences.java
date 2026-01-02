package org.shinsha.pvpexperiences;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.commands.PvPEditCommand;
import org.shinsha.pvpexperiences.commands.PvPPlayCommand;
import org.shinsha.pvpexperiences.listeners.*;
import org.shinsha.pvpexperiences.sessions.SessionManager;

import java.util.HashMap;

public class PvPExperiences extends JavaPlugin {

    private static PvPExperiences plugin;
    public static PvPExperiences getPlugin() {
        return plugin;
    }

    private HashMap<Player, PvPMap> editingPlayerMap;
    public HashMap<Player, ItemStack[]> InventoryMap;
    public SessionManager sessionManager;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("onEnable is called!");
        editingPlayerMap = new HashMap<>();
        InventoryMap = new HashMap<>();

        //Serializable classes
        ConfigurationSerialization.registerClass(PvPMap.class);

        //Commands
        getCommand("pvpedit").setExecutor(new PvPEditCommand());
        getCommand("pvpplay").setExecutor(new PvPPlayCommand());

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        getServer().getPluginManager().registerEvents(new ItemHeldListener(), this);
        getServer().getPluginManager().registerEvents(new DamageBlockListener(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockListener(), this);
        getServer().getPluginManager().registerEvents(new DisconnectListener(), this);

        //Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        sessionManager = new SessionManager();
    }

    public void RegisterEditingPlayer(Player p, PvPMap mapName){
        editingPlayerMap.put(p, mapName);
    }

    public boolean isPlayerEditing(Player p){
        return editingPlayerMap.containsKey(p);
    }

    public boolean isMapEditing(String mapName){

        for(var map : editingPlayerMap.entrySet()){
            if(map.getValue().getMapName().equals(mapName)){
                return true;
            }
        }

        return false;
    }

    public PvPMap getEditingMap(Player p){
        return editingPlayerMap.get(p);
    }

    public void EndEditingPlayer(Player p){
        editingPlayerMap.remove(p);
    }

    public void StorePlayerInventory(Player p){
        InventoryMap.put(p, p.getInventory().getContents());
    }

    public ItemStack[] RetrievePlayerInventory(Player p){
        p.getInventory().setContents(InventoryMap.get(p));
        return InventoryMap.remove(p);
    }

}

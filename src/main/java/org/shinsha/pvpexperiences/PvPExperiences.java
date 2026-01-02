package org.shinsha.pvpexperiences;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.assetmanagers.KitManager;
import org.shinsha.pvpexperiences.assetmanagers.MapManager;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.commands.PvPEditCommand;
import org.shinsha.pvpexperiences.commands.PvPKitCommand;
import org.shinsha.pvpexperiences.commands.PvPPlayCommand;
import org.shinsha.pvpexperiences.listeners.*;
import org.shinsha.pvpexperiences.sessions.SessionManager;

import java.util.HashMap;

public class PvPExperiences extends JavaPlugin {

    private static PvPExperiences plugin;
    public static PvPExperiences getPlugin() {
        return plugin;
    }

    public HashMap<Player, ItemStack[]> InventoryMap;
    public SessionManager sessionManager;
    public KitManager kitManager;
    public MapManager mapManager;

    @Override
    public void onEnable() {
        plugin = this;

        //Serializable classes
        ConfigurationSerialization.registerClass(PvPMap.class);
        ConfigurationSerialization.registerClass(Kit.class);

        sessionManager = new SessionManager();
        kitManager = new KitManager();
        mapManager = new MapManager();

        getLogger().info("onEnable is called!");
        InventoryMap = new HashMap<>();

        //Commands
        getCommand("pvpedit").setExecutor(new PvPEditCommand());
        getCommand("pvpplay").setExecutor(new PvPPlayCommand());
        getCommand("pvpkit").setExecutor(new PvPKitCommand());

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
    }

    public void StorePlayerInventory(Player p){
        InventoryMap.put(p, p.getInventory().getContents());
    }

    public ItemStack[] RetrievePlayerInventory(Player p){
        p.getInventory().setContents(InventoryMap.get(p));
        return InventoryMap.remove(p);
    }

}

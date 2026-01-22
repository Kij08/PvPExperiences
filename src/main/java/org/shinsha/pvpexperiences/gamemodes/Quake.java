package org.shinsha.pvpexperiences.gamemodes;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.sessions.Modifiers;
import org.shinsha.pvpexperiences.sessions.Session;

import java.util.ArrayList;

public class Quake extends GameModeBase implements Listener {

    private Scoreboard scoreboard;

    public Quake(Session s){
        super(s);
    }

    public Quake(Session s, ArrayList<Modifiers> mods){
        super(s, mods);
    }

    @Override
    public void StartGame() {
        GivePlayerKits();

        PvPExperiences.getPlugin().getServer().getPluginManager().registerEvents(this, PvPExperiences.getPlugin());

        ArrayList<Modifiers> blacklist = new ArrayList<>();
        blacklist.add(Modifiers.OldPvp);
        SetupModifiers(blacklist);
    }

    @Override
    public void EndGame() {
        HandlerList.unregisterAll(this);
        CleanupModifiers();
    }

    @Override
    public Kit GetKitOverride(){
        Kit k = new Kit("QuakeKit");
        k.AddItemToKit(new ItemStack(Material.DIAMOND_HOE), 0);
        return k;
    }

    @Override
    public Scoreboard GetModeScoreboard(){
        return scoreboard;
    }

    @Override
    public boolean CanStartGame() {
        return true;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(owningSession.isPlayerInSession(p)){

            if(e.getHand() != EquipmentSlot.HAND) {
                return;
            }

            if(e.getItem().getType() == Material.DIAMOND_HOE && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
                e.setCancelled(true);
                e.setUseItemInHand(Event.Result.DENY);

                Vector lookDir = p.getLocation().getDirection();
                Location spawnLoc = p.getEyeLocation();
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0F);

                BukkitTask taskId = Bukkit.getServer().getScheduler().runTaskTimer(PvPExperiences.getPlugin(), new Runnable() {
                    public void run() {
                        p.spawnParticle(Particle.DUST, spawnLoc, 1,dustOptions);
                        spawnLoc.add(lookDir.normalize());
                    }
                }, 0, 5);

            }
        }
    }
}

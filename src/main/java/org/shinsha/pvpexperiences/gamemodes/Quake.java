package org.shinsha.pvpexperiences.gamemodes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.shinsha.pvpexperiences.sessions.Session;

public class Quake extends GameModeBase implements Listener {

    public Quake(Session s){
        super(s);
    }

    @Override
    public void StartGame() {
        for(Player p : owningSession.GetActivePlayers()){
            p.getInventory().clear();
            p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_HOE));
        }
    }

    @Override
    public void EndGame() {

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

                p.getLocation().getDirection();
                p.getEyeHeight();
                p.getEyeLocation();
            }
        }
    }
}

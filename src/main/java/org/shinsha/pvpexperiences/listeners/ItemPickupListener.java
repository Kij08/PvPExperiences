package org.shinsha.pvpexperiences.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.shinsha.pvpexperiences.PvPExperiences;

public class ItemPickupListener implements Listener {
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player p){
            if(PvPExperiences.getPlugin().mapManager.isPlayerEditing(p)){
                e.setCancelled(true);
            }
        }
    }
}

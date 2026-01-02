package org.shinsha.pvpexperiences.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.shinsha.pvpexperiences.PvPExperiences;

public class DamageBlockListener implements Listener {
    @EventHandler
    public void onBlockDamage(BlockDamageEvent e){
        if(PvPExperiences.getPlugin().isPlayerEditing(e.getPlayer())){
            e.setCancelled(true);
        }
    }
}

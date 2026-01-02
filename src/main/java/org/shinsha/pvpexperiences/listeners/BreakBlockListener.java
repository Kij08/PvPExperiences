package org.shinsha.pvpexperiences.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.shinsha.pvpexperiences.PvPExperiences;

public class BreakBlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(PvPExperiences.getPlugin().isPlayerEditing(e.getPlayer())){
            e.setCancelled(true);
        }
    }
}

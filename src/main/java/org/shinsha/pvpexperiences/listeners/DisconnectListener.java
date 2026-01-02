package org.shinsha.pvpexperiences.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shinsha.pvpexperiences.PvPExperiences;

public class DisconnectListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!PvPExperiences.getPlugin().mapManager.isPlayerEditing(p)) {
            return;
        }

        PvPExperiences.getPlugin().RetrievePlayerInventory(p);

    }

}

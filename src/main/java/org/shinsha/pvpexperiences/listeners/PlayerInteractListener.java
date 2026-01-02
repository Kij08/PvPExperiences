package org.shinsha.pvpexperiences.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        Player p = e.getPlayer();

        if (!PvPExperiences.getPlugin().isPlayerEditing(p)) {
            return;
        }

        PvPMap editingMap = PvPExperiences.getPlugin().getEditingMap(p);
        ItemStack item = e.getItem();
        if(item != null){
            if(e.getAction() == Action.LEFT_CLICK_BLOCK){
                //Remove spawn location
                if(item.getType() == Material.GOLDEN_SWORD){
                    p.sendMessage("You left clicked with a golden sword at " + e.getClickedBlock().getLocation());
                    if(!editingMap.spawnLocations.remove(e.getClickedBlock().getLocation())){
                        p.sendMessage("This is not a spawn location.");
                    }
                    else{
                        p.sendMessage("Removed spawn location.");
                    }
                }
                //Reposition bound1
                else if(item.getType() == Material.DIAMOND_SWORD){
                    editingMap.setBound1(e.getClickedBlock().getLocation());
                    editingMap.setOutlineVisible(p);
                    p.sendMessage("Bound 1 set to: " + editingMap.getBound1());
                }
            }
            else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
                //Add spawn location
                if(item.getType() == Material.GOLDEN_SWORD){
                    p.sendMessage("You right clicked with a golden sword at " + e.getClickedBlock().getLocation());

                    Location loc = e.getClickedBlock().getLocation().add(0.5, 0.5, 0.5);
                    Location locOffset1 = loc.clone();
                    locOffset1.add(0, 1, 0);
                    Location locOffset2 = loc.clone();
                    locOffset2.add(0, 2, 0);

                    //Check if valid spawn position
                    //TODO: Check if within map bounds
                    if((locOffset1.getBlock().getType() == Material.AIR || locOffset1.getBlock().getType() == Material.CAVE_AIR) && (locOffset2.getBlock().getType() == Material.AIR || locOffset2.getBlock().getType() == Material.CAVE_AIR)) {
                        if(editingMap.spawnLocations.contains(loc)){
                            p.sendMessage("Already a spawn location.");
                        }


                        editingMap.spawnLocations.add(loc);
                        p.sendMessage("Added spawn location.");
                    }
                    else{
                        p.sendMessage("Invalid location.");
                    }
                }
                //Reposition bound2
                else if(item.getType() == Material.DIAMOND_SWORD){
                    editingMap.setBound2(e.getClickedBlock().getLocation());
                    editingMap.setOutlineVisible(p);
                    p.sendMessage("Bound 2 set to: " + editingMap.getBound2());
                }
            }
        }

    }
}

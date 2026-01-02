package org.shinsha.pvpexperiences.listeners;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;

import java.util.ArrayList;


public class ItemHeldListener implements Listener {

    private ArrayList<BukkitTask> particleTasks = new ArrayList<>();
    private ArrayList<ItemDisplay> spawnOutlines = new ArrayList<>();

    @EventHandler
    public void onPlayerHeld(PlayerItemHeldEvent e){
        Player p = e.getPlayer();

        if (!PvPExperiences.getPlugin().mapManager.isPlayerEditing(p)) {
            cleanupSpawnLocations();

            return;
        }

        PvPMap editingMap = PvPExperiences.getPlugin().mapManager.getEditingMap(p);

        ItemStack prevItem = p.getInventory().getItem(e.getPreviousSlot());
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());


        //If previous wasnt a gold sword and this is a gold sword then call the event
        if((prevItem == null || prevItem.getType() != Material.GOLDEN_SWORD) && (newItem != null && newItem.getType() == Material.GOLDEN_SWORD)){
            p.sendMessage("Holding gold sword");


            for(int i = 0; i < editingMap.spawnLocations.size(); i++){

                //Create display outlines
                ItemDisplay display = editingMap.getWorld().spawn(editingMap.spawnLocations.get(i), ItemDisplay.class);
                display.setItemStack(new ItemStack(Material.BLACK_CONCRETE));

                Transformation t = display.getTransformation();
                t.getScale().set(0.99, 0.99, 0.99);
                display.setTransformation(t);

                display.setVisibleByDefault(false);
                display.setGlowing(true);
                p.showEntity(PvPExperiences.getPlugin(), display);

                spawnOutlines.add(display);

                //Create particle tasks
                BukkitTask task = new ParticleRunnable(editingMap.spawnLocations.get(i).toVector(), p)
                        .runTaskTimer(PvPExperiences.getPlugin(), 0L /*<-- the initial delay */, 5L /*<-- the interval */);

                particleTasks.add(task);
            }

        }
        //If we switch to something that isnt a gold sword
        else if (newItem == null || p.getInventory().getItem(e.getNewSlot()).getType() != Material.GOLDEN_SWORD) {
            p.sendMessage("No longer holding a gold sword");

            cleanupSpawnLocations();
        }
    }

    private void cleanupSpawnLocations(){
        //Delete particle tasks
        if(!particleTasks.isEmpty()){
            for(var task : particleTasks){
                task.cancel();
            }
            particleTasks.clear();
        }

        //Delete spawn outlines
        if(!spawnOutlines.isEmpty()){
            for(var outline : spawnOutlines){
                outline.remove();
            }
            spawnOutlines.clear();
        }
    }
}

class ParticleRunnable extends BukkitRunnable {
    private Vector spawnLoc; // This variable will hold the data passed to the constructor
    private Player player;

    // Constructor that accepts the data (a Player object in this case)
    public ParticleRunnable(Vector spawnLoc, Player p) {
        this.spawnLoc = spawnLoc;
        player = p;
    }

    @Override
    public void run() {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0F);
        for(int i = 0; i < 150; i++){
            player.spawnParticle(Particle.DUST, spawnLoc.toLocation(player.getWorld()), 1, 0, i, 0, dustOptions);
        }

    }
}

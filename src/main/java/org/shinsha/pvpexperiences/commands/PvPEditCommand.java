package org.shinsha.pvpexperiences.commands;

import org.shinsha.pvpexperiences.assetmanagers.MapManager;
import org.shinsha.pvpexperiences.files.FileFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;

public class PvPEditCommand implements CommandExecutor {

    public PvPEditCommand(){
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player p){
            if(args.length == 0){
                p.sendMessage("Invalid arguements. /pvpexperiences [begin|edit|save|saveas|savequit|quit]");
            }
            else{
                switch(args[0]){
                    case "begin":
                        if(!PvPExperiences.getPlugin().isPlayerEditing(p)){
                            PvPExperiences.getPlugin().RegisterEditingPlayer(p, new PvPMap("newMap", p.getWorld()));
                            SetupEditingPlayer(p);

                            p.sendMessage("Starting edit of map newMap");
                        }
                        else{
                            p.sendMessage("You are already editing a map.");
                            return true;
                        }
                        break;
                    case "edit":
                        if(args.length <= 1){
                            p.sendMessage("No map specified.");
                            return true;
                        }

                        if(PvPExperiences.getPlugin().isMapEditing(args[1])){
                            p.sendMessage("This map is already being edited.");
                            return true;
                        }

                        if(PvPExperiences.getPlugin().isPlayerEditing(p)){
                            p.sendMessage("You are already editing a map.");
                            return true;
                        }

                        PvPMap map = MapManager.GetMapFromFileName(args[1] + ".yml");

                        if(map != null){
                            PvPExperiences.getPlugin().RegisterEditingPlayer(p, map);
                            SetupEditingPlayer(p);
                            map.setOutlineVisible(p);
                            p.teleport(map.getBound2());
                        }
                        else{
                            p.sendMessage("Failed to load map.");
                        }

                        break;
                    case "save":
                        Save(p);
                        break;
                    case "saveas":
                        SaveAs(p, args[1]);
                        break;
                    case "savequit":
                        Save(p);
                        Quit(p);
                        break;
                    case "quit":
                        Quit(p);
                    break;
                }
            }

        }

        return true;
    }

    private void SetupEditingPlayer(Player p){
        //Save player inventory
        PvPExperiences.getPlugin().StorePlayerInventory(p);

        //Transition to editing state
        p.getInventory().clear();
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setInvulnerable(true);

        ItemStack BoundsEditor = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta BoundsEditorMeta = BoundsEditor.getItemMeta();
        BoundsEditorMeta.setDisplayName("Bounds Editor");
        BoundsEditor.setItemMeta(BoundsEditorMeta);

        p.getInventory().setItem(0, BoundsEditor);

        ItemStack SpawnpointEditor = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SpawnpointMeta = SpawnpointEditor.getItemMeta();
        SpawnpointMeta.setDisplayName("Spawn point Editor");
        SpawnpointEditor.setItemMeta(SpawnpointMeta);

        p.getInventory().setItem(1, SpawnpointEditor);
    }

    private void Edit(Player p){

    }

    private void Save(Player p){
        FileFactory.WriteMapFile(PvPExperiences.getPlugin().getEditingMap(p));
    }

    private void SaveAs(Player p, String newName){
        FileFactory.WriteMapFile(PvPExperiences.getPlugin().getEditingMap(p), newName);
    }

    private void Quit(Player p){
        if(PvPExperiences.getPlugin().isPlayerEditing(p)){

            //Clear map visualisers
            PvPExperiences.getPlugin().getEditingMap(p).deleteOutline();

            //Reload player inventory
            PvPExperiences.getPlugin().EndEditingPlayer(p);
            PvPExperiences.getPlugin().RetrievePlayerInventory(p);

            //Transition to gameplay state
            p.setFlying(false);
            //p.setAllowFlight(false);

            //Keep invulnerability for a couple more seconds
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskLater(PvPExperiences.getPlugin(), () -> {
                p.setInvulnerable(false);

            }, 20L * 10L /*<-- the delay */);

            p.sendMessage("Successfully quit edit mode.");
        }
        else{
            p.sendMessage("You are not editing a map.");
        }
    }

}

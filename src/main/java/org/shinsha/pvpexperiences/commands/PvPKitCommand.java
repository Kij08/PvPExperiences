package org.shinsha.pvpexperiences.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.assetmanagers.MapManager;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;
import org.shinsha.pvpexperiences.files.FileFactory;

public class PvPKitCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player p){
            if(args.length == 0){
                p.sendMessage("Invalid arguments. /pvpkit [save|load");
            }
            else{
                switch(args[0]){
                    case "save":

                        PvPExperiences.getPlugin().kitManager.CreateKit(p, args[1]);

                        break;
                    case "load":

                        break;
                }
            }

        }


        return true;
    }
}

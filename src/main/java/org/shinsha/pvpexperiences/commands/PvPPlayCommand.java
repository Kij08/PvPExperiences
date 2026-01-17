package org.shinsha.pvpexperiences.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.sessions.Session;

public class PvPPlayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player p) {
            if (args.length == 0) {
                p.sendMessage("Invalid arguements. /pvpplay [cs|join|list|eg|es]");
            } else {
                switch (args[0]) {
                    case "cs":
                        Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + " is starting a lobby!\nType /pvpplay join playername to join!");
                        PvPExperiences.getPlugin().sessionManager.CreateSession(p);
                        break;
                    case "join":
                        PvPExperiences.getPlugin().sessionManager.JoinSession(Bukkit.getPlayer(args[1]), p);
                        break;
                    case "list":
                        break;
                    case "eg":
                    case "endgame":
                        PvPExperiences.getPlugin().sessionManager.EndSessionGamemode(p);
                        break;
                    case "es":
                    case "endsession":
                        PvPExperiences.getPlugin().sessionManager.EndSession(p);
                        break;
                }
            }
        }
        return true;
    }

}

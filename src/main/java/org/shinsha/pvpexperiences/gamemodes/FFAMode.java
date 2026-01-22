package org.shinsha.pvpexperiences.gamemodes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.gamemodes.modifiers.ModifierBase;
import org.shinsha.pvpexperiences.sessions.Modifiers;
import org.shinsha.pvpexperiences.sessions.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class FFAMode extends GameModeBase implements Listener {

    HashMap<Player, PlayerStats> statMap;
    ArrayList<Integer> persistentTasks;
    Scoreboard scoreboard;

    public FFAMode(Session s){
        super(s);
    }

    public FFAMode(Session s, ArrayList<Modifiers> mods){
        super(s, mods);
    }

    @Override
    public void StartGame(){

        persistentTasks = new ArrayList<>();
        statMap = new HashMap<>();
        InitScoreboard();
        GivePlayerKits();

        int scoreLine = 1;
        for(Player p : owningSession.GetActivePlayers()){
            //Set gamemode
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(owningSession.GetMap().GetRandomSpawnLocation());

            //Start repeatable task for detecting out of bounds
            BukkitTask taskId = Bukkit.getServer().getScheduler().runTaskTimer(PvPExperiences.getPlugin(), new Runnable() {
                public void run() {
                    if(!owningSession.GetMap().inBounds(p)){
                        p.teleport(owningSession.GetMap().GetRandomSpawnLocation());
                    }
                }
            }, 2L, 20L * 2L);
            persistentTasks.add(taskId.getTaskId());

            RegisterPlayerInScoreboard(p, scoreLine);
            //Populate map with player stats
            statMap.put(p, new PlayerStats(scoreLine));
            scoreLine++;
            UpdateScoreLine(p);


            p.setScoreboard(scoreboard);

            p.sendTitle(ChatColor.GREEN + "Begin", "", 10, 60, 10);
        }
        for(Player p : owningSession.GetSpectators()){
            p.setGameMode(GameMode.SPECTATOR);
            p.setScoreboard(scoreboard);
        }

        PvPExperiences.getPlugin().getServer().getPluginManager().registerEvents(this, PvPExperiences.getPlugin());

        SetupModifiers();
    }

    @Override
    public void EndGame() {
        //Cleanup scheduled tasks
        for (int i : persistentTasks) {
            Bukkit.getScheduler().cancelTask(i);
        }

        for (Player p : owningSession.GetActivePlayers()) {
            //Set to empty scoreboard
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        HandlerList.unregisterAll(this);
        CleanupModifiers();
    }

    @Override
    public boolean CanStartGame(){
        return true;
    }

    @Override
    public Scoreboard GetModeScoreboard(){
        return scoreboard;
    }

    private void InitScoreboard(){
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective("ObjectiveName", "Criteria", ChatColor.RED + "KDA", RenderType.INTEGER);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = obj.getScore("Name                K/D");
        score.setScore(14);
    }

    private void RegisterPlayerInScoreboard(Player p, int scoreLine){
        //Register new player team
        Team team = scoreboard.registerNewTeam(p.getDisplayName());
        team.addEntry(ChatColor.AQUA + p.getDisplayName());
        Objective obj = scoreboard.getObjective("ObjectiveName");
        obj.getScore(ChatColor.AQUA + p.getDisplayName()).setScore(scoreLine);
    }

    private void UpdateScoreLine(Player p){
        PlayerStats stats = statMap.get(p);
        scoreboard.getTeam(p.getDisplayName()).setSuffix(":                       " + stats.Kills + "/" + stats.Deaths);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player noob = e.getEntity();
        Player pro = e.getEntity().getKiller();

        if(owningSession.isPlayerInSession(noob)){
            e.setKeepInventory(true);
            e.getDrops().clear();

            if(pro != null && owningSession.isPlayerInSession(pro)){
                statMap.get(pro).Kills++;
                UpdateScoreLine(pro);
            }
            statMap.get(noob).Deaths++;
            UpdateScoreLine(noob);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if(owningSession.isPlayerInSession(e.getPlayer())) {
            e.setRespawnLocation(owningSession.GetMap().GetRandomSpawnLocation());

            Kit k = owningSession.GetKit();
            if(k != null){
                k.PopulatePlayerInventory(e.getPlayer().getInventory());
            }

            //2 seconds of spawn protection
            e.getPlayer().setInvulnerable(true);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PvPExperiences.getPlugin(), new Runnable() {
                public void run() {
                    e.getPlayer().setInvulnerable(false);
                }
            }, 2L * 20L);
        }
    }
}

class PlayerStats{
    public PlayerStats(int sbLine){
        scoreBoardLine = sbLine;
    }

    int Kills = 0;
    int Deaths = 0;
    int scoreBoardLine = 0;
}

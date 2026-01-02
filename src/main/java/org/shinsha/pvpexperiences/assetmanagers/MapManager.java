package org.shinsha.pvpexperiences.assetmanagers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.files.FileFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class MapManager {

    private HashMap<String, PvPMap> mapRegistry;

    //List of maps that are being edited
    private HashMap<Player, PvPMap> editingPlayerMap;


    public MapManager(){
        //Populate registry
        mapRegistry = new HashMap<>();
        editingPlayerMap = new HashMap<>();

        try{

            for(String s : FileFactory.GetMapList()){

                PvPMap m = (PvPMap) FileFactory.GetMapConfigFile(s).getObject("Map", PvPMap.class);
                if(m != null){
                    mapRegistry.put(m.getMapName(), m);
                }
                Bukkit.broadcastMessage("meme");

            }
        }
        catch (IOException e){
            Bukkit.broadcastMessage("No maps");
        }
    }

    public Set<String> GetMapNames(){
        return mapRegistry.keySet();
    }

    public PvPMap GetMapFromName(String name){
        return mapRegistry.get(name);
    }

    public void SaveMap(Player p, String name){
        //Save players editing map
        FileFactory.WriteMapFile(getEditingMap(p), name);

        //Add the map that this player is editing to the registry
        mapRegistry.put(getEditingMap(p).getMapName(), getEditingMap(p));
    }

    public void RegisterEditingPlayer(Player p, PvPMap mapName){
        editingPlayerMap.put(p, mapName);
    }

    public boolean isPlayerEditing(Player p){
        return editingPlayerMap.containsKey(p);
    }

    public boolean isMapEditing(String mapName){

        for(var map : editingPlayerMap.entrySet()){
            if(map.getValue().getMapName().equals(mapName)){
                return true;
            }
        }

        return false;
    }

    public PvPMap getEditingMap(Player p){
        return editingPlayerMap.get(p);
    }

    public void EndEditingPlayer(Player p){
        editingPlayerMap.remove(p);
    }



}

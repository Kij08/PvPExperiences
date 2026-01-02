package org.shinsha.pvpexperiences.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;

import java.io.File;
import java.io.IOException;

public class FileFactory {

    public static final String mapFileDir = PvPExperiences.getPlugin().getDataFolder().getAbsolutePath() + "/maps";

    public static FileConfiguration GetMapFile(String mapName){
        File mapFile;
        FileConfiguration mapFileConfig;

        mapFile = new File(mapFileDir, mapName);

        if(!mapFile.exists()){
            Bukkit.broadcastMessage("Couldnt load file");
            return null;
        }

        mapFileConfig = YamlConfiguration.loadConfiguration(mapFile);

        return mapFileConfig;
    }

    public static void WriteMapFile(PvPMap map){
        File mapFile;
        FileConfiguration mapFileConfig;

        mapFile = new File(mapFileDir, map.getMapName() + ".yml");

        if(!mapFile.exists()){
            try {
                mapFile.createNewFile();
            }
            catch(IOException e){

            }
        }

        mapFileConfig = YamlConfiguration.loadConfiguration(mapFile);
        mapFileConfig.set("MapData", map);
        try{
            mapFileConfig.save(mapFile);
        }
        catch(IOException e){

        }
    }

    public static void WriteMapFile(PvPMap map, String newName){
        File mapFile;
        FileConfiguration mapFileConfig;

        mapFile = new File(mapFileDir, newName + ".yml");

        if(!mapFile.exists()){
            try {
                mapFile.createNewFile();
            }
            catch(IOException e){
                Bukkit.broadcastMessage("Could not create new file");
            }
        }

        mapFileConfig = YamlConfiguration.loadConfiguration(mapFile);
        mapFileConfig.set("MapData", map);
        try{
            mapFileConfig.save(mapFile);
        }
        catch(IOException e){

        }
    }
}

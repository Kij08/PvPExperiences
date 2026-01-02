package org.shinsha.pvpexperiences.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.shinsha.pvpexperiences.PvPExperiences;
import org.shinsha.pvpexperiences.assetmanagers.Kit;
import org.shinsha.pvpexperiences.assetmanagers.PvPMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFactory {

    public static final String mapFileDir = PvPExperiences.getPlugin().getDataFolder().getAbsolutePath() + "/maps";
    public static final String kitFileDir = PvPExperiences.getPlugin().getDataFolder().getAbsolutePath() + "/kits";

    public static Set<String> GetMapList() throws IOException {
        Stream<Path> maps = Files.list(Paths.get(FileFactory.mapFileDir));
        return maps.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).collect(Collectors.toSet());
    }

    public static FileConfiguration GetMapConfigFile(String mapName){
        File mapFile;
        FileConfiguration mapFileConfig;

        if(mapName.contains(".yml")){
            mapFile = new File(mapFileDir, mapName);
        }
        else{
            mapFile = new File(mapFileDir, mapName + ".yml");
        }

        if(!mapFile.exists()){
            Bukkit.broadcastMessage("Couldnt load file");
            return null;
        }

        mapFileConfig = YamlConfiguration.loadConfiguration(mapFile);

        return mapFileConfig;
    }

    public static void WriteMapFile(PvPMap map, String newName){
        File mapFile;
        FileConfiguration mapFileConfig;
        map.setMapName(newName);

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
        mapFileConfig.set("Map", map);
        try{
            mapFileConfig.save(mapFile);
        }
        catch(IOException e){

        }
    }

    public static Set<String> GetKitFiles() throws IOException {
        Stream<Path> kits = Files.list(Paths.get(FileFactory.kitFileDir));
        return kits.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).collect(Collectors.toSet());
    }

    public static FileConfiguration GetKitConfigFile(String kitName){
        File kitFile;
        FileConfiguration kitFileConfig;

        if(kitName.contains(".yml")){
            kitFile = new File(kitFileDir, kitName);
        }
        else{
            kitFile = new File(kitFileDir, kitName + ".yml");
        }


        if(!kitFile.exists()){
            Bukkit.broadcastMessage("Couldnt load file");
            return null;
        }

        kitFileConfig = YamlConfiguration.loadConfiguration(kitFile);

        return kitFileConfig;
    }

    public static void WriteKitFile(Kit kit){
        File kitFile;
        FileConfiguration kitFileConfig;

        kitFile = new File(kitFileDir, kit.GetKitName() + ".yml");

        if(!kitFile.exists()){
            try {
                kitFile.createNewFile();
            }
            catch(IOException e){

            }
        }

        kitFileConfig = YamlConfiguration.loadConfiguration(kitFile);
        kitFileConfig.set("Kit", kit);
        try{
            kitFileConfig.save(kitFile);
        }
        catch(IOException e){

        }
    }
}

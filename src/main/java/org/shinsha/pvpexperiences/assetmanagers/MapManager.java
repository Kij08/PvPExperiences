package org.shinsha.pvpexperiences.assetmanagers;

import org.bukkit.configuration.file.FileConfiguration;
import org.shinsha.pvpexperiences.files.FileFactory;

public class MapManager {
    public static PvPMap GetMapFromFileName(String fileName){
        FileConfiguration fc = FileFactory.GetMapFile(fileName);
        return (PvPMap) fc.get("MapData");
    }

}

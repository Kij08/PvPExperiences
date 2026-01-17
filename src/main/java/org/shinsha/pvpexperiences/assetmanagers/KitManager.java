package org.shinsha.pvpexperiences.assetmanagers;

import org.bukkit.entity.Player;
import org.shinsha.pvpexperiences.files.FileFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class KitManager {

    private HashMap<String, Kit> KitRegistry;

    public KitManager(){
        //Populate registry
        KitRegistry = new HashMap<>();
        try{
            for(String s : FileFactory.GetKitFiles()){

                Kit k = (Kit) FileFactory.GetKitConfigFile(s).getObject("Kit", Kit.class);
                if(k != null){
                    KitRegistry.put(k.GetKitName(), k);
                }
            }
        }
        catch (IOException e){
            //No kits
        }

    }

    public Set<String> GetKitNames(){
        return KitRegistry.keySet();
    }

    public void CreateKit(Player p, String name){
        //Create kit
        Kit k = new Kit(name);

        k.SaveInventoryToKit(p.getInventory());

        FileFactory.WriteKitFile(k);

        //Add to registry
        KitRegistry.put(k.GetKitName(), k);
    }

    public Kit GetKitFromName(String name){
        return KitRegistry.get(name);
    }
}

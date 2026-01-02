package org.shinsha.pvpexperiences.assetmanagers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Kit implements ConfigurationSerializable {

    private List<ItemStack> kitItems;
    private String kitName;

    public Kit(Map<String, Object> map) {
        // Deserialization constructor

        kitName = (String) map.get("Kit-Name");
        kitItems = (List<ItemStack>) map.get("Kit-Items");
    }

    public Kit(String name){
        kitName = name;
    }

    public void SaveInventoryToKit(Inventory inv){
        kitItems = Arrays.asList(inv.getContents());
    }

    public String GetKitName(){
        return kitName;
    }

    @Override
    public final Map<String, Object> serialize(){
        Map<String, Object> serialisedMap = new HashMap<>();
        serialisedMap.put("Kit-Items", kitItems);
        serialisedMap.put("Kit-Name", kitName);
        return serialisedMap;
    }
}

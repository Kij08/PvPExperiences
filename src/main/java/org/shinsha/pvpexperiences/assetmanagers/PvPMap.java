package org.shinsha.pvpexperiences.assetmanagers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.shinsha.pvpexperiences.PvPExperiences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PvPMap implements ConfigurationSerializable {

    private String mapName;
    private World world;
    private Location bound1;
    private Location bound2;
    public ArrayList<Location> spawnLocations;
    boolean isEditing = false;

    private ArrayList<ItemDisplay> outlineEdges;

    public PvPMap(Map<String, Object> map) {
        // Deserialization constructor
        mapName = (String) map.get("name");
        world = Bukkit.getWorld((String) map.get("world"));
        bound1 = (Location) map.get("bound1");
        bound2 = (Location) map.get("bound2");
        spawnLocations = (ArrayList<Location>) map.get("spawnLocations");
        outlineEdges = new ArrayList<>();

        recreateOutline();
    }

    public PvPMap(){
        this("newMap", Bukkit.getWorld("world"));
    }

    public PvPMap(String name, World world){
        mapName = name;
        this.world = world;
        bound1 = new Location(world, 0, 0, 0);
        bound2 = new Location(world, 0, 0, 0);
        spawnLocations = new ArrayList<>();
        outlineEdges = new ArrayList<>();
    }

    public Location getRandomSpawn(){
        return spawnLocations.get(0);
    }

    public World getWorld() {
        return world;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String newName){
        mapName = newName;
    }

    public void setBound1(Location bound1) {
        this.bound1 = bound1.add(0.5, 0.5, 0.5);

        recreateOutline();
    }

    public Location getBound1() {
        return bound1;
    }

    public void setBound2(Location bound2) {
        this.bound2 = bound2.add(0.5, 0.5, 0.5);

        recreateOutline();
    }

    public Location getBound2() {
        return bound2;
    }

    public void setOutlineVisible(Player p){
        for (var edge : outlineEdges) {
            p.showEntity(PvPExperiences.getPlugin(), edge);
        }
    }

    public void deleteOutline(){
        //Cleanup old outline
        if(!outlineEdges.isEmpty()){
            for(var edge : outlineEdges){
                edge.remove();
            }
            outlineEdges.clear();
        }
    }

    private void DebugPoint(Vector vec, boolean diagonal){
        //Debug points
        Location loc = new Location(world, vec.getX(), vec.getY(), vec.getZ());
        ItemDisplay display = world.spawn(loc, ItemDisplay.class);
        String s = "Block pos: {X: " + vec.getX() + " Y: " + vec.getY() + " Z: " + vec.getZ() + "}";

        if(diagonal){
            s += " Diagonal";
            display.setItemStack(new ItemStack(Material.RED_CONCRETE));
        }
        else{
            display.setItemStack(new ItemStack(Material.WHITE_CONCRETE));
        }
        display.setVisibleByDefault(true);
        Transformation t = display.getTransformation();
        t.getScale().set(1.1);
        display.setTransformation(t);

        Bukkit.broadcastMessage(s);
    }

    private void recreateOutline(){

        deleteOutline();

        Vector diagonal1 = bound1.toVector();
        Vector diagonal2 = bound2.toVector();
        double minX = Double.min(diagonal1.getX(), diagonal2.getX());
        double minY = Double.min(diagonal1.getY(), diagonal2.getY());
        double minZ = Double.min(diagonal1.getZ(), diagonal2.getZ());
        double maxX = Double.max(diagonal1.getX(), diagonal2.getX());
        double maxY = Double.max(diagonal1.getY(), diagonal2.getY());
        double maxZ = Double.max(diagonal1.getZ(), diagonal2.getZ());
        Vector xYZ = new Vector(minX, maxY, maxZ);
        Vector XyZ = new Vector(maxX, minY, maxZ);
        Vector XYz = new Vector(maxX, maxY, minZ);
        Vector Xyz = new Vector(maxX, minY, minZ);
        Vector xYz = new Vector(minX, maxY, minZ);
        Vector xyZ = new Vector(minX, minY, maxZ);
        Vector XYZ = new Vector(maxX, maxY, maxZ);
        Vector xyz = new Vector(minX, minY, minZ);

        //Min face
        createStraightLine(xyz, xyZ);
        createStraightLine(xyz, Xyz);
        createStraightLine(XyZ, xyZ);
        createStraightLine(XyZ, Xyz);

        //Max face
        createStraightLine(XYZ, xYZ);
        createStraightLine(XYZ, XYz);
        createStraightLine(xYz, xYZ);
        createStraightLine(xYz, XYz);

        //Vertical edges
        createStraightLine(XYZ, XyZ);
        createStraightLine(xyz, xYz);
        createStraightLine(xyZ, xYZ);
        createStraightLine(Xyz, XYz);

    }

    private void createStraightLine(Vector point1, Vector point2){
        Location loc = new Location(world, point1.getX() + ((point2.getX()-point1.getX())/2), point1.getY()+ ((point2.getY()-point1.getY())/2), point1.getZ()+ ((point2.getZ()-point1.getZ())/2));
        ItemDisplay display = world.spawn(loc, ItemDisplay.class);
        display.setItemStack(new ItemStack(Material.WHITE_CONCRETE));
        display.setVisibleByDefault(false);
        display.setGlowing(true);

        Transformation t = display.getTransformation();

        if(Math.abs(point1.getX() - point2.getX()) != 0){
            //If the points are seperated by the x direction
            t.getScale().set(Math.abs(point1.getX() - point2.getX()), 0.5, 0.5);
        }
        else if(Math.abs(point1.getY() - point2.getY()) != 0){
            //If the points are seperated by the y direction
            t.getScale().set(0.5, Math.abs(point1.getY() - point2.getY()), 0.5);
        }
        else if (Math.abs(point1.getZ() - point2.getZ()) != 0){
            //If the points are seperated by the z direction
            t.getScale().set(0.5, 0.5, Math.abs(point1.getZ() - point2.getZ()));
        }

        display.setTransformation(t);

        outlineEdges.add(display);
    }

    @Override
    public final Map<String, Object> serialize(){
        Map<String, Object> serialisedMap = new HashMap<>();
        serialisedMap.put("Name", mapName);
        serialisedMap.put("world", world.getName());
        serialisedMap.put("bound1", bound1);
        serialisedMap.put("bound2", bound2);
        serialisedMap.put("spawnLocations", spawnLocations);
        return serialisedMap;
    }
}

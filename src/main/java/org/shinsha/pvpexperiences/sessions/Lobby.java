package org.shinsha.pvpexperiences.sessions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.shinsha.pvpexperiences.PvPExperiences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Lobby implements Listener {

    private enum MenuState{
        Main,
        Kit,
        Mode,
        Map,
        Modifier
    }

    private MenuState state;
    private Session owningSession;
    private Inventory mainInv;
    private Inventory mapListInv;
    private Inventory modeListInv;
    private Inventory kitListInv;
    private Inventory modifierListInv;

    private String selectedMapName;
    private GameModes selectedMode;
    private String selectedKitName;
    private ArrayList<Modifiers> selectedModifiers;

    public Inventory getInv(){
        return mainInv;
    }

    public Lobby(Session session){
        owningSession = session;
        selectedModifiers = new ArrayList<>();

        mainInv = Bukkit.createInventory(null, 36, ChatColor.RED + "Lobby");
        kitListInv = Bukkit.createInventory(null, 18, ChatColor.RED + "Kits");
        mapListInv = Bukkit.createInventory(null, 18, ChatColor.RED + "Maps");
        modeListInv = Bukkit.createInventory(null, 18, ChatColor.RED + "Modes");
        modifierListInv = Bukkit.createInventory(null, 18, ChatColor.RED + "Modifers");
        InitLobby();

        PvPExperiences.getPlugin().getServer().getPluginManager().registerEvents(this, PvPExperiences.getPlugin());
        state = MenuState.Main;
    }

    final int KitSlot = 5;
    final int ModifierSlot = 7;
    final int ModeSlot = 6;
    final int MapSlot = 8;

    private void InitLobby(){
        mainInv.setItem(0, createGuiItem(Material.PLAYER_HEAD, "Players", "Active player list."));
        mainInv.setItem(18, createGuiItem(Material.OPEN_EYEBLOSSOM, "Spectators", "Spectator list."));
        mainInv.setItem(35, createGuiItem(Material.GREEN_WOOL, ChatColor.GREEN + "Begin Match"));
        mainInv.setItem(34, createGuiItem(Material.RED_WOOL, ChatColor.RED + "Quit Lobby"));
        mainInv.setItem(KitSlot, createGuiItem(Material.DIAMOND_SWORD, "Select Kit..."));
        mainInv.setItem(ModifierSlot, createGuiItem(Material.BLAZE_POWDER, "Select Modifiers..."));
        mainInv.setItem(ModeSlot, createGuiItem(Material.REDSTONE, "Select Mode..."));
        mainInv.setItem(MapSlot, createGuiItem(Material.MAP, "Select Map..."));

        //Kits
        kitListInv.setItem(0, createGuiItem(Material.DIAMOND_SWORD, "No Kit", "Take your own gear into battle."));
        int kitIndex = 1;
        for(String kit : PvPExperiences.getPlugin().kitManager.GetKitNames()){
            kitListInv.setItem(kitIndex, createGuiItem(Material.DIAMOND_SWORD, kit, "Kit description."));
            kitIndex++;
        }
        kitListInv.setItem(17, createGuiItem(Material.RED_WOOL, ChatColor.RED + "Return"));


        //Maps
        int mapIndex = 0;
        for(String map : PvPExperiences.getPlugin().mapManager.GetMapNames()){
            mapListInv.setItem(mapIndex, createGuiItem(Material.MAP, map, "Map description."));
            mapIndex++;
        }
        mapListInv.setItem(17, createGuiItem(Material.RED_WOOL, ChatColor.RED + "Return"));

        modeListInv.setItem(GameModes.FFA.ordinal(), createGuiItem(Material.REDSTONE, "FFA"));
        modeListInv.setItem(GameModes.Quake.ordinal(), createGuiItem(Material.REDSTONE, "Quake- WIP"));
        modeListInv.setItem(GameModes.TeamBattle.ordinal(), createGuiItem(Material.REDSTONE, "Team Battle- WIP"));
        modeListInv.setItem(GameModes.Jousting1v1.ordinal(), createGuiItem(Material.REDSTONE, "Jousting Versus- WIP"));
        modeListInv.setItem(GameModes.JoustingTournament.ordinal(), createGuiItem(Material.REDSTONE, "Jousting Tournament- WIP"));
        modeListInv.setItem(17, createGuiItem(Material.RED_WOOL, "Return"));

        modifierListInv.setItem(Modifiers.OldPvp.ordinal(), createGuiItem(Material.BLAZE_POWDER, ChatColor.RED + "1.8 Pvp"));
        modifierListInv.setItem(Modifiers.HideNametags.ordinal(), createGuiItem(Material.BLAZE_POWDER, ChatColor.RED + "Hide Nametags"));
        modifierListInv.setItem(17, createGuiItem(Material.RED_WOOL, ChatColor.RED + "Return"));
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    protected void UpdatePlayerLists(ArrayList<Player> newActive, ArrayList<Player> newSpectator){
        for(int i = 0; i < newActive.size() + newSpectator.size(); i++){
            mainInv.clear(i+9);
            mainInv.clear(i+27);

            if(newActive.size() > i){
                mainInv.setItem(i+9, createSkullItem(newActive.get(i)));
            }
            if(newSpectator.size() > i){
                mainInv.setItem(i+27, createSkullItem(newSpectator.get(i)));
            }
        }
    }

    private void UpdateKitName(String newName){
        ItemMeta meta = mainInv.getItem(KitSlot).getItemMeta();
        meta.setDisplayName(newName);
        mainInv.getItem(KitSlot).setItemMeta(meta);
    }

    private void UpdateMapName(String newName){
        ItemMeta meta = mainInv.getItem(MapSlot).getItemMeta();
        meta.setDisplayName(newName);
        mainInv.getItem(MapSlot).setItemMeta(meta);
    }

    private void UpdateModeName(String newName){
        ItemMeta meta = mainInv.getItem(ModeSlot).getItemMeta();
        meta.setDisplayName(newName);
        mainInv.getItem(ModeSlot).setItemMeta(meta);
    }

    private void UpdateModifierSelection(int selectedSlot){
        //If there are no enchants on the item enchant it and add it to the slected list
        //If it has enchants remove it
        if(!selectedModifiers.contains(Modifiers.values()[selectedSlot])/*!modifierListInv.getItem(selectedSlot).getItemMeta().hasEnchantmentGlintOverride()*/){
            selectedModifiers.add(Modifiers.values()[selectedSlot]);
            ItemMeta meta = modifierListInv.getItem(selectedSlot).getItemMeta();
            meta.setEnchantmentGlintOverride(true);
            meta.setDisplayName(ChatColor.GREEN + ChatColor.stripColor(meta.getDisplayName()));
            modifierListInv.getItem(selectedSlot).setItemMeta(meta);
        }
        else{
            selectedModifiers.remove(Modifiers.values()[selectedSlot]);
            ItemMeta meta = modifierListInv.getItem(selectedSlot).getItemMeta();
            meta.setEnchantmentGlintOverride(false);
            meta.setDisplayName(ChatColor.RED + ChatColor.stripColor(meta.getDisplayName()));
            modifierListInv.getItem(selectedSlot).setItemMeta(meta);
        }
    }

    public ItemStack createSkullItem(Player p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(p);
        item.setItemMeta(meta);
        return item;
    }

    private void OpenWindow(Player p, MenuState state){
        this.state = state;

        switch (state){
            case MenuState.Kit:
                p.openInventory(kitListInv);
                break;
            case MenuState.Mode:
                p.openInventory(modeListInv);
                break;
            case MenuState.Map:
                p.openInventory(mapListInv);
                break;
            case MenuState.Modifier:
                p.openInventory(modifierListInv);
                break;
            default:
                p.openInventory(mainInv);
                break;
        }
    }

    protected void CloseLobby(Player p){

        //Return to main window if we are in a sub window
        if(state != MenuState.Main){
            OpenWindow(p, MenuState.Main);
            return;
        }

        HandlerList.unregisterAll(this);

        if(p == owningSession.getOwner()){
            PvPExperiences.getPlugin().sessionManager.DestroySession(p);
        }
    }


    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!(e.getInventory().equals(mainInv) || e.getInventory().equals(mapListInv) || e.getInventory().equals(modeListInv) || e.getInventory().equals(kitListInv) || e.getInventory().equals(modifierListInv))) {
            return;
        }

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        //If we are the host check a lot more clicks than just normal players
        if(p == owningSession.getOwner()){
            switch(clickedItem.getType()){
                case GREEN_WOOL:
                    owningSession.StartSession(selectedMapName, selectedMode, selectedKitName, selectedModifiers);
                    break;
                case DIAMOND_SWORD:
                    if(state == MenuState.Main) {
                        OpenWindow(p, MenuState.Kit);
                    }
                    else{
                        selectedKitName = e.getCurrentItem().getItemMeta().getDisplayName();
                        UpdateKitName(selectedKitName);
                    }
                    break;
                case MAP:
                    if(state == MenuState.Main) {
                        OpenWindow(p, MenuState.Map);
                    }
                    else{
                        selectedMapName = e.getCurrentItem().getItemMeta().getDisplayName();
                        UpdateMapName(selectedMapName);
                    }
                    break;
                case REDSTONE:
                    if(state == MenuState.Main) {
                        OpenWindow(p, MenuState.Mode);
                    }
                    else{
                        selectedMode = GameModes.values()[e.getRawSlot()];
                        UpdateModeName(selectedMode.toString());
                    }
                    break;
                case BLAZE_POWDER:
                    if(state == MenuState.Main) {
                        OpenWindow(p, MenuState.Modifier);
                    }
                    else{
                        UpdateModifierSelection(e.getRawSlot());
                    }
                    break;
            }
        }

        switch(clickedItem.getType()){
            case PLAYER_HEAD:
                owningSession.JoinActivePlayers(p);
                break;
            case OPEN_EYEBLOSSOM:
                owningSession.JoinSpectatingPlayers(p);
                break;
            case RED_WOOL:
                CloseLobby(p);
                break;

        }


        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(mainInv) || e.getInventory().equals(mapListInv) || e.getInventory().equals(modeListInv) || e.getInventory().equals(kitListInv) || e.getInventory().equals(modifierListInv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e){
        if (e.getInventory().equals(mainInv) || e.getInventory().equals(mapListInv) || e.getInventory().equals(modeListInv) || e.getInventory().equals(kitListInv) || e.getInventory().equals(modifierListInv)) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PvPExperiences.getPlugin(), new Runnable() {
                public void run() {
                    if(owningSession.getSessionState() == SessionState.LOBBY){
                        OpenWindow((Player)e.getPlayer(), state);
                    }
                }
            }, 2);
        }
    }
}

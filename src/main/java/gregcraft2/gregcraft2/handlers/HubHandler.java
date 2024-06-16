package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import gregcraft2.gregcraft2.worldgen.HubChunkGenerator;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class HubHandler implements Listener {
    private Gregcraft2 _plugin;
    private UUID hub_id;

    private final NamespacedKey pickup_action;
    private final NamespacedKey run_worlds;
    private final NamespacedKey ongoing_worlds;
    private final NamespacedKey world_creator;
    private final NamespacedKey next_offset;
    private final NamespacedKey current_world;
    private final NamespacedKey world_lives;

    public enum PickupAction{
        NEW_WORLD,
        CHANGE_PAGE,
        SELECT_WORLD
    }

    public HubHandler(Gregcraft2 plugin){
        this._plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.pickup_action = new NamespacedKey(plugin, "pickup_action");
        this.run_worlds = new NamespacedKey(plugin, "run_worlds");
        this.ongoing_worlds = new NamespacedKey(plugin, "ongoing_worlds");
        this.world_creator = new NamespacedKey(plugin, "world_creator");
        this.next_offset = new NamespacedKey(plugin, "next_offset");
        this.current_world = new NamespacedKey(plugin, "current_world");
        this.world_lives = plugin.world_lives;

        World hub = Bukkit.getWorld("hub");
        if (hub == null){
            WorldCreator creator = new WorldCreator("hub");
            creator.generator(new HubChunkGenerator());
            hub = creator.createWorld();
            if (hub == null) {
                Bukkit.getLogger().warning("Could not find or make hub world.");
                return;
            }
        }
        this.hub_id = hub.getUID();
        hub.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        hub.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        hub.setTime(18000);
    }

    @EventHandler
    public void openChest(InventoryOpenEvent event){
        if (event.isCancelled()) return;

        if ( !(event.getPlayer() instanceof Player) ) return;
        Player player = (Player) event.getPlayer();
        if (player.getWorld().getUID() != this.hub_id) return;

        Inventory inventory = event.getInventory();
        Location inv_location = event.getInventory().getLocation();
        if (inv_location == null) return;

        World world = inv_location.getWorld();
        if (world == null) return;

        int z = inv_location.getBlockZ();
        inventory.clear();
        if (z < 3){
            //Leaderboard?????
        }else if (z < 10){
            //World select :)
            PersistentDataContainer world_nbt = world.getPersistentDataContainer();
            List<String> runs = world_nbt.getOrDefault(this.run_worlds, PersistentDataType.LIST.strings(), new ArrayList<String>());
            List<String> ongoing = world_nbt.getOrDefault(this.ongoing_worlds, PersistentDataType.LIST.strings(), new ArrayList<String>());
            this.fillWorlds(inventory, runs, ongoing);


            ItemStack create_world = new ItemStack(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
            ItemMeta meta = create_world.getItemMeta();
            if (meta == null){
                Bukkit.getLogger().warning("meta not found");
                return;
            }
            PersistentDataContainer nbt = meta.getPersistentDataContainer();
            nbt.set(this.pickup_action , PersistentDataType.INTEGER, PickupAction.NEW_WORLD.ordinal() );
            meta.setDisplayName("Create World");
            create_world.setItemMeta(meta);
            inventory.setItem(49, create_world);
        }else{
            //Stats??????????????????????????
        }
    }

    private void fillWorlds(Inventory inventory, List<String> worlds, List<String> ongoing, int offset){
        final int NUM_ROWS = 3;
        final int NUM_COLS = 7;
        final int OFFSET_SIZE = NUM_ROWS * NUM_COLS;
        final int total_offset = OFFSET_SIZE * offset;

        if (offset != 0){
            ItemStack item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName("Previous Page");
            PersistentDataContainer nbt = meta.getPersistentDataContainer();
            nbt.set(this.pickup_action, PersistentDataType.INTEGER, PickupAction.CHANGE_PAGE.ordinal());
            nbt.set(this.next_offset, PersistentDataType.INTEGER, offset - 1);
            item.setItemMeta(meta);

            inventory.setItem(18, item);
        } else {
            inventory.clear(18);
        }

        if ( (offset + 1) * OFFSET_SIZE < worlds.size() ){
            ItemStack item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName("Next Page");
            PersistentDataContainer nbt = meta.getPersistentDataContainer();
            nbt.set(this.pickup_action, PersistentDataType.INTEGER, PickupAction.CHANGE_PAGE.ordinal());
            nbt.set(this.next_offset, PersistentDataType.INTEGER, offset + 1);
            item.setItemMeta(meta);
            inventory.setItem(26, item);
        } else {
            inventory.clear(26);
        }

        for (int row = 0; row < NUM_ROWS; row++ ){
            for (int col = 0; col < NUM_COLS; col++){
                int index = total_offset + (row * NUM_COLS) + col;
                if (index >= worlds.size()){
                    inventory.clear((row+1) * 9 + 1 + col);
                    continue;
                }

                String world_name = worlds.get(index);
                ItemStack world_item = new ItemStack(Material.COMPASS);
                ItemMeta meta = world_item.getItemMeta();
                assert meta != null;
                if (ongoing.contains(world_name)){
                    meta.setLore(Collections.singletonList("Ongoing"));
                }else{
                    world_item.setType(Material.SKELETON_SKULL);
                    meta = world_item.getItemMeta();
                    assert meta != null;
                }
                meta.setDisplayName(world_name);
                PersistentDataContainer nbt = meta.getPersistentDataContainer();
                nbt.set(this.pickup_action, PersistentDataType.INTEGER, PickupAction.SELECT_WORLD.ordinal());
                world_item.setItemMeta(meta);

                inventory.setItem((row+1) * 9 + 1 + col, world_item);
            }
        }
    }
    private void fillWorlds(Inventory inventory, List<String> worlds, List<String> ongoing){
        this.fillWorlds(inventory, worlds, ongoing,0);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.isCancelled()) return;
        World world = event.getWhoClicked().getWorld();
        if (world.getUID() != this.hub_id) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        int action = nbt.getOrDefault(this.pickup_action, PersistentDataType.INTEGER, -1);
        if (action == -1) return;
        event.setCancelled(true);

        PersistentDataContainer world_nbt = world.getPersistentDataContainer();
        List<String> runs = world_nbt.getOrDefault(this.run_worlds, PersistentDataType.LIST.strings(), new ArrayList<String>());
        List<String> ongoing = world_nbt.getOrDefault(this.ongoing_worlds, PersistentDataType.LIST.strings(), new ArrayList<String>());

        if (action == PickupAction.NEW_WORLD.ordinal()){
            Bukkit.getScheduler().runTask(this._plugin, () -> {
                event.getView().close();
                ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
                BookMeta book_meta = (BookMeta) book.getItemMeta();
                if (book_meta == null) {
                    Bukkit.getLogger().warning("Could not get book meta");
                    return;
                }
                book_meta.addPage("Whatever you sign this book as will be the name of the world. Choose responsibly.");
                PersistentDataContainer book_nbt = book_meta.getPersistentDataContainer();
                book_nbt.set(this.world_creator, PersistentDataType.BOOLEAN, true);
                book.setItemMeta(book_meta);

                event.getWhoClicked().getInventory().addItem(book);
            });
        } else if (action == PickupAction.CHANGE_PAGE.ordinal()) {
            PersistentDataContainer item_nbt = meta.getPersistentDataContainer();
            int next_offset = item_nbt.getOrDefault(this.next_offset, PersistentDataType.INTEGER, -1);
            assert next_offset != -1;
            this.fillWorlds(event.getInventory(), runs, ongoing, next_offset);
        } else if (action == PickupAction.SELECT_WORLD.ordinal()){
            String world_name = meta.getDisplayName();
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Joining world: '" + world_name + "'...");
            if (ongoing.contains(world_name)){
                player.getInventory().clear();
                player.setFoodLevel(20);
                player.setSaturation(5);
                player.setExp(0);
                player.setGameMode(GameMode.SURVIVAL);
            } else {
                player.setGameMode(GameMode.SPECTATOR);
            }

            Bukkit.getScheduler().runTask(this._plugin, () -> {
                World target_world = Bukkit.getWorld("attempt_"+world_name);
                if (target_world == null){
                    target_world = new WorldCreator("attempt"+world_name).createWorld();
                    assert target_world != null;
                }

                PersistentDataContainer player_nbt = player.getPersistentDataContainer();
                player_nbt.set(this.current_world, PersistentDataType.STRING, "attempt_" + world_name);
                player.setRespawnLocation(target_world.getSpawnLocation());
                player.teleport(target_world.getSpawnLocation());
            });
        }
    }

    @EventHandler
    public void createWorld(PlayerEditBookEvent event){
        if (event.isCancelled()) return;
        if (!event.isSigning()) return;

        BookMeta meta = event.getNewBookMeta();
        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        boolean world_creator = nbt.getOrDefault(this.world_creator, PersistentDataType.BOOLEAN, false);
        if (!world_creator) return;

        World world = event.getPlayer().getWorld();
        PersistentDataContainer world_nbt = world.getPersistentDataContainer();
        List<String> worlds = world_nbt.getOrDefault(this.run_worlds, PersistentDataType.LIST.strings(), new ArrayList<>());

        String title = meta.getTitle();
        if (title == null) return;
        if (worlds.contains(title)){
            event.getPlayer().sendMessage("A world with that name already exists.");
            event.setCancelled(true);
            return;
        }

        Bukkit.broadcastMessage("Creating new world: '" + title + "'...");

        event.getPlayer().getInventory().remove(Material.WRITABLE_BOOK);
        Bukkit.getScheduler().runTask(this._plugin, () -> {
            WorldCreator creator = new WorldCreator("attempt_" + title);
            World new_world = creator.createWorld();
            if (new_world == null){
                Bukkit.getLogger().warning("create world returned null.");
                return;
            }

            PersistentDataContainer new_world_nbt = new_world.getPersistentDataContainer();
            new_world_nbt.set(this.world_lives, PersistentDataType.INTEGER, 3);
            this._plugin.getConfig();
            Bukkit.broadcastMessage("World '" + title + "' has been created.");

            List<String> new_runs = new ArrayList<>(worlds);
            new_runs.add(title);

            List<String> ongoing = world_nbt.getOrDefault(this.ongoing_worlds, PersistentDataType.LIST.strings(), new ArrayList<>());
            List<String> new_ongoing = new ArrayList<>(ongoing);
            new_ongoing.add(title);

            world_nbt.set(this.run_worlds, PersistentDataType.LIST.strings(), new_runs);
            world_nbt.set(this.ongoing_worlds, PersistentDataType.LIST.strings(), new_ongoing);
        });
    }

    @EventHandler
    public void updatePlayerWorld(PlayerJoinEvent event){
        PersistentDataContainer player_nbt = event.getPlayer().getPersistentDataContainer();
        String world_name = player_nbt.get(this.current_world, PersistentDataType.STRING);
        if (world_name == null) return;
        Bukkit.getLogger().info(event.getPlayer().getWorld().getName() + ", " + world_name);

        World world = Bukkit.getWorld(world_name);
        if (world == null){
            world = new WorldCreator(world_name).createWorld();
            if (world == null){
                Bukkit.getLogger().warning("Could not create world '" + world_name + "' on player join");
                return;
            }
        }
        if (event.getPlayer().getWorld().getUID() != world.getUID()){
            Location correction = event.getPlayer().getLocation();
            correction.setWorld(world);
            event.getPlayer().teleport(correction);
        }
    }
}

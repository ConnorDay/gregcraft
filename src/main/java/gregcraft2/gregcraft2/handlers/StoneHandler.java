package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoneHandler implements Listener {
    private Gregcraft2 _plugin;
    private Set<Material> targetBlocks;
    public StoneHandler(Gregcraft2 plugin){
        this._plugin = plugin;
        this.targetBlocks = new HashSet<>();
        this.targetBlocks.add(Material.STONE);
        this.targetBlocks.add(Material.DEEPSLATE);
        this.targetBlocks.add(Material.ANDESITE);
        this.targetBlocks.add(Material.GRANITE);
        this.targetBlocks.add(Material.DIORITE);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void respawnStone(BlockBreakEvent event){
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Material type = block.getType();
        Location location = block.getLocation();
        World world = block.getWorld();
        if (!this.targetBlocks.contains(type)) return;
        if (location.getBlockY() > 30) return;

        //TODO: have some way of potentially preventing this
        Bukkit.getScheduler().runTaskLater(this._plugin, () -> {
            world.getBlockAt(location).setType(type);
        }, 20 * 30);
    }
}

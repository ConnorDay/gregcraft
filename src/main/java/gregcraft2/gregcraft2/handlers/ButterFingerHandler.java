package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ButterFingerHandler implements Listener {
    private Random random;
    private Gregcraft2 _plugin;
    private Map<UUID, Integer> tasks;
    public ButterFingerHandler(Gregcraft2 plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.random = new Random();
        this._plugin = plugin;
        this.tasks = new HashMap<>();
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Bukkit.getLogger().info("Started Butterfinger");
        Integer task_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this._plugin, () -> {
            PlayerInventory inventory = player.getInventory();
            if (inventory.getItemInMainHand().getAmount() == 0) return;

            player.sendMessage("Looks like you've got butter fingers ;)");
            player.dropItem(true);
            player.getInventory().setItemInMainHand(null);
        }, 0, 20 );
        this.tasks.put(player.getUniqueId(), task_id);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        Player player = event.getPlayer();
        int task_id = this.tasks.get(player.getUniqueId());
        Bukkit.getScheduler().cancelTask(task_id);
        Bukkit.getLogger().info("Cancelled");
    }
}

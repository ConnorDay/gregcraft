package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WetHandler implements Listener {
    private Map<UUID, Integer> dontRepeat;
    public WetHandler(Gregcraft2 plugin){
        this.dontRepeat = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Block block = player.getWorld().getBlockAt(player.getLocation());
                if (block.getType() == Material.WATER){
                    if (!this.dontRepeat.containsKey(player.getUniqueId())) {
                        Bukkit.broadcastMessage(player.getDisplayName() + " is wet!");
                    }
                    this.dontRepeat.put(player.getUniqueId(), 5);
                } else {
                    if (this.dontRepeat.containsKey(player.getUniqueId())){
                        int amount = this.dontRepeat.get(player.getUniqueId());
                        amount--;
                        if (amount == 0) {
                            this.dontRepeat.remove(player.getUniqueId());
                        }else{
                            this.dontRepeat.put(player.getUniqueId(), amount);
                        }
                    }
                }
            }
        },0, 5);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}

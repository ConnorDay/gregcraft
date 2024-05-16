package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SheepChaosHandler implements Listener {

    private Map<UUID, BukkitTask> watching;
    private Gregcraft2 _plugin;
    public SheepChaosHandler(Gregcraft2 plugin){
        this._plugin = plugin;
        this.watching = new HashMap();
        Bukkit.getPluginManager().registerEvents(this, plugin);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->{
            for (Player p : Bukkit.getOnlinePlayers()){
                this.onRecurringSheepCheck(p);
            }
        },0,5);
    }

    public void onRecurringSheepCheck(Player player){
        final int check_size = 8;
        List<Entity> entities = player.getNearbyEntities(check_size,check_size,check_size);
        for (Entity entity : entities){
            if (entity.getType() != EntityType.SHEEP) continue;
            Sheep sheep = (Sheep) entity;
            double distance = player.getLocation().distance(sheep.getLocation());
            if (distance <= 3.5){
                if (!this.watching.containsKey(sheep.getUniqueId())){
                    sheep.setVisualFire(true);
                    this.sheepExplode(sheep);
                }
            } else {
                if (this.watching.containsKey(sheep.getUniqueId())){
                    BukkitTask task = this.watching.get(sheep.getUniqueId());
                    task.cancel();
                    sheep.setVisualFire(false);
                    this.watching.remove(sheep.getUniqueId());
                }
            }
        }
    }

    private void sheepExplode(Sheep sheep){
        sheep.getWorld().playSound(sheep, Sound.ENTITY_CREEPER_PRIMED, 3.0f, 1.0f);
        BukkitTask task = Bukkit.getScheduler().runTaskLater(this._plugin,() -> {
            if (!sheep.isDead()){
                sheep.remove();
                sheep.getWorld().createExplosion( sheep.getLocation(), 3, false, true, sheep);
                this.watching.remove(sheep.getUniqueId());
            }
        }, 20 * 2);

        this.watching.put(sheep.getUniqueId(), task);
    }

    @EventHandler
    public void onSheepOrCreeperDeath(EntityDeathEvent event){
        boolean isCreeper = event.getEntityType() == EntityType.CREEPER;
        boolean isSheep = event.getEntityType() == EntityType.SHEEP;
        if (!isCreeper && !isSheep) return;
        if (isSheep) {
            event.getDrops().clear();
        } else if (isCreeper) {
            //TODO: Make this drop random colors of wool. Equal weight.
            event.getDrops().add(new ItemStack(Material.WHITE_WOOL));
        }
    }
}

package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.*;

public class HydraHandler implements Listener {
    private final Gregcraft2 _plugin;
    private Set<UUID> watching;
    public HydraHandler(Gregcraft2 plugin) {
        this._plugin = plugin;
        this.watching = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;

        Random random = new Random();
        Location deathSite = entity.getLocation();
        World world = entity.getWorld();

        if (random.nextInt(20) > -1){
            entity.setVelocity(new Vector(0,0,0));
            world.playSound(entity.getLocation(), Sound.ENTITY_WITHER_SPAWN, 3.0f, 1.0f);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, deathSite.add(0,1,0), 100);


            int toSpawn = random.nextInt(4) + 2;

            Vector direction = new Vector(1, 0, 0);
            Bukkit.getScheduler().runTaskLater(this._plugin, () -> {
                direction.rotateAroundY(random.nextDouble() * Math.PI * 2);
                for (int i = 0; i < toSpawn; i++) {
                    Entity mob = world.spawnEntity(deathSite, entity.getType());
                    Vector off = direction.rotateAroundY(Math.PI * 2 * i / toSpawn);
                    mob.setVelocity(off);
                }
            }, 20);
        }
    }
}

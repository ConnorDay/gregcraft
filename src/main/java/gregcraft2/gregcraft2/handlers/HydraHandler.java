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

            EntitySnapshot snapshot = entity.createSnapshot();
            EntityType type = entity.getType();

            int toSpawn = random.nextInt(4) + 2;

            Vector direction = new Vector(1, 0, 0);
            direction.rotateAroundY(random.nextDouble() * Math.PI * 2);

            List<Entity> corpses = new ArrayList<>();
            for (int i = 0; i < toSpawn; i++) {
                Entity mob = snapshot.createEntity(deathSite);
                corpses.add(mob);
                Vector off = direction.rotateAroundY(Math.PI * 2 * i / toSpawn);
                mob.setVelocity(off);
            }

            entity.remove();

            Bukkit.getScheduler().runTaskLater(this._plugin, () -> {
                List<Location> locations = new ArrayList<>();
                for (Entity corpse : corpses){
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, corpse.getLocation().add(0,1,0), 100);
                    locations.add(corpse.getLocation());
                    corpse.remove();
                }
                Bukkit.getScheduler().runTaskLater(this._plugin, () -> {
                    for (Location location : locations){
                        world.spawnEntity(location, type);
                    }
                }, 20);
            }, 20);
        }
    }
}

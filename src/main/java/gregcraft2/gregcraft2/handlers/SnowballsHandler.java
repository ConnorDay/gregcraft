package gregcraft2.gregcraft2.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class SnowballsHandler implements Listener {
    private Gregcraft2 _plugin;
    public SnowballsHandler(Gregcraft2 plugin) {
        this._plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void touchiesnow(PlayerMoveEvent event69){
        LivingEntity player69 = event69.getPlayer();
        //Bukkit.getLogger().info("Temperature: " + player69.getWorld().getTemperature(player69.getLocation().getBlockX(), player69.getLocation().getBlockY(), player69.getLocation().getBlockZ()) );
        if(player69.getWorld().getTemperature(player69.getLocation().getBlockX(), player69.getLocation().getBlockY(), player69.getLocation().getBlockZ()) <= 0.05){
            Random dice2 = new Random();
            Random dice = new Random();
            if(dice2.nextInt(1000)<=5){
                Bukkit.broadcastMessage("fegli");
                int direction = dice.nextInt(3);
                if(direction == 0){
                    Entity projectile11 = player69.getWorld().spawnEntity(player69.getLocation().add(10, 1.5 ,0), EntityType.SNOWBALL);
                    projectile11.setVelocity(new Vector(-1.5, 0, 0));
                    projectile11.setGravity(false);
                }
                else if(direction == 1){
                    Bukkit.broadcastMessage("fegliy");
                    Entity projectile12 = player69.getWorld().spawnEntity(player69.getLocation().add(0, 10 ,0), EntityType.SNOWBALL);
                    projectile12.setVelocity(new Vector(0, -1.5, 0));
                    projectile12.setGravity(false);
                }
                else if(direction == 2) {
                    Entity projectile11 = player69.getWorld().spawnEntity(player69.getLocation().add(0, 1.5, 10), EntityType.SNOWBALL);
                    projectile11.setVelocity(new Vector(0, 0, -1.5));
                    projectile11.setGravity(false);
                }
            }
        }
    }




    @EventHandler
    public void snowballhit(ProjectileHitEvent event){
        if (event.isCancelled()) return;
        Entity player = event.getHitEntity();
        Entity snowball = event.getEntity();
        Location player_location = player.getLocation();
        if(snowball.getType() == EntityType.SNOWBALL  &&  player.getType() == EntityType.PLAYER  &&  player.getWorld().getTemperature(player_location.getBlockX(), player_location.getBlockY(), player_location.getBlockZ()) <= 0.05){
            Bukkit.broadcastMessage(player.getName()+" HAS STARTED A SNOWBALL FIGHT");
            player.sendMessage("HELP I'M LOSING A SNOWBALL FIGHT");
            for(int x=1;x <= 10; x++) {
                Entity projectile1 = player.getWorld().spawnEntity(player_location.add(10, 0 ,0), EntityType.SNOWBALL);
                projectile1.setVelocity(new Vector(-1.5, 0, 0));
                projectile1.setGravity(false);
                Entity projectile2 = player.getWorld().spawnEntity(player_location.add(0, 10 ,0), EntityType.SNOWBALL);
                projectile2.setVelocity(new Vector(0, -1.5, 0));
                projectile2.setGravity(false);
                Entity projectile3 = player.getWorld().spawnEntity(player_location.add(0, 0 ,10), EntityType.SNOWBALL);
                projectile3.setVelocity(new Vector(0, 0, -1.5));
                projectile3.setGravity(false);
            }
        }
    }
}

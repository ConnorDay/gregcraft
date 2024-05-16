package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class DramaHandler implements Listener {
    public DramaHandler(Gregcraft2 plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        final int check_size = 3;
        for (Entity entity : event.getPlayer().getNearbyEntities(check_size,check_size,check_size)){
            if (entity.getType() != EntityType.PLAYER) continue;
            if (event.getPlayer().getLocation().distance(entity.getLocation()) > 1.25) continue;

            //Add a chance here
            Random random = new Random();
            if (random.nextInt(100) == 0) {
                event.getPlayer().attack(entity);
            }
        }
    }
}

package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class WorkTheFireHandler implements Listener {

    private Gregcraft2 _plugin;

    public WorkTheFireHandler(Gregcraft2 plugin){
        this._plugin=plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void levelup(PlayerLevelChangeEvent levels){
        Player player1 = levels.getPlayer();
        int Nlevel = levels.getNewLevel();
        int Olevel = levels.getOldLevel();
        Random dice5 = new Random();
        if(Nlevel > Olevel){
            for(int x = 0; x < Nlevel;x++)
            {
                int R = dice5.nextInt(255);
                int G = dice5.nextInt(255);
                int B = dice5.nextInt(255);
                int R1 = dice5.nextInt(255);
                int G1 = dice5.nextInt(255);
                int B1 = dice5.nextInt(255);
                FireworkEffect color = FireworkEffect.builder()
                        .withColor(Color.fromRGB(R,G,B))
                        .withTrail()
                        .withFlicker()
                        .withFade(Color.fromRGB(R1,G1,B1))
                        .build();
                Firework Boom = (Firework) player1.getWorld().spawnEntity(player1.getLocation(), EntityType.FIREWORK);
                FireworkMeta metadata = Boom.getFireworkMeta();
                metadata.addEffect(color);
                metadata.setPower(2);
                Boom.setFireworkMeta( metadata);
            }
        }
    }

}

package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.util.Vector;

public class FlyingPigsHandler implements Listener {
    public FlyingPigsHandler(Gregcraft2 plugin){

        Bukkit.getPluginManager().registerEvents(this, plugin);

        //TODO: Make pigs move around instead of just float

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (World world : Bukkit.getWorlds()){
                for (Pig pig : world.getEntitiesByClass(Pig.class)){
                    pig.setGravity(false);

                    Location pigLocation = pig.getLocation();
                    //TODO: update this logic to have variable desired height for the pig
                    Block beneathPig = world.getBlockAt(pigLocation.add(0,-1,0));
                    Block limitBlock = world.getBlockAt(pigLocation.add(0,-2,0));

                    Vector pigVelocity = pig.getVelocity();
                    boolean is_above_something = beneathPig.getType() != Material.AIR;
                    boolean is_above_passable = beneathPig.isPassable();
                    boolean is_above_liquid = beneathPig.isLiquid();

                    if ( (is_above_something && !is_above_passable) || is_above_liquid ){
                        pigVelocity.setY(0.15);
                    } else if (limitBlock.getType() == Material.AIR || limitBlock.isPassable()) {
                        pigVelocity.setY(-0.15);
                    } else {
                        pigVelocity.setY(0);
                    }
                    pig.setVelocity(pigVelocity);

                }
            }
        },0, 2);
    }
}

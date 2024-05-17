package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import java.util.Locale;
import java.util.Random;

public class PokieMaineHandler implements Listener {
    public PokieMaineHandler(Gregcraft2 plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void egghit(EntityDamageByEntityEvent event){
        //Bukkit.getLogger().info(""+event.getDamager().getType());
    if (event.getDamager().getType() == EntityType.EGG) {
        LivingEntity pokemon = (LivingEntity)event.getEntity();
        Random chance = new Random();
        String mobmob = pokemon.getName();
        Material pokeball = Material.getMaterial(mobmob.toUpperCase(Locale.ROOT)+"_SPAWN_EGG");
        if (pokeball == null) return;
        //pokemon.remove();
        if (chance.nextInt(100)>=60) {
            pokemon.remove();
            event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),new ItemStack(pokeball));
            return;
        }
        //event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(),event.getEntity().getType());
    }


    }
}

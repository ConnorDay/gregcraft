package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class DeathHandler implements Listener {
    private final Gregcraft2 _plugin;
    private final NamespacedKey last_death;
    public Set<UUID> killing_time;
    public DeathHandler(Gregcraft2 plugin){
        this._plugin = plugin;

        this.killing_time = new HashSet<>();

        this.last_death = new NamespacedKey(plugin, "last_death");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        //TODO: Find a way to cancel this if possible. Might be needed for the sky fight
        //honestly might be best if we have a flag on the world the player is in that says if it's a run or not
        //We'll have to only kill players in the same world as well.
        Player player = event.getEntity();
        World world = player.getWorld();
        if (this.killing_time.contains(world.getUID()) || event.getEntity().getGameMode() == GameMode.SPECTATOR) return;

        PersistentDataContainer world_nbt = world.getPersistentDataContainer();
        int default_lives = this._plugin.getConfig().getInt("worldLives");
        int lives = world_nbt.getOrDefault(this._plugin.world_lives, PersistentDataType.INTEGER, default_lives);
        lives--;

        if (lives <= 0) {
            event.getEntity().setGameMode(GameMode.SPECTATOR);
            lives = 0;
        }

        world_nbt.set(this._plugin.world_lives, PersistentDataType.INTEGER, lives);
        Date now = new Date();
        world_nbt.set(this.last_death, PersistentDataType.LONG, now.getTime());

        Bukkit.broadcastMessage( player.getDisplayName() + " has died. Lives remaining: " + lives );
        this.killing_time.add(world.getUID());
        for (Player to_kill : event.getEntity().getWorld().getPlayers()){
            if (to_kill.getGameMode() == GameMode.SPECTATOR) continue;

            to_kill.damage(Float.POSITIVE_INFINITY, player);
            if (lives <= 0){
                to_kill.setGameMode(GameMode.SPECTATOR);
            }
        }

        this.killing_time.remove(world.getUID());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinsFailedAttempt(PlayerJoinEvent event){
        Location location = event.getPlayer().getLocation();
        World world = location.getWorld();
        Bukkit.getLogger().info(event.getPlayer().getLastPlayed() + "");
        if (world == null) return;

        Player player = event.getPlayer();

        PersistentDataContainer world_nbt = world.getPersistentDataContainer();

        long last_death = world_nbt.getOrDefault(this.last_death, PersistentDataType.LONG, Long.MAX_VALUE);
        if (last_death < event.getPlayer().getLastPlayed()) return;

        Integer lives = world_nbt.get(this._plugin.world_lives, PersistentDataType.INTEGER);
        if ( lives == null ) return;

        if ( lives > 0 ) return;

        this.killing_time.add(world.getUID());
        player.setHealth(0);
        if (lives == 0) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        this.killing_time.remove(world.getUID());
    }
}

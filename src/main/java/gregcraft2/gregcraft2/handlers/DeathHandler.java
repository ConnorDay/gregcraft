package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathHandler implements Listener {
    private Gregcraft2 _plugin;
    public static boolean killing_time;
    public DeathHandler(Gregcraft2 plugin){
        this._plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        //TODO: Find a way to cancel this if possible. Might be needed for the sky fight
        //honestly might be best if we have a flag on the world the player is in that says if it's a run or not
        //We'll have to only kill players in the same world as well.
        if (DeathHandler.killing_time || event.getEntity().getGameMode() == GameMode.SPECTATOR) return;
        event.getEntity().setGameMode(GameMode.SPECTATOR);

        Bukkit.broadcastMessage("Someone has died. You have 5 seconds");
        Bukkit.getScheduler().runTaskLater(this._plugin, new Runnable() {
            @Override
            public void run() {
                DeathHandler.killing_time = true;
                for (Player player : Bukkit.getOnlinePlayers()){
                    //TODO: make the initial player kill everyone else?
                    //damageable.damage(float amount, Entity source);
                    player.setHealth(0);
                }
                DeathHandler.killing_time = false;
            }
        }, 100);
    }
}

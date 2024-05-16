package gregcraft2.gregcraft2;

import gregcraft2.gregcraft2.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gregcraft2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Fegli");

        new DeathHandler(this);
        new SheepChaosHandler(this);
        new DramaHandler(this);
        new FlyingPigsHandler(this);
        new HydraHandler(this);
        //TODO: Poll to drop item in hand
        new ButterFingerHandler(this);
        new WetHandler(this);
        //TODO: Go through portal random spot 100 away
        new PortalHandler(this);
        new StoneHandler(this);
        //TODO: Forget how to swim
        //TODO: big snowball to snowball fight
        //TODO: Skywars
        //TODO: Random trivia (with secret answers and shaming)
        //TODO: Leaderboard (for run or overall?)

        //TODO: regenerate world command
        //TODO: hub area
        //TODO: troll command
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting Down");
    }
}

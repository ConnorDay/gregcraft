package gregcraft2.gregcraft2;

import gregcraft2.gregcraft2.commands.HubCommand;
import gregcraft2.gregcraft2.commands.JoinWorldCommand;
import gregcraft2.gregcraft2.commands.TestCommand;
import gregcraft2.gregcraft2.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gregcraft2 extends JavaPlugin {
    public final NamespacedKey world_lives;
    public Gregcraft2(){
        super();
        this.world_lives = new NamespacedKey(this, "world_lives");
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Fegli");

        new DeathHandler(this);
        new SheepChaosHandler(this);
        new DramaHandler(this);
        new FlyingPigsHandler(this);
        new HydraHandler(this);
        new ButterFingerHandler(this);
        new WetHandler(this);
        //TODO: Go through portal random spot 100 away
        new PortalHandler(this);
        new StoneHandler(this);
        //TODO: Forget how to swim
        //TODO: Skywars
        //TODO: Random trivia (with secret answers and shaming)
        //TODO: Leaderboard (for run or overall?)

        //TODO: regenerate world command
        //TODO: hub area
        new HubHandler(this);
        //TODO: troll command
        //KYle:
        new PokieMaineHandler(this);

        new SnowballsHandler(this);

        //Commands
        new TestCommand(this);
        new HubCommand(this);
        new JoinWorldCommand(this);

        this.setupConfig();
    }

    private void setupConfig() {
        FileConfiguration config = this.getConfig();
        config.addDefault("worldLives", 3);

        config.options().copyDefaults();
        this.saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting Down");
    }
}

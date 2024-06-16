package gregcraft2.gregcraft2.commands;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinWorldCommand implements CommandExecutor {
    public JoinWorldCommand(Gregcraft2 plugin){
        plugin.getCommand("join").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (args.length != 1) return false;
        if (!(sender instanceof Player) ) return false;
        World target = Bukkit.getWorld(args[0]);
        if (target == null) return false;

        Player player = (Player) sender;
        player.teleport(target.getSpawnLocation());
        return true;
    }
}

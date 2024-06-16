package gregcraft2.gregcraft2.commands;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
    public TestCommand(Gregcraft2 plugin){
        plugin.getCommand("test").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Bukkit.getLogger().info("fegli");
        return true;
    }
}

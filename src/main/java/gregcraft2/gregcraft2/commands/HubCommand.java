package gregcraft2.gregcraft2.commands;

import gregcraft2.gregcraft2.Gregcraft2;
import gregcraft2.gregcraft2.handlers.HubHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HubCommand implements CommandExecutor {
    public HubCommand(Gregcraft2 plugin){
        plugin.getCommand("hub").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) ) return false;
        Player player = (Player) sender;
        World hub = Bukkit.getWorld("hub");
        if (hub == null) return false;
        player.setGameMode(GameMode.ADVENTURE);
        for (ItemStack item : player.getInventory().getContents()){
            if (item == null) continue;
            player.getWorld().dropItem(player.getLocation(), item);
        }
        player.getInventory().clear();
        player.teleport(hub.getSpawnLocation());
        return true;
    }
}

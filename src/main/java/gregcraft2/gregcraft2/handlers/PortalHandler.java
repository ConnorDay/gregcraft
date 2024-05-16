package gregcraft2.gregcraft2.handlers;

import gregcraft2.gregcraft2.Gregcraft2;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class PortalHandler implements Listener {
    public PortalHandler(Gregcraft2 plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}

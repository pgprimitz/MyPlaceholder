package dev.nutellim.MyPlaceholders.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        if (!containsPlaceholder(command)) return;

        event.setCommand(PlaceholderAPI.setPlaceholders(null, command));
    }

    private boolean containsPlaceholder(String command) {
        if (!command.contains("%myplaceholder_")
                && !command.contains("%mp_")
                && !command.contains("%mypl_")) return false;

        if (command.startsWith("papi") || command.startsWith("placeholderapi")) return false;

        return !command.startsWith("mp ") && !command.startsWith("myplaceholder ")
                && !command.startsWith("mypl ");
    }
}

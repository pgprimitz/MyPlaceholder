package dev.nutellim.MyPlaceholders.listeners;

import dev.nutellim.MyPlaceholders.utilities.BukkitUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerCommand(ServerCommandEvent event) {
        String resolved = resolvePlaceholders(event.getCommand());
        if (resolved != null) event.setCommand(resolved);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);
        if (!containsPlaceholder(command)) return;

        String resolved = PlaceholderAPI.setPlaceholders(event.getPlayer(), command);
        event.setMessage("/" + resolved);
    }

    private String resolvePlaceholders(String command) {
        if (!containsPlaceholder(command)) return null;

        Player player = BukkitUtil.getPlayerByArguments(command.split(" "));
        if (player == null) return null;

        return PlaceholderAPI.setPlaceholders(player, command);
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

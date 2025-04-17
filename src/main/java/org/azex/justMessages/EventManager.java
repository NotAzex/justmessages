package org.azex.justMessages;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {

    private final Utilities utilities;

    public EventManager(Utilities utilities) {
        this.utilities = utilities;
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        utilities.replies.entrySet().removeIf(entry -> player.equals(entry.getValue()));
        utilities.ignores.entrySet().removeIf(entry -> player.equals(entry.getValue()));
        utilities.replies.remove(player);
        utilities.toggleStatus.remove(player);
        utilities.ignores.remove(player);
    }

}

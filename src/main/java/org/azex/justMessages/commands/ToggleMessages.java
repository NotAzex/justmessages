package org.azex.justMessages.commands;

import org.azex.justMessages.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleMessages implements CommandExecutor {

    private final Utilities utilities;

    public ToggleMessages(Utilities utilities) {
        this.utilities = utilities;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (utilities.toggleStatus.containsKey(player)) {
            utilities.toggleStatus.remove(player);
            player.sendMessage(utilities.stringFromConfig("Other.EnabledMessages"));
            utilities.playSound("Sounds.EnabledMessages", player);
        } else {
            utilities.toggleStatus.put(player, true);
            player.sendMessage(utilities.stringFromConfig("Other.DisabledMessages"));
            utilities.playSound("Sounds.DisabledMessages", player);
        }
        return true;
    }
}

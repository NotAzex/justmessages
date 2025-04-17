package org.azex.justMessages.commands;

import org.azex.justMessages.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Ignoring implements CommandExecutor {

    private final Utilities utilities;

    public Ignoring(Utilities utilities) {
        this.utilities = utilities;
    }

    private void formatted(Player target, Player player, String path) {
        player.sendMessage(utilities.stringFromConfig(path).replaceText(builder -> builder
                .match("%target%")
                .replacement(target.getName()
                )));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(utilities.stringFromConfig("Other.UsedIgnoreWrong"));
            utilities.playSound("Sounds.Error", player);
            return false;

        }

        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage(utilities.stringFromConfig("Other.PlayerNotOnline"));
            utilities.playSound("Sounds.Error", player);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == player) {
            player.sendMessage(utilities.stringFromConfig("Other.CantIgnoreYourself"));
            utilities.playSound("Sounds.Error", player);
            return false;
        }

        utilities.ignores.putIfAbsent(player, new HashSet<>());
        Set<Player> ignored = utilities.ignores.get(player);

        if (ignored.contains(target)) {
            ignored.remove(target);
            formatted(target, player, "Other.UnignoredPlayer");
            utilities.playSound("Sounds.Unignored", player);
        } else {
            ignored.add(target);
            formatted(target, player, "Other.IgnoredPlayer");
            utilities.playSound("Sounds.Ignored", player);
        }

        return true;
    }
}

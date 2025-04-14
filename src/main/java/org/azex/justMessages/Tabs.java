package org.azex.justMessages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tabs implements TabCompleter {

    private List<String> players(String args) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        switch (command.getName()) {

            case "togglepm" -> {
                return Collections.emptyList();
            }

            case "justmessages" -> {
                if (args.length == 1) {
                    if (sender.hasPermission("justmessages.reload")) { return List.of("reload"); }
                }
                return Collections.emptyList();

            }
            case "msg" -> {
                if (args.length == 1) {
                    return players(args[0]);
                }
            }

        }

        return List.of("[<text>]");
    }
}

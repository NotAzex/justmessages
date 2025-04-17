package org.azex.justMessages.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.azex.justMessages.JustMessages;
import org.azex.justMessages.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Main implements CommandExecutor {

    private final JustMessages plugin;
    private final Messaging messaging;
    private final Utilities utilities;
    private final MiniMessage mini = Utilities.mini;
    private Component formatted;
    private final Component reloaded = mini.deserialize("<gold>✉ <yellow>The config has been reloaded!");

    public Main(JustMessages plugin, Messaging messaging, Utilities utilities) {
        this.utilities = utilities;
        this.messaging = messaging;
        this.plugin = plugin;
        buildComponent();
    }

    private void buildComponent() {
        Component link = Component.text("https://modrinth.com/project/messages\n")
                .color(TextColor.color(0x55FF55))
                .clickEvent(ClickEvent.openUrl("https://modrinth.com/project/messages"));
        Component message =
                mini.deserialize("\n<gold>✉ Just Messages [v" + plugin.getDescription().getVersion() + "]" +
                        "\n <gray>| <yellow>Just messages, nothing more.\n <gray>|<yellow> Available at <green>Modrinth<yellow>!\n" +
                        "<gray> | ");
        formatted = message.append(link);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 0) {
            if (args[0].equals("reload")) {
                if (!sender.hasPermission("justmessages.reload")) { return false; }
                plugin.reloadConfig();
                messaging.loadFormat();
                sender.sendMessage(reloaded);
                utilities.playSound("Sounds.Reloaded", player);
                return true;
            }
        }

        sender.sendMessage(formatted);
        utilities.playSound("Sounds.Info", player);
        return true;
    }
}

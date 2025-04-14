package org.azex.justMessages.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import org.azex.justMessages.JustMessages;
import org.azex.justMessages.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Messaging implements CommandExecutor {

    private final JustMessages plugin;
    private final Utilities utilities;
    private Component senderReply; // e.g 'you replied to X'
    private Component recipientReply; // e.g 'X replied to you'
    private Component senderMessage; // e.g 'you sent a message to X'
    private Component recipientMessage; // e.g 'X sent you a message'
    private final MiniMessage mini = Utilities.mini;

    public Messaging(JustMessages plugin, Utilities utilities) {
        this.utilities = utilities;
        this.plugin = plugin;
        loadFormat();
    }

    public void loadFormat() {
        FileConfiguration config = plugin.getConfig();
        String configRepliesSender = config.getString("Replies.Sender", "<red>Invalid Just Messages config.");
        String configSenderMessage = config.getString("Messages.Sender", "<red>Invalid Just Messages config.");
        String configRecipientReply = config.getString("Replies.Recipient", "<red>Invalid Just Messages config.");
        String configRecipientMessage = config.getString("Messages.Recipient", "<red>Invalid Just Messages config.");

        try {
            senderReply = mini.deserialize(configRepliesSender);
            recipientReply = mini.deserialize(configRecipientReply);
            senderMessage = mini.deserialize(configSenderMessage);
            recipientMessage = mini.deserialize(configRecipientMessage);
        } catch (ParsingExceptionImpl e) {
            plugin.getLogger().warning("Please don't use legacy color codes in the Just Messages config.");
        }
    }

    private Component replace(String sender, String recipient, String msg, Component two) {
        Component one = two
                .replaceText(builder -> builder
                        .match("%sender%")
                        .replacement(sender)
                )
                .replaceText(builder -> builder
                        .match("%recipient%")
                        .replacement(recipient)
                )
                .replaceText(builder -> builder
                        .match("%message%")
                        .replacement(msg));
        return one;
    }

    private void sendMessage(String type, Player sender, Player recipient, String msg) {
        if (sender != null && recipient != null) {

            String senderName = sender.getName();
            String recipientName = recipient.getName();

            if (type.equals("reply")) {
                Component newSender = replace(senderName, recipientName, msg, senderReply);
                Component newRecipient = replace(senderName, recipientName, msg, recipientReply);

                sender.sendMessage(newSender);
                recipient.sendMessage(newRecipient);

                utilities.playSound("Sounds.ReceivedReply", recipient);
                utilities.playSound("Sounds.Replied", sender);

                utilities.replies.put(recipient, sender);
            }

            if (type.equals("msg")) {
                Component newSender = replace(senderName, recipientName, msg, senderMessage);
                Component newRecipient = replace(senderName, recipientName, msg, recipientMessage);

                sender.sendMessage(newSender);
                recipient.sendMessage(newRecipient);

                utilities.playSound("Sounds.ReceivedMessage", recipient);
                utilities.playSound("Sounds.Messaged", sender);

                utilities.replies.put(recipient, sender);
                utilities.replies.put(sender, recipient);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String name = command.getName();

        if (!(commandSender instanceof Player sender)) {
            return false;
        }

        switch (name) {
            case "msg" -> {

                if (args.length < 2) {
                    sender.sendMessage(utilities.stringFromConfig("Other.UsedMsgWrong"));
                    utilities.playSound("Sounds.Error", sender);
                    return false;
                }

                Player recipient = Bukkit.getPlayer(args[0]);

                if (recipient == null) {
                    sender.sendMessage(utilities.stringFromConfig("Other.PlayerNotOnline"));
                    utilities.playSound("Sounds.Error", sender);
                    return false;
                }

                if (recipient == sender) {
                    sender.sendMessage(utilities.stringFromConfig("Other.CantSendMessageToYourself"));
                    utilities.playSound("Sounds.Error", sender);
                    return false;
                }

                String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                sendMessage("msg", sender, recipient, msg);
                return true;

            }
            case "reply" -> {

                if (args.length < 1) {
                    sender.sendMessage(utilities.stringFromConfig("Other.UsedReplyWrong"));
                    utilities.playSound("Sounds.Error", sender);
                    return false;
                }

                if (utilities.replies.get(sender) == null) {
                    sender.sendMessage(utilities.stringFromConfig("Other.NoOneToReplyTo"));
                    utilities.playSound("Sounds.Error", sender);
                    return false;
                }

                String msg = String.join(" ", args);
                sendMessage("reply", sender, utilities.replies.get(sender), msg);
                return true;

            }
        }
        return false;
    }
}

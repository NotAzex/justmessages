package org.azex.justMessages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Utilities {

    private JustMessages plugin;

    public Utilities(JustMessages plugin) {
        this.plugin = plugin;
    }

    public static MiniMessage mini = MiniMessage.miniMessage();
    public ConcurrentHashMap<Player, Boolean> toggleStatus = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Player, Player> replies = new ConcurrentHashMap<>();

    public void playSound(String path, Player player) {
        FileConfiguration config = plugin.getConfig();
        Sound sound = Sound.valueOf(config.getString(path + ".Sound", "BLOCK_NOTE_BLOCK_BASS"));
        float pitch = Float.valueOf(config.getString(path + ".Pitch", "1"));
        float volume = Float.valueOf(config.getString(path + ".Volume", "100"));
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public Component stringFromConfig(String path) {
        try {
            return mini.deserialize(plugin.getConfig().getString(path, "<red>Invalid value for option " + path + " in the config of JustMessages!"));
        } catch (ParsingExceptionImpl e) {
            plugin.getLogger().warning("Please don't use legacy color codes in the config of Just Messages!");
        }
        return mini.deserialize("<red>Invalid Just Messages config. Delete all legacy color codes from there!");
    }

}

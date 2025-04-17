package org.azex.justMessages;

import org.azex.justMessages.commands.Ignoring;
import org.azex.justMessages.commands.Main;
import org.azex.justMessages.commands.Messaging;
import org.azex.justMessages.commands.ToggleMessages;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustMessages extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Utilities utilities = new Utilities(this);
        Messaging messaging = new Messaging(this, utilities);
        Tabs tabs = new Tabs();
        try {
            getCommand("justmessages").setExecutor(new Main(this, messaging, utilities));
            getCommand("msg").setExecutor(messaging);
            getCommand("reply").setExecutor(messaging);
            getCommand("togglepm").setExecutor(new ToggleMessages(utilities));
            getCommand("ignore").setExecutor(new Ignoring(utilities));
            getCommand("msg").setTabCompleter(tabs);
            getCommand("reply").setTabCompleter(tabs);
            getCommand("justmessages").setTabCompleter(tabs);
            getCommand("ignore").setTabCompleter(tabs);
        } catch (NullPointerException e) {
            getLogger().warning("Failed to load commands or their tab completions.");
        }

        getServer().getPluginManager().registerEvents(new EventManager(utilities), this);

    }
}

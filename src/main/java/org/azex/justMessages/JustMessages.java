package org.azex.justMessages;

import org.azex.justMessages.commands.Main;
import org.azex.justMessages.commands.Messaging;
import org.azex.justMessages.commands.ToggleMessages;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustMessages extends JavaPlugin {

    private Messaging messaging;
    private Tabs tabs;
    private Utilities utilities;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        utilities = new Utilities(this);
        messaging = new Messaging(this, utilities);
        tabs = new Tabs();
        try {
            getCommand("justmessages").setExecutor(new Main(this, messaging, utilities));
            getCommand("msg").setExecutor(messaging);
            getCommand("reply").setExecutor(messaging);
            getCommand("togglepm").setExecutor(new ToggleMessages(utilities));
            getCommand("msg").setTabCompleter(tabs);
            getCommand("reply").setTabCompleter(tabs);
            getCommand("justmessages").setTabCompleter(tabs);
        } catch (NullPointerException e) {
            getLogger().warning("Failed to load some commands or their tab completions.");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

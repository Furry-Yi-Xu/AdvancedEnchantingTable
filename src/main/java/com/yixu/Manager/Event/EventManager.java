package com.yixu.Manager.Event;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EventManager {

    private final JavaPlugin plugin;

    public EventManager(JavaPlugin plugin) {
        this.plugin = plugin;
        registerEvents();
    }

    public void registerEvents() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

//        pluginManager.registerEvents(new PlayerInteract(), plugin);

    }

}

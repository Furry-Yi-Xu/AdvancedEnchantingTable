package com.yixu.Manager.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerFileConfigManager {

    private final JavaPlugin plugin;

    private final Map<String, FileConfiguration> configs = new HashMap<>();

    private static final String[] LOADED_FILES = {
            "config.yml",
            "message.yml",
            "enchant.yml",
            "gui.yml",
            "database.yml"
    };

    public ServerFileConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfigs(LOADED_FILES);
    }

    public void loadConfigs(String... fileNames) {
        for (String fileName : fileNames) {
            File file = new File(plugin.getDataFolder(), fileName);

            if (!file.exists()) {
                plugin.saveResource(fileName, false);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            configs.put(fileName, config);
        }
    }

    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    public void reloadAllConfigs() {
        configs.clear();
        loadConfigs(LOADED_FILES);
    }

}

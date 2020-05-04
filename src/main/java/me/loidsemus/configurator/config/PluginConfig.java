package me.loidsemus.configurator.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class PluginConfig {

    private final JavaPlugin plugin;
    private final File configFile;

    private FileConfiguration config;

    public PluginConfig(JavaPlugin plugin, File configFile) {
        this.plugin = plugin;
        this.configFile = configFile;
    }

    public void load() {
        if (!configFile.exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                plugin.getLogger().log(Level.SEVERE, "Could not create plugin folder");
                return;
            }
            plugin.saveResource(configFile.getName(), false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

}

package me.loidsemus.configurator.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.function.Consumer;

public class ConfigUtil {

    public static void loadConfigFileAsync(Plugin plugin, File file, Consumer<YamlConfiguration> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
           Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(configuration));
        });
    }

}

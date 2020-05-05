package me.loidsemus.configurator;

import me.loidsemus.configurator.commands.MainCommand;
import me.loidsemus.configurator.config.PluginConfig;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.metrics.MetricsLite;
import me.loidsemus.configurator.util.updatechecker.UpdateChecker;
import me.loidsemus.pluginlib.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class Configurator extends JavaPlugin {

    private PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        loadConfigAndMessages();

        registerCommands();

        new MetricsLite(this, 7408);
        UpdateChecker updateChecker = new UpdateChecker(this, 78334);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
                        updateChecker.getNewVersion(version -> {
                            getLogger().log(Level.WARNING, "There is a new version available on SpigotMC: " + version);
                            getLogger().log(Level.INFO, "Download at: https://www.spigotmc.org/resources/configurator.78334/");
                        }),
                0L, 60L * 30L * 20L);
    }

    public void loadConfigAndMessages() {
        pluginConfig = new PluginConfig(this, new File(getDataFolder(), "config.yml"));
        pluginConfig.load();

        String languageCode = pluginConfig.getConfig().getString("lang");
        if (languageCode == null) {
            getLogger().log(Level.WARNING, "A language code is not set in the config, falling back to default values");
            Messages.useDefaults();
            return;
        }


        if (!Messages.load(getDataFolder(), languageCode, LangKey.values(), LangKey.PREFIX)) {
            return;
        }


        getLogger().log(Level.INFO, "Loaded configs and messages (" + languageCode + ")");
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("configurator")).setExecutor(new MainCommand(this));
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}

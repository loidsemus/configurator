package me.loidsemus.configurator.util.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateChecker {

    private Plugin plugin;
    private int resourceId;

    private SemanticVersion currentVersion;

    public UpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;

        this.currentVersion = new SemanticVersion(plugin.getDescription().getVersion());
    }

    public void getNewVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    SemanticVersion newestVersion = new SemanticVersion(scanner.next());
                    if (newestVersion.compareTo(currentVersion) > 0) {
                        Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(newestVersion.getVersionString()));
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Could not check for updates: " + e.getMessage());
            }
        });
    }
}

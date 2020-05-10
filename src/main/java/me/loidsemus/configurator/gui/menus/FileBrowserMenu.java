package me.loidsemus.configurator.gui.menus;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import me.loidsemus.configurator.gui.ListMenu;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.util.ConfigUtil;
import me.loidsemus.configurator.util.ItemBuilder;
import me.loidsemus.pluginlib.Messages;
import org.apache.commons.io.comparator.DirectoryFileComparator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FileBrowserMenu extends ListMenu {

    private String currentPath;
    private final Gui parent;
    private final Plugin plugin;
    private final Plugin targetPlugin;

    public FileBrowserMenu(Gui parent, Plugin plugin, Plugin targetPlugin) {
        super(plugin, 6, Messages.get(LangKey.HEADER_FILE_BROWSER_MENU, false, targetPlugin.getName()));
        this.parent = parent;
        this.plugin = plugin;
        this.targetPlugin = targetPlugin;

        setCurrentPath(targetPlugin.getDataFolder().getPath());
    }

    private void loadCurrentPath() {
        File currentDirectory = new File(currentPath);
        List<GuiItem> items = new ArrayList<>();
        File[] files = currentDirectory.listFiles(file -> file.isDirectory() || file.getName().endsWith(".yml"));
        if (files == null) return;

        // Directories first
        Arrays.sort(files, DirectoryFileComparator.DIRECTORY_COMPARATOR);

        for (File file : files) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER);
            Consumer<InventoryClickEvent> consumer;
            if (file.isDirectory()) {
                builder.material(Material.BOOK);
                builder.displayname(Messages.get(LangKey.TYPE_DIRECTORY_TITLE, false, file.getName()));
                builder.lore(Messages.get(LangKey.TYPE_DIRECTORY, false));

                consumer = event -> {
                    event.setCancelled(true);
                    setCurrentPath(file.getPath());
                };
            }
            else {
                builder.displayname(Messages.get(LangKey.TYPE_CONFIG_FILE_TITLE, false, file.getName()));
                builder.lore(Messages.get(LangKey.TYPE_CONFIG_FILE, false));

                consumer = event -> {
                    event.setCancelled(true);
                    //p.sendMessage("Loading config: " + file.getPath());
                    ConfigUtil.loadConfigFileAsync(plugin, file, config -> {
                        ConfigMenu menu = new ConfigMenu(this, plugin, targetPlugin, file, config);
                        menu.setParent(this);
                        menu.show(event.getWhoClicked());
                    });
                };
            }
            GuiItem guiItem = new GuiItem(builder.build(), consumer);
            items.add(guiItem);
        }

        addItems(items);
        setBackButton();
    }

    private void setCurrentPath(String path) {
        clear();
        this.currentPath = path;
        loadCurrentPath();
        update();
    }

    private void setBackButton() {
        // Back button should go to parent GUI
        if (currentPath.equals(targetPlugin.getDataFolder().getPath())) {
            setParent(parent);
        }
        else {
            ItemStack item = new ItemBuilder(Material.FEATHER)
                    .displayname(Messages.get(LangKey.NAV_UP, false))
                    .build();
            GuiItem guiItem = new GuiItem(item, event -> {
                event.setCancelled(true);
                setCurrentPath(new File(currentPath).getParentFile().getPath());
            });

            getNavigatorPane().addItem(guiItem, 0, 0);
        }
    }
}

package me.loidsemus.configurator.gui.menus;

import com.github.stefvanschie.inventoryframework.GuiItem;
import me.loidsemus.configurator.Configurator;
import me.loidsemus.configurator.gui.ListMenu;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.messages.Messages;
import me.loidsemus.configurator.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginBrowserMenu extends ListMenu {

    private final Configurator plugin;

    public PluginBrowserMenu(Configurator plugin) {
        super(plugin, 6, Messages.get(LangKey.HEADER_PLUGIN_BROWSER_MENU, false));
        this.plugin = plugin;

        setup();
    }

    private void setup() {
        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();

        List<GuiItem> items = new ArrayList<>();

        for (Plugin pl : plugins) {
            if (!pl.getDataFolder().exists() || isBlacklisted(pl)) {
                continue;
            }

            ItemBuilder builder = new ItemBuilder(Material.EMERALD_BLOCK);

            if (pl.isEnabled()) {
                builder.displayname(Messages.get(LangKey.PLUGIN_NAME_ENABLED, false, pl.getName(), pl.getDescription().getVersion()));
            }
            else {
                builder.material(Material.REDSTONE_BLOCK);
                builder.displayname(Messages.get(LangKey.PLUGIN_NAME_DISABLED, false, pl.getName(), pl.getDescription().getVersion()));
            }

            GuiItem guiItem = new GuiItem(builder.build(), event -> {
                event.setCancelled(true);
                FileBrowserMenu menu = new FileBrowserMenu(this, plugin, pl);
                menu.setParent(this);
                menu.show(event.getWhoClicked());
            });
            items.add(guiItem);
        }

        addItems(items);
    }

    private boolean isBlacklisted(Plugin plugin) {
        List<String> blacklistedPlugins = this.plugin.getPluginConfig().getConfig().getStringList("blacklisted_plugins");
        return blacklistedPlugins.contains(plugin.getName());
    }
}

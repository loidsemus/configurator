package me.loidsemus.configurator.gui.menus;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import me.loidsemus.configurator.gui.Hierarchical;
import me.loidsemus.configurator.gui.ListMenu;
import me.loidsemus.configurator.gui.input.MenuConversationCanceller;
import me.loidsemus.configurator.gui.input.NumberPrompt;
import me.loidsemus.configurator.gui.input.TextPrompt;
import me.loidsemus.configurator.internal.ValueType;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.util.ItemBuilder;
import me.loidsemus.pluginlib.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Menu that displays a YAML configuration file
 */
public class ConfigMenu extends ListMenu implements Hierarchical {

    private final Gui parent;
    private final Plugin plugin;
    private final Plugin targetPlugin;
    private final YamlConfiguration config;
    private String currentPath;

    private boolean doSave = true;

    public ConfigMenu(Gui parent, Plugin plugin, Plugin targetPlugin, File file, YamlConfiguration config) {
        super(plugin, 6, Messages.get(LangKey.HEADER_CONFIG_MENU, false, targetPlugin.getName(), file.getName()));
        this.parent = parent;
        this.plugin = plugin;
        this.targetPlugin = targetPlugin;
        this.config = config;

        setOnClose(event -> {
            Player player = (Player) event.getPlayer();
            if (doSave) {
                player.sendMessage(Messages.get(LangKey.CONFIG_SAVING, true, targetPlugin.getName(), file.getName()));
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        config.save(file);
                        player.sendMessage(Messages.get(LangKey.CONFIG_SAVED_SUCCESS, true, targetPlugin.getName(), file.getName()));
                    } catch (IOException e) {
                        player.sendMessage(Messages.get(LangKey.ERROR_COULD_NOT_SAVE, true, targetPlugin.getName(), file.getName()));
                    }
                });
            }
        });

        setCurrentPath(config.getCurrentPath());
    }

    private void addConfigItems() {
        ConfigurationSection section = config.getConfigurationSection(currentPath);

        if (section == null) {
            return;
        }

        List<GuiItem> items = new ArrayList<>();
        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            ValueType type = ValueType.of(value);

            ItemBuilder itemBuilder = new ItemBuilder(type.getMaterial())
                    .displayname(Messages.get(LangKey.CONFIG_ITEM_TITLE, false, key))
                    .lore("&7Type: &r" + type.getName());

            if (type != ValueType.UNREADABLE && type != ValueType.SECTION) {
                itemBuilder.lore("&7Value: &r" + StringUtils.abbreviate(value.toString(), 65));
            }

            // Make item represent boolean state
            if (type == ValueType.BOOLEAN && (!(boolean) value)) {
                itemBuilder.material(Material.RED_WOOL);
            }

            items.add(new GuiItem(itemBuilder.build(), getOnClick(type, section, key)));
        }
        addItems(items);
    }

    private Consumer<InventoryClickEvent> getOnClick(ValueType type, ConfigurationSection section, String key) {
        Consumer<InventoryClickEvent> consumer;
        switch (type) {
            case SECTION:
                consumer = event -> {
                    event.setCancelled(true);
                    setCurrentPath(Objects.requireNonNull(section.getConfigurationSection(key)).getCurrentPath());
                };
                break;
            case STRING:
                consumer = event -> {
                    event.setCancelled(true);
                    doSave = false;
                    event.getWhoClicked().closeInventory();
                    doSave = true;
                    new ConversationFactory(plugin)
                            .withModality(true)
                            .withConversationCanceller(new MenuConversationCanceller(this))
                            .withFirstPrompt(new TextPrompt(input -> {
                                setValue(section, key, input);
                                show(event.getWhoClicked());
                            }))
                            .buildConversation((Player) event.getWhoClicked())
                            .begin();
                };
                break;
            case NUMBER:
                consumer = event -> {
                    event.setCancelled(true);
                    doSave = false;
                    event.getWhoClicked().closeInventory();
                    doSave = true;
                    new ConversationFactory(plugin)
                            .withModality(true)
                            .withConversationCanceller(new MenuConversationCanceller(this))
                            .withFirstPrompt(new NumberPrompt(input -> {
                                setValue(section, key, input);
                                show(event.getWhoClicked());
                            }))
                            .buildConversation((Player) event.getWhoClicked())
                            .begin();
                };
                break;
            case BOOLEAN:
                consumer = event -> {
                    event.setCancelled(true);
                    setValue(section, key, !section.getBoolean(key));
                };
                break;
            default:
                consumer = event -> event.setCancelled(true);

        }
        return consumer;
    }

    @Override
    public void reload() {
        int page = getPaginatedPane().getPage();
        clear();
        addConfigItems();
        getPaginatedPane().setPage(page);
        updateNavigator();
        update();
    }

    @Override
    public void loadCurrentPath() {
        addConfigItems();
        setBackButton();
    }

    @Override
    public void setCurrentPath(String path) {
        // For some reason this method calls the close event if this isn't done
        doSave = false;
        clear();
        this.currentPath = path;
        loadCurrentPath();
        update();
        doSave = true;
    }

    @Override
    public void setBackButton() {
        if (currentPath.equals("")) {
            setParent(parent);
        }
        else {
            ItemStack item = new ItemBuilder(Material.FEATHER)
                    .displayname(Messages.get(LangKey.NAV_UP, false))
                    .build();
            GuiItem guiItem = new GuiItem(item, event -> {
                event.setCancelled(true);

                ConfigurationSection currentSection = config.getConfigurationSection(currentPath);
                if (currentSection == null) return;

                ConfigurationSection parent = currentSection.getParent();
                if (parent == null) return;

                setCurrentPath(parent.getCurrentPath());
            });

            getNavigatorPane().addItem(guiItem, 0, 0);
        }
    }

    private void setValue(ConfigurationSection section, String key, Object value) {
        section.set(key, value);
        reload();
    }

}

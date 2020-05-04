package me.loidsemus.configurator.internal;

import me.loidsemus.configurator.gui.Hierarchical;
import me.loidsemus.configurator.gui.input.MenuConversationCanceller;
import me.loidsemus.configurator.gui.input.NumberPrompt;
import me.loidsemus.configurator.gui.input.TextPrompt;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.messages.Messages;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public enum ValueType {
    SECTION(Messages.get(LangKey.TYPE_SECTION, false), Material.BOOK),
    STRING(Messages.get(LangKey.TYPE_STRING, false), Material.PAPER),
    NUMBER(Messages.get(LangKey.TYPE_NUMBER, false), Material.DIAMOND),
    BOOLEAN(Messages.get(LangKey.TYPE_BOOLEAN, false), Material.GREEN_WOOL),
    UNREADABLE(Messages.get(LangKey.TYPE_UNREADABLE, false), Material.REDSTONE);

    private final String name;
    private Material material;

    ValueType(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public static ValueType of(Object object) {
        if (object instanceof ConfigurationSection) {
            return ValueType.SECTION;
        }
        else if (object instanceof String) {
            return ValueType.STRING;
        }
        else if (object instanceof Number) {
            return ValueType.NUMBER;
        }
        else if (object instanceof Boolean) {
            return ValueType.BOOLEAN;
        }
        else {
            return ValueType.UNREADABLE;
        }
    }
}

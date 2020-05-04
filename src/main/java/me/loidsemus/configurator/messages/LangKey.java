package me.loidsemus.configurator.messages;

import me.loidsemus.pluginlib.Translatable;

public enum LangKey implements Translatable {
    PREFIX("&c[Configurator]&r"),
    INSUFFICIENT_PERMISSION("&cInsufficient permission"),

    OPENING_GUI("Opening GUI"),
    HEADER_PLUGIN_BROWSER_MENU("Configurable plugins"),
    HEADER_FILE_BROWSER_MENU("{pluginName} > File browser", "pluginName"),
    HEADER_CONFIG_MENU("{pluginName} > {fileName}", "pluginName", "fileName"),

    PLUGIN_NAME_ENABLED("&a{name} &7({version})", "name", "version"),
    PLUGIN_NAME_DISABLED("&c{name} &7({version})", "name", "version"),

    NAV_BACK("&7Go back"),
    NAV_UP("&7Up one level"),
    NAV_NEXT_PAGE("&7Next page"),
    NAV_PREVIOUS_PAGE("&7Prev. page"),
    NAV_PAGE_INDICATOR("&7Page {current}/{max}", "current", "max"),

    TYPE_DIRECTORY("&7Directory"),
    TYPE_DIRECTORY_TITLE("&e{name}", "name"),
    TYPE_CONFIG_FILE("&7Config file"),
    TYPE_CONFIG_FILE_TITLE("&a{name}", "name"),

    CONFIG_ITEM_TITLE("&a{name}", "name"),

    TYPE_SECTION("&aSection"),
    TYPE_STRING("&aText"),
    TYPE_NUMBER("&aNumber"),
    TYPE_BOOLEAN("&aBoolean"),
    TYPE_UNREADABLE("&c&lUnreadable by Configurator"),

    PROMPT_ENTER_TEXT("Enter new &etext&r in chat, type &c&l!c&r to cancel"),
    PROMPT_ENTER_NUMBER("Enter new &enumber&r in chat, type &c&l!c&r to cancel"),
    PROMPT_FAILED_NUMBER_VALIDATION("&e{input} &cis not valid number input", "input"),
    PROMPT_CANCELED("Input cancelled"),

    CONFIG_SAVING("Saving {pluginName}/{fileName}...", "pluginName", "fileName"),
    CONFIG_SAVED_SUCCESS("{pluginName}/{fileName} saved successfully", "pluginName", "fileName"),
    ERROR_COULD_NOT_SAVE("&cCouldn't save file {pluginName}/{fileName}! Error logged to console", "pluginName", "fileName");

    private final String defaultValue;
    private final String[] args;

    LangKey(String defaultValue, String... args) {
        this.defaultValue = defaultValue;
        this.args = args;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String[] getArgs() {
        return args;
    }

    public String getKey() {
        return this.toString().toLowerCase();
    }
}

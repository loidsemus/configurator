package me.loidsemus.configurator.messages;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages {

    private static final Logger logger = Logger.getLogger(Messages.class.getName());
    private static final Properties properties = new Properties();

    private static boolean useDefaults = true;
    //private String prefixOffset;

    private Messages() {}

    public static void load(File langDirectory, String languageCode) throws FileNotFoundException {
        if(!langDirectory.exists()) {
            if(!langDirectory.mkdirs()) {
                logger.log(Level.WARNING, "Couldn't create language directory, falling back to default language values");
                useDefaults();
                return;
            }
        }
        properties.clear();
        saveDefaults(new File(langDirectory, "lang_default.properties"));

        if (languageCode.equalsIgnoreCase("default")) {
            useDefaults = true;
            //setPrefixOffset(LangKey.PREFIX.getDefaultValue());
            return;
        }
        useDefaults = false;

        File langFile = new File(langDirectory, "lang_" + languageCode + ".properties");
        FileInputStream inputStream = new FileInputStream(langFile);

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //setPrefixOffset(properties.getProperty("prefix"));

        List<String> missingKeys = new ArrayList<>();
        for (LangKey key : LangKey.values()) {
            if (!properties.containsKey(key.getKey())) {
                missingKeys.add(key.getKey());
            }
        }

        if (missingKeys.isEmpty()) {
            return;
        }

        logger.log(Level.WARNING,
                "lang_" + languageCode + ".properties is missing " + missingKeys.size() + " values: " +
                        String.join(", ", missingKeys) + "\n" +
                        "To fix this, copy the missing values from lang_default.properties to lang_" + languageCode + ".properties");
    }

    public static void useDefaults() {
        useDefaults = true;
    }

    /**
     * Saves the keys and values of {@link LangKey} to lang_default.properties, so a user can copy to their own file.
     */
    private static void saveDefaults(File file) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, false))) {

            pw.println("# DO NOT CHANGE THE CONTENTS OF THIS FILE\n" +
                    "# To change messages, copy the contents of this file and rename it to \"lang_<code>.properties\",\n" +
                    "# and change the \"language\" config value to the code you\n" +
                    "# used in the file name. Example: lang_es.properties or lang_custom.properties\n");

            for (LangKey key : LangKey.values()) {
                if (key.getArgs().length > 0) {
                    pw.println("# Placeholders: " + String.join(", ", key.getArgs()));
                }
                else {
                    pw.println("# No placeholders");
                }
                pw.println(key.getKey() + "=" + key.getDefaultValue() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*private void setPrefixOffset(String prefix) {
        if (isNotNullOrEmpty(prefix)) {
            StringBuilder prefixOffset = new StringBuilder(" ");
            String strippedPrefix = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', prefix));
            for (int i = 0; i < strippedPrefix.length(); i++) {
                prefixOffset.append(" ");
            }
            this.prefixOffset = prefixOffset.toString();
            return;
        }
        prefixOffset = "";
    }

    public String getPrefixOffset() {
        return prefixOffset;
    }*/

    public static String get(LangKey key, boolean prefix, String... args) {
        StringBuilder builder = new StringBuilder();

        if (useDefaults) {
            if (prefix) {
                builder.append(ChatColor.translateAlternateColorCodes('&', LangKey.PREFIX.getDefaultValue())).append(" ");
            }
            builder.append(ChatColor.translateAlternateColorCodes('&', key.getDefaultValue()));

        }
        else {

            if (prefix && isNotNullOrEmpty(properties.getProperty("prefix"))) {
                builder.append(ChatColor.translateAlternateColorCodes('&', properties.getProperty("prefix"))).append(" ");
            }

            if (isNotNullOrEmpty(properties.getProperty(key.getKey()))) {
                builder.append(ChatColor.translateAlternateColorCodes('&', properties.getProperty(key.getKey())));
            }
            else {
                builder.append(ChatColor.translateAlternateColorCodes('&', key.getDefaultValue()));
            }
        }

        String message = builder.toString();
        // Replace placeholders
        if (key.getArgs().length > 0) {
            int index = 0;
            for (String arg : args) {
                message = StringUtils.replace(message, "{" + key.getArgs()[index] + "}", arg);
                index++;
            }
        }
        return message;
    }

    private static boolean isNotNullOrEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}

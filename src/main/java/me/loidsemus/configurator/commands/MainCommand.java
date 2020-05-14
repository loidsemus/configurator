package me.loidsemus.configurator.commands;

import me.loidsemus.configurator.Configurator;
import me.loidsemus.configurator.gui.menus.PluginBrowserMenu;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.pluginlib.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final Configurator plugin;

    public MainCommand(Configurator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Handle reload stuff before console check, as it is the only command that console should be able to execute
        if (args.length >= 1 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) {
            if (!sender.hasPermission("configurator.reload")) {
                sender.sendMessage(Messages.get(LangKey.INSUFFICIENT_PERMISSION, true));
                return true;
            }

            plugin.loadConfigAndMessages();
            sender.sendMessage("Config and messages reloaded");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Consoles can't use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("configurator.use")) {
            player.sendMessage(Messages.get(LangKey.INSUFFICIENT_PERMISSION, true));
            return true;
        }

        player.sendMessage(Messages.get(LangKey.OPENING_GUI, true));
        if (player.hasPermission("configurator.write")) {
            player.sendMessage(Messages.get(LangKey.WRITE_ACCESS, true));
        }
        else {
            player.sendMessage(Messages.get(LangKey.READ_ONLY_MODE, true));
        }

        new PluginBrowserMenu(plugin).show(player);

        return true;
    }

}

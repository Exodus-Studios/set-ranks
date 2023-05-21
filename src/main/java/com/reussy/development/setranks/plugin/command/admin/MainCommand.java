package com.reussy.development.setranks.plugin.command.admin;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.ConfigManager;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainCommand extends BaseCommand {
    public MainCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!hasPermission(sender, "staffutilities.command.main")) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.NO_PERMISSION));
            return false;
        }

        if (args.length == 0) {
            if (!hasPermission(sender, "staffutilities.command.gui")) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.NO_PERMISSION));
                return false;
            }

        } else if (args.length == 1) {

            switch (args[0].toLowerCase()) {
                case "reload" -> {
                    if (!hasPermission(sender, "staffutilities.command.reload")) {
                        Utils.send(sender, plugin.getMessageManager().get(PluginMessages.NO_PERMISSION));
                        return false;
                    }

                    plugin.getConfigManagers().forEach(ConfigManager::reload);
                    Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLUGIN_RELOADED));
                }
                case "version" -> {
                    if (!hasPermission(sender, "staffutilities.command.version")) {
                        Utils.send(sender, plugin.getMessageManager().get(PluginMessages.NO_PERMISSION));
                        return false;
                    }

                    Utils.send(sender, "&r         &b&lSet Ranks Help");
                    Utils.send(sender, "&r");
                    Utils.send(sender, "&r &bVersion: &7" + plugin.getDescription().getVersion());
                    Utils.send(sender, "&r &bAuthor: &7" + "reussy (https://reussy.me)");
                }

                default -> Utils.send((Player) sender, plugin.getConfigManager().getList("messages", "help-message"));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("1")) {
                Utils.send(sender, plugin.getMessageManager().getList("messages", "help-message"), null);
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] args) {

        if (!hasPermission(sender, "staffutilities.command.main")) return List.of();

        if (args.length == 0) {
            return List.of("1", "2", "3");
        } else if (args.length == 1) {
            return List.of("main", "reload", "help", "version");
        }

        return List.of();
    }
}

package com.reussy.development.setranks.plugin.command.rank;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetRankCommand extends BaseCommand {
    public SetRankCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (sender instanceof Player) return false;

        if (args.length == 0) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_USAGE, false));
            return false;
        }

        return false;
    }
}

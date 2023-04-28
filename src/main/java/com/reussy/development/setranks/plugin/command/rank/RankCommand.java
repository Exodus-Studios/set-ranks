package com.reussy.development.setranks.plugin.command.rank;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.menu.type.user.SelectRankMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommand extends BaseCommand {
    public RankCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!commonChecks(sender, "setranks.command.rank")) return false;

        final Player player = (Player) sender;

        if (args.length == 0) {
            Utils.send(player, plugin.getMessageManager().get(PluginMessages.RANK_USAGE, false));
            return false;
        } else if (args.length == 1) {
            final OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(player, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            new SelectRankMenu(plugin, player, target).open(player);
            return true;
        }

        return true;
    }
}

package com.reussy.development.setranks.plugin.command.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearGrantHistoryCommand extends BaseCommand {
    public ClearGrantHistoryCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!hasPermission(sender, "setranks.command.cleargranthistory")) return false;

        if (args.length == 0) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.GRANT_CLEAR_HISTORY_USAGE, false));
            return false;
        } else if (args.length == 1) {
            final OfflinePlayer target = plugin.getServer().getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            // TODO: Método para eliminar el historial de grants de un jugador

        }

        return false;
    }
}

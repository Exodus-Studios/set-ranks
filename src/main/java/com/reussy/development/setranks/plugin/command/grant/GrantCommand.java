package com.reussy.development.setranks.plugin.command.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.model.group.Group;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GrantCommand extends BaseCommand {
    public GrantCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!hasPermission(sender, "setranks.command.grant")) return false;

        if (args.length < 3) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.GRANT_USAGE, false));
        } else {

            final OfflinePlayer target = plugin.getServer().getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            String groupName = args[1];
            Group group = plugin.getLuckPermsAPI().get().getGroupManager().getGroup(groupName);

            if (group == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_RANK_NOT_FOUND, false));
                return false;
            }

            StringBuilder builder = new StringBuilder();

            for (int i = 3; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
        }

        return false;
    }
}

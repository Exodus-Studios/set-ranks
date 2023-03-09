package com.reussy.development.setranks.plugin.command.rank;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SetRankCommand extends BaseCommand {
    public SetRankCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (sender instanceof Player){
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.ONLY_CONSOLE, false));
            return false;
        }

        if (args.length < 2) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_USAGE, false));
            return false;
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            Group group = plugin.getLuckPermsAPI().get().getGroupManager().getGroup(args[1]);

            if (group == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_RANK_NOT_FOUND));
                return false;
            }

            plugin.getGroupController().hasGroup(target.getUniqueId(), group.getName()).thenAcceptAsync(hasGroup -> {
                if (hasGroup) {
                    Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_ALREADY_IN_GROUP, false));
                } else {
                    if (args[2] != null){
                        if (Utils.runCommand(sender, "lp user " + target.getName() + " parent addTemp " + group.getName() + " " + args[2])) {
                            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_SUCCESSFUL, false),
                                    new String[][]{{"{PLAYER_NAME}", target.getName()},
                                            {"{GROUP_NAME}", group.getName()},
                                            {"{DURATION}", Utils.StringToDate(args[2]).toString()}});
                        }
                    } else {
                        if (Utils.runCommand(sender, "lp user " + target.getName() + " parent add " + group.getName())) {
                            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_SUCCESSFUL, false),
                                    new String[][]{{"{PLAYER_NAME}", target.getName()},
                                            {"{GROUP_NAME}", group.getName()},
                                            {"{DURATION}", "Permanent"}});
                        }
                    }
                }
            });
        }

        return false;
    }
}

package com.reussy.development.setranks.plugin.command.rank;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.PromotionResult;
import net.luckperms.api.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PromoteCommand extends BaseCommand {
    public PromoteCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!commonChecks(sender, "setranks.command.promote")) return false;

        final Player player = (Player) sender;

        if (args.length < 1) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PROMOTE_USAGE, false));
            return false;
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            User user = plugin.getLuckPermsAPI().get().getUserManager().getUser(target.getUniqueId());

            if (user == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            Group group = plugin.getGroupController().getGroupSync(user.getUniqueId());
            Group nextGroup = plugin.getGroupController().getNextGroup(group);

            if (nextGroup == null) {
                Utils.send(sender, plugin.getMessageManager().get(PluginMessages.SET_RANK_RANK_NOT_FOUND, false));
                return false;
            }

            plugin.getGroupController().hasGroup(target.getUniqueId(), nextGroup.getName()).thenAcceptAsync(hasGroup -> {
                if (hasGroup) {
                    Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PROMOTE_ALREADY_IN_GROUP, false));
                } else {
                    if (Utils.runCommand(sender, "lp user " + target.getName() + " parent set " + nextGroup.getName())) {
                        Utils.send(sender, plugin.getMessageManager().get(PluginMessages.PROMOTE_SUCCESSFUL, false),
                                new String[][]{{"{PLAYER_NAME}", target.getName()},
                                        {"{GROUP_NAME}", nextGroup.getName()},
                                        {"{DURATION}", "Permanent"}});
                    }
                }
            });

            return true;
        }
    }


    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        } else if (args.length == 2) {
            return plugin.getLuckPermsAPI().get().getTrackManager().getLoadedTracks().stream().map(Track::getName).toList();
        }

        return List.of();
    }
}

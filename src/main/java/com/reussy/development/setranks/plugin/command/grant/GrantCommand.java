package com.reussy.development.setranks.plugin.command.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.sql.entity.RoleHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.RoleTypeChange;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.UserTypeChange;
import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GrantCommand extends BaseCommand {
    public GrantCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!commonChecks(sender, "setranks.command.grant")) return false;

        final Player player = (Player) sender;

        if (args.length < 3) {
            Utils.send(player, plugin.getMessageManager().get(PluginMessages.GRANT_USAGE, false));
        } else {

            final OfflinePlayer target = plugin.getServer().getOfflinePlayerIfCached(args[0]);

            if (target == null) {
                Utils.send(player, plugin.getMessageManager().get(PluginMessages.PLAYER_NOT_FOUND, false));
                return false;
            }

            String groupName = args[1];
            Group group = plugin.getLuckPermsAPI().get().getGroupManager().getGroup(groupName);

            if (group == null) {
                Utils.send(player, plugin.getMessageManager().get(PluginMessages.SET_RANK_RANK_NOT_FOUND, false));
                return false;
            }

            String reason = Utils.getTextAsParameter(args, 2) == null ? "No reason provided." : Utils.getTextAsParameter(args, 2);

            plugin.getPluginScheduler().doAsync(() -> plugin.getQueryManager().insertUserHistory(new UserHistoryEntity(
                    target.getUniqueId(),
                    player.getUniqueId(),
                    UserTypeChange.GRANT,
                    groupName, // In this case, the field permission is used to store the group name.
                    new Date(),
                    reason
            )));
            Utils.send(player, plugin.getMessageManager().get(PluginMessages.GRANT_CREATED, false)
                    .replace("{PLAYER_NAME}", Objects.requireNonNull(target.getName())).replace("{GROUP_NAME}", group.getName()).replace("{REASON}", reason));
            return true;
        }

        return false;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        } else if (args.length == 2) {
            return plugin.getLuckPermsAPI().get().getGroupManager().getLoadedGroups().stream().map(Group::getName).toList();
        } else if (args.length == 3) {
            return List.of("<REASON>");
        }

        return List.of();
    }
}

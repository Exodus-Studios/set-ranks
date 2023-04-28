package com.reussy.development.setranks.plugin.command.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.UserTypeChange;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UnGrantCommand extends BaseCommand {
    public UnGrantCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!commonChecks(sender, "setranks.command.ungrant")) return false;

        final Player player = (Player) sender;

        if (args.length < 2) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.UNGRANT_USAGE, false));
        } else {
            try {
                long id = Long.parseLong(args[0]);

                String reason = Utils.getTextAsParameter(args, 1) == null ? "No reason provided." : Utils.getTextAsParameter(args, 1);

                plugin.getPluginScheduler().doAsync(() -> {

                    UserHistoryEntity userHistoryEntity = plugin.getQueryManager().getUserHistory(id);

                    if (userHistoryEntity == null) {
                        Utils.send(player, plugin.getMessageManager().get(PluginMessages.GRANT_NOT_FOUND, false));
                        return;
                    }

                    plugin.getQueryManager().insertUserHistory(
                            new UserHistoryEntity(
                                    userHistoryEntity.getUserChanged(),
                                    player.getUniqueId(),
                                    UserTypeChange.UNGRANT,
                                    userHistoryEntity.getRank(), // In this case, the field permission is used to store the group name.
                                    new Date(),
                                    reason
                            ));

                    plugin.getGroupController().hasGroup(userHistoryEntity.getUserChanged(), userHistoryEntity.getRank()).thenAcceptAsync(hasGroup -> {
                        if (hasGroup) {
                            Utils.runCommand(sender, "lp user " + userHistoryEntity.getUserChanged() + " parent remove " + userHistoryEntity.getRank());
                        }
                    });

                    Utils.send(player, plugin.getMessageManager().get(PluginMessages.UNGRANT_CREATED, false)
                            .replace("{GRANT_ID}", String.valueOf(id))
                            .replace("{PLAYER_NAME}", Objects.requireNonNull(Bukkit.getOfflinePlayer(userHistoryEntity.getUserChanged()).getName()))
                            .replace("{GROUP_NAME}", userHistoryEntity.getRank())
                            .replace("{REASON}", reason));
                });

            } catch (NumberFormatException e) {
                Utils.send(player, plugin.getMessageManager().get(PluginMessages.UNGRANT_USAGE, false));
            }
        }

        return false;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        List<String> ids = plugin.getQueryManager().getUsersHistory().stream().map(UserHistoryEntity::getId).map(String::valueOf).toList();
        if (args.length == 1) {
            return ids;
        } else if (args.length == 2) {
            return List.of("<REASON>");
        }

        return List.of();
    }
}

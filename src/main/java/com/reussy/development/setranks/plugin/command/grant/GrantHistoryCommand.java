package com.reussy.development.setranks.plugin.command.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.menu.type.grant.GrantHistoryMenu;
import com.reussy.development.setranks.plugin.sql.entity.RoleHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.RoleTypeChange;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.UserTypeChange;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Date;

public class GrantHistoryCommand extends BaseCommand {
    public GrantHistoryCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!commonChecks(sender, "setranks.command.granth")) return false;

        final Player player = (Player) sender;

        if (args.length == 0) {
            new GrantHistoryMenu(plugin, player).open(player);
        } else if (args.length == 1) {
            plugin.getQueryManager().insertUserHistory(new UserHistoryEntity(BigInteger.TEN, player.getUniqueId(), player.getUniqueId(), UserTypeChange.GRANT, "none", new Date(), "test"));
        }

        return false;
    }
}

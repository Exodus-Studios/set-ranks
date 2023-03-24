package com.reussy.development.setranks.plugin.command.user;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.command.BaseCommand;
import com.reussy.development.setranks.plugin.menu.type.rank.RankMenu;
import com.reussy.development.setranks.plugin.menu.type.user.UserMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserCommand extends BaseCommand {
    public UserCommand(String name, SetRanksPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (!commonChecks(sender, "setranks.command.user")) return false;

        final Player player = (Player) sender;

        if (args.length == 0){
            new UserMenu(plugin, player).open(player);
        }

        return true;
    }
}

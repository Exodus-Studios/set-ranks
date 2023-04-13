package com.reussy.development.setranks.plugin.command;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.utils.MinecraftVersion;
import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class to handle the commands.
 * <p>
 * This class is used to handle the commands.
 * The commands are registered in the {@link com.reussy.development.setranks.plugin.SetRanksPlugin#onEnable()} method.
 * Inheritance is used to create the commands and extends the methods to the child classes.
 * </p>
 */
public abstract class BaseCommand extends BukkitCommand {

    protected SetRanksPlugin plugin;

    protected BaseCommand(String name, SetRanksPlugin plugin) {
        super(name);
        this.plugin = plugin;
        registerDirectly();
    }

    @Override
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args);

    protected boolean registerDirectly() {
        try {
            Field commands = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commands.setAccessible(true);
            CommandMap commandMap = (CommandMap) commands.get(Bukkit.getServer());
            commandMap.register("", this);
            Utils.sendDebugMessage("The command " + this.getName() + " has been registered!");
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if the command sender is a player.
     *
     * @param sender The command sender.
     * @return True if the command sender is a player.
     */
    protected boolean isPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    /**
     * Check if the command sender has the permission.
     *
     * @param sender     The command sender.
     * @param permission The permission.
     * @return True if the command sender has the permission.
     */
    protected boolean hasPermission(@NotNull CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    /**
     * Check if the player has the permission.
     * If is operator, return true.
     *
     * @param player     The player.
     * @param permission The permission.
     * @return True if the player has the permission.
     */
    protected boolean hasPermission(@NotNull Player player, String permission) {
        if (permission == null || permission.isEmpty()) return true;
        if (player.isOp()) return true;
        return player.hasPermission(permission);
    }

    protected boolean commonChecks(@NotNull CommandSender sender, String permission) {
        if (!isPlayer(sender)) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.MUST_BE_PLAYER));
            return false;
        }

        if (!hasPermission(sender, permission)) {
            Utils.send(sender, plugin.getMessageManager().get(PluginMessages.NO_PERMISSION));
            return false;
        }
        return true;
    }

    protected OfflinePlayer getOffline(UUID uuid, String username) {

        if (Bukkit.getPlayer(uuid) != null) {
            return Bukkit.getPlayer(uuid);
        } else if (MinecraftVersion.getVersionNumber() >= 1_16_0) {
            if (Bukkit.getOfflinePlayerIfCached(username) != null) {
                return Bukkit.getOfflinePlayerIfCached(username);
            }
        }

        return Bukkit.getOfflinePlayer(uuid);
    }

    protected User getOnlineUser(@NotNull UUID uuid) {
        return plugin.getLuckPermsAPI().get().getUserManager().getUser(uuid);
    }

    protected CompletableFuture<User> getUser(UUID query) {
        return plugin.getLuckPermsAPI().get().getUserManager().loadUser(query);
    }

    /**
     * Get the player from the name.
     *
     * @param name name of player.
     * @return Player or null if the player is not found or connected.
     */

    protected Player getTarget(@NotNull String name) {
        return Bukkit.getPlayer(name);
    }

    /**
     * Get the player from the uuid.
     *
     * @param uuid UUID player.
     * @return Player or null if the player is not found or connected.
     */

    protected Player getTarget(@NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Get a collection of players with the permission
     * "setranks.staff".
     *
     * @return Collection updated with the players with the permission.
     */
    protected Collection<Player> getStaff() {
        List<Player> staff = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("setranks.staff")).forEach(staff::add);
        return staff;
    }

    protected Collection<Player> filterPlayersByPermission(@NotNull String permission) {
        List<Player> staff = new ArrayList<>();
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).forEach(staff::add);
        return staff;
    }

    /**
     * Return the suggestions ordered by the arguments
     * that being typed.
     *
     * @param args        The command arguments.
     * @param suggestions The command arguments.
     * @return The suggestions ordered.
     */
    protected List<String> suggestions(@NotNull String @NotNull [] args, List<String> suggestions) throws IllegalArgumentException {
        final List<String> arguments = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], suggestions, arguments);
        Collections.sort(arguments);
        return arguments;
    }
}

package com.reussy.development.setranks.plugin.utils;

import com.cryptomorin.xseries.XSound;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.exception.PluginErrorException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Utils {

    /* Clase con utilidades para el plugin */

    public static final SetRanksPlugin plugin = SetRanksPlugin.getPlugin(SetRanksPlugin.class);
    public static final String PLUGIN_NAME = plugin.getDescription().getName();
    private static final String SEPARATOR = "&e&m========================================================================";
    public static final String PREFIX = plugin.getConfigManager().get("messages", "prefix");

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendWarnMessage(String message) {
        Utils.sendConsoleMessage(SEPARATOR);
        Utils.sendConsoleMessage(ExodusMessageType.WARN.get() + message);
        Utils.sendConsoleMessage(SEPARATOR);
    }

    public static void sendErrorMessage(String message) {
        Utils.sendConsoleMessage(SEPARATOR);
        Utils.sendConsoleMessage("");
        Utils.sendConsoleMessage("");
        Utils.sendConsoleMessage(ExodusMessageType.ERROR.get() + message);
        Utils.sendConsoleMessage("");
        Utils.sendConsoleMessage("");
        Utils.sendConsoleMessage(SEPARATOR);
    }

    public static void sendDebugMessage(String message) {
        if (!plugin.getConfigManager().getBoolean("general", "debug")) return;
        Utils.sendConsoleMessage(ExodusMessageType.DEBUG.get() + message);
    }

    public static void disablePlugin() {
        if (plugin.getPluginStatus().isDisabling() || plugin.getPluginStatus().isDisabled()) return;

        plugin.getPluginStatus().setStatus(ExodusPluginStatus.DISABLING);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return IridiumColorAPI.process(message);
    }

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    public static @NotNull List<String> colorize(@NotNull List<String> message) {
        List<String> colorized = new ArrayList<>();
        message.forEach(line -> {
            line = IridiumColorAPI.process(line);
            colorized.add(line);
        });

        return colorized;
    }

    /**
     * Strip the colors in the message.
     *
     * @param message The message to strip.
     * @return returns the message without color.
     */
    public static String fade(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public static void send(Player player, String message) {
        if (message == null || message.isEmpty() || player == null) return;

        player.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to a command sender.
     *
     * @param sender  The command sender related.
     * @param message The message to send.
     */
    public static void send(CommandSender sender, String message) {
        if (message == null || message.isEmpty() || sender == null) return;

        sender.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public static void send(Player player, List<String> message) {
        if (message == null || message.isEmpty() || player == null) return;

        message.forEach(line -> send(player, line));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, String message) {
        if (message == null || message.isEmpty() || players.isEmpty()) return;

        players.forEach(player -> send(player, message));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, List<String> message) {
        if (message == null || message.isEmpty() || players.isEmpty()) return;

        players.forEach(player -> message.forEach(line -> send(player, line)));
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player       The player related.
     * @param message      The message to send.
     * @param placeholders The placeholders to replace.
     */
    public static void send(Player player, String message, String[][] placeholders) {
        if (message == null || message.isEmpty() || player == null) return;

        for (String[] placeholder : placeholders) {
            message = message.replace(placeholder[0], placeholder[1]);
        }

        player.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player       The player related.
     * @param message      The message to send.
     * @param placeholders The placeholders to replace.
     */
    public static void send(Player player, List<String> message, String[][] placeholders) {
        if (message == null || message.isEmpty() || player == null) return;

        for (String[] placeholder : placeholders) {
            message = message.stream().map(line -> line.replace(placeholder[0], placeholder[1])).collect(Collectors.toList());
        }

        message.forEach(line -> send(player, line));
    }

    /**
     * Send a colorized message to the command sender.
     *
     * @param sender       The sender of the command.
     * @param message      The message to send.
     * @param placeholders The placeholders to replace.
     */
    public static void send(CommandSender sender, List<String> message, String[][] placeholders) {
        if (message == null || message.isEmpty() || sender == null) return;

        for (String[] placeholder : placeholders) {
            message = message.stream().map(line -> line.replace(placeholder[0], placeholder[1])).collect(Collectors.toList());
        }

        message.forEach(line -> send(sender, line));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players      The players related.
     * @param message      The message to send.
     * @param placeholders The placeholders to replace.
     */
    public static void send(Collection<Player> players, String message, String[][] placeholders) {
        if (message == null || message.isEmpty()) return;

        for (String[] placeholder : placeholders) {
            message = message.replace(placeholder[0], placeholder[1]);
        }

        for (Player player : players) {
            send(player, message, placeholders);
        }
    }

    /**
     * Play a minecraft sound to the player.
     *
     * @param player The player related.
     * @param sound  The sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void play(Player player, String sound, float volume, float pitch) {

        if (sound == null || player == null) return;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a minecraft sound to the player list.
     *
     * @param players The collection of player's related.
     * @param sound   The sound to play.
     * @param volume  The volume of the sound.
     * @param pitch   The pitch of the sound.
     */
    public static void play(Collection<Player> players, String sound, float volume, float pitch) {

        if (sound == null) return;

        players.forEach(player -> play(player, sound, volume, pitch));
    }

    /**
     * Play a XSeries Sound parsed to a Minecraft Sound
     *
     * @param player The player related.
     * @param sound  The XSeries Sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void play(Player player, XSound sound, float volume, float pitch) {

        if (sound == null || player == null) return;

        sound.play(player, volume, pitch);
    }

    /**
     * Play a XSeries Sound parsed to a Minecraft Sound
     * This method follow the format SOUND:VOLUME:PITCH
     *
     * @param player The player related.
     * @param format The format of the sound.
     */
    public static void play(Player player, String format) {

        if (format == null || format.isEmpty() || format.equalsIgnoreCase("none")) return;

        String[] split = format.split(":");

        if (split.length != 3) {
            throw new PluginErrorException("The format for the sound " + format + " is invalid. The format must be SOUND:VOLUME:PITCH", new IndexOutOfBoundsException());
        }

        Optional<XSound> xSound = XSound.matchXSound(split[0]);
        float volume = split[1].length() == 0 ? 1 : Float.parseFloat(split[1]);
        float pitch = split[2].length() == 0 ? 1 : Float.parseFloat(split[2]);

        xSound.ifPresent(value -> play(player, value, volume, pitch));
    }

    public static Collection<Player> getFiltered(String permission) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).collect(Collectors.toList());
    }

    public static Date date(String dateString) {

        List<String> validDateFormats = List.of("y", "m", "w", "d", "h", "s");

        if (dateString == null || dateString.isEmpty() || !validDateFormats.contains(dateString)) return null;

        dateString = dateString.replaceAll("\\s", "");
        if (validDateFormats.stream().noneMatch(dateString.toLowerCase()::contains)) return null;

        List<String> dateFormats = new ArrayList<>();

        if (!Character.isDigit(dateString.charAt(0)) || Character.isDigit(dateString.length())) return null;

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < dateString.length(); i++) {
            char c = dateString.charAt(i);
            if (Character.isDigit(c)) {
                s.append(c);
            } else {
                if (i + 1 < dateString.length() && !Character.isDigit(dateString.charAt(i + 1))) return null;
                dateFormats.add(s.toString() + c);
                s = new StringBuilder();
            }
        }

        Calendar now = Calendar.getInstance();

        for (String format : dateFormats) {

            String number = format.replaceAll("[^0-9]", "");
            String type = format.replaceAll("[0-9]", "");

            if (!type.equals("m")) type = type.toUpperCase();

            switch (type) {
                case "Y" -> now.add(Calendar.YEAR, Integer.parseInt(number));
                case "M" -> now.add(Calendar.MONTH, Integer.parseInt(number));
                case "W" -> now.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(number));
                case "D" -> now.add(Calendar.DAY_OF_YEAR, Integer.parseInt(number));
                case "H" -> now.add(Calendar.HOUR_OF_DAY, Integer.parseInt(number));
                case "m" -> now.add(Calendar.MINUTE, Integer.parseInt(number));
                case "S" -> now.add(Calendar.SECOND, Integer.parseInt(number));
            }
        }

        return now.getTime();
    }
}

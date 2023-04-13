package com.reussy.development.setranks.plugin.menu.type.user;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class UserMenu extends BaseMenu {

    private final Player viewer;
    private final PaginatedGui paginatedGui;
    private String filter;

    public UserMenu(SetRanksPlugin plugin, Player viewer) {
        super(plugin, plugin.getUserMenuManager(), plugin.getUserMenuManager().get("user-menu", "title"), plugin.getUserMenuManager().getInt("user-menu", "rows"), true, plugin.getUserMenuManager().getInt("user-menu", "users-per-page"));

        this.viewer = viewer;
        this.filter = "ON";

        paginatedGui = Gui
                .paginated()
                .rows(rows)
                .title(Component.text(Utils.colorize(title)))
                .pageSize(pageSize)
                .create();
        setConfigManager(plugin.getUserMenuManager());
        populatePlayers();
    }

    /**
     * @return The {@link Gui} or {@link PaginatedGui} created in the constructor.
     */
    @Override
    public BaseGui menu() {
        return paginatedGui;
    }

    /**
     * Set the items in the menu.
     */
    @Override
    protected void setItems() {
        plugin.getElementBuilder().populateCustomItems(viewer, paginatedGui, getConfigManager(), getConfigManager().getSection("user-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        paginatedGui.setItem(getOfflinePlayersPosition(), ItemBuilder.from(getOfflinePlayersItem()).asGuiItem(event -> {

            if (filter.equals("OFF")) return;

            filter = "OFF";

            paginatedGui.clearPageItems();

            if (populatePlayers()) {
                paginatedGui.update();
            }
        }));

        paginatedGui.setItem(getAllPlayersPosition(), ItemBuilder.from(getAllPlayersItem()).asGuiItem(event -> {

            if (filter.equals("ALL")) return;

            filter = "ALL";

            paginatedGui.clearPageItems();

            if (populatePlayers()) {
                paginatedGui.update();
            }
        }));

        paginatedGui.setItem(getOnlinePlayersPosition(), ItemBuilder.from(getOnlinePlayersItem()).asGuiItem(event -> {

            if (filter.equals("ON")) return;

            filter = "ON";

            paginatedGui.clearPageItems();

            if (populatePlayers()) {
                paginatedGui.update();
            }
        }));
    }

    /**
     * Open the menu for the player passed.
     *
     * @param player The player to open the menu for.
     */
    @Override
    public void open(@NotNull HumanEntity... player) {
        setItems();
        Stream.of(player).forEach(paginatedGui::open);
    }

    private boolean populatePlayers() {

        Collection<OfflinePlayer> players = new ArrayList<>();

        if (filter.equalsIgnoreCase("ON")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (players.contains(player)) return;
                players.add(player);
            });
        } else if (filter.equalsIgnoreCase("OFF")) {
            Arrays.stream(Bukkit.getOfflinePlayers()).toList().forEach(player -> {
                if (player == null) return;
                if (players.contains(player.getPlayer())) return;
                players.add(player);
            });
        } else if (filter.equalsIgnoreCase("ALL")) {

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (players.contains(player)) return;
                players.add(player);
            });

            Arrays.stream(Bukkit.getOfflinePlayers()).toList().forEach(player -> {
                if (player == null) return;
                if (players.contains(player.getPlayer())) return;
                players.add(player);
            });
        }

        players.forEach(player -> paginatedGui.addItem(ItemBuilder.from(getPlayerItem(player)).asGuiItem(event -> new UserManagementMenu(plugin, viewer, player).open(viewer))));

        return true;
    }

    private ItemStack getPlayerItem(OfflinePlayer player) {
        return plugin.getElementBuilder().createFromSection(player, getConfigManager().getSection("user-menu.items.player-head-item"),
                new String[][]{{"{PLAYER_NAME}", player.getName() == null ? "Unknown" : player.getName()}});
    }

    private ItemStack getOfflinePlayersItem() {
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("user-menu.items.offline-players-item"));
    }

    private int getOfflinePlayersPosition() {
        return getConfigManager().getInt("user-menu.items.offline-players-item", "position");
    }

    private ItemStack getAllPlayersItem() {
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("user-menu.items.all-players-item"));
    }

    private int getAllPlayersPosition() {
        return getConfigManager().getInt("user-menu.items.all-players-item", "position");
    }

    private ItemStack getOnlinePlayersItem() {
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("user-menu.items.online-players-item"));
    }

    private int getOnlinePlayersPosition() {
        return getConfigManager().getInt("user-menu.items.online-players-item", "position");
    }

    private int getNextPosition() {
        return getConfigManager().getInt("user-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("user-menu", "previous-page-position");
    }
}

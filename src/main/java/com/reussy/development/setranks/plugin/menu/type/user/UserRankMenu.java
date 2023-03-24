package com.reussy.development.setranks.plugin.menu.type.user;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class UserRankMenu extends BaseMenu {

    private final Player manager;
    private final OfflinePlayer target;
    private final User user;
    private final Gui paginatedGui;
    public UserRankMenu(SetRanksPlugin plugin, Player manager, OfflinePlayer target, @NotNull User user) {
        super(plugin, plugin.getUserMenuManager(), plugin.getUserMenuManager().get("user-rank-menu", "title"), plugin.getUserMenuManager().getInt("user-rank-menu", "rows"), false, 1);

        this.manager = manager;
        this.target = target;
        this.user = user;

        paginatedGui = Gui
                .gui()
                .rows(rows)
                .title(Component.text(Utils.colorize(title.replace("{PLAYER_NAME}", Objects.requireNonNull(target.getName())))))
                .create();
        setConfigManager(plugin.getUserMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(target, paginatedGui, getConfigManager(), getConfigManager().getSection("user-rank-menu.custom-items"), null);


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
}

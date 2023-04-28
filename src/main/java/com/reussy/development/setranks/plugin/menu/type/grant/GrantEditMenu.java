package com.reussy.development.setranks.plugin.menu.type.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

public class GrantEditMenu extends BaseMenu {
    private final Player viewer;
    private final UserHistoryEntity userHistoryEntity;
    private final Gui gui;

    public GrantEditMenu(SetRanksPlugin plugin, Player viewer, UserHistoryEntity userHistoryEntity) {
        super(plugin, plugin.getEditGrantMenuManager().get("editor-menu", "title"), plugin.getEditGrantMenuManager().getInt("editor-menu", "rows"), false, 1);
        this.viewer = viewer;
        this.userHistoryEntity = userHistoryEntity;

        this.gui = Gui.gui()
                .title(Component.text(Utils.colorize(title.replace("{GRANT_ID}", String.valueOf(userHistoryEntity.getId())))))
                .rows(rows)
                .disableAllInteractions()
                .create();

        setConfigManager(plugin.getEditGrantMenuManager());
    }

    /**
     * @return The {@link Gui} or {@link PaginatedGui} created in the constructor.
     */
    @Override
    public BaseGui menu() {
        return gui;
    }

    /**
     * Set the items in the menu.
     */
    @Override
    protected void setItems() {
        plugin.getElementBuilder().populateCustomItems(viewer, gui, getConfigManager(), getConfigManager().getSection("editor-menu.custom-items"), placeholders(userHistoryEntity));
    }

    /**
     * Open the menu for the player passed.
     *
     * @param player The player to open the menu for.
     */
    @Override
    public void open(@NotNull HumanEntity... player) {
        setItems();
        Stream.of(player).forEach(gui::open);
    }

    @Contract("_ -> new")
    private String @NotNull [] @NotNull [] placeholders(@NotNull UserHistoryEntity userHistoryEntity) {
       return new String[][]{{"{GRANT_ID}", String.valueOf(userHistoryEntity.getId())},
                {"{USER_CHANGER}", getUsername(userHistoryEntity.getUserChanger())},
                {"{USER_CHANGED}", getUsername(userHistoryEntity.getUserChanged())},
                {"{RANK_NAME}", userHistoryEntity.getRank()},
                {"{GRANT_TYPE}", userHistoryEntity.getType().toString()},
                {"{GRANT_REASON}", userHistoryEntity.getReason()},
                {"{GRANT_DATE}", userHistoryEntity.getDate().toString()}};
    }

    private String getUsername(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}

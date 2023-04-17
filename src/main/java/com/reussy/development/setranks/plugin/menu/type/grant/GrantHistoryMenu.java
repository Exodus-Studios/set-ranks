package com.reussy.development.setranks.plugin.menu.type.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

public class GrantHistoryMenu extends BaseMenu {

    private final Player viewer;
    private final PaginatedGui paginatedGui;

    public GrantHistoryMenu(SetRanksPlugin plugin, Player viewer) {
        super(plugin, plugin.getGrantHistoryMenuManager().get("history-menu", "title"), plugin.getGrantHistoryMenuManager().getInt("history-menu", "rows"), true, plugin.getGrantHistoryMenuManager().getInt("history-menu", "grants-per-page"));
        this.viewer = viewer;

        this.paginatedGui = Gui.paginated()
                .title(Component.text(Utils.colorize(title)))
                .rows(rows)
                .pageSize(pageSize)
                .disableAllInteractions()
                .create();

        setConfigManager(plugin.getGrantHistoryMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(viewer, paginatedGui, getConfigManager(), getConfigManager().getSection("history-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        populateGrants();

    }

    /**
     * Open the menu for the player passed.
     *
     * @param player The player to open the menu for.
     */
    @Override
    public void open(@NotNull HumanEntity... player) {
        setItems();
        setItems();
        Stream.of(player).forEach(paginatedGui::open);
    }

    private void populateGrants() {
        plugin.getQueryManager().getUserHistoryList(viewer.getUniqueId()).forEach((entity -> paginatedGui.addItem(ItemBuilder.from(createGrantItem(entity)).asGuiItem(event -> {
            viewer.sendActionBar("test action bar " + entity.getId());
        }))));
    }

    private ItemStack createGrantItem(@NotNull UserHistoryEntity entity) {
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("history-menu.items.grant-item"),
                new String[][]{{"{GRANT_ID}", String.valueOf(entity.getId())},
                        {"{USER_CHANGER}", getUsername(entity.getUserChanger())},
                        {"{USER_CHANGED}", getUsername(entity.getUserChanged())},
                        {"{GRANT_TYPE}", entity.getType().toString()},
                        {"{GRANT_REASON}", entity.getReason()},
                        {"{GRANT_DATE}", entity.getDate().toString()}});
    }

    private int getNextPosition() {
        return getConfigManager().getInt("history-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("history-menu", "previous-page-position");
    }

    private String getUsername(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}

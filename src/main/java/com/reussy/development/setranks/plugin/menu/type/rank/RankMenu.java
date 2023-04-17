package com.reussy.development.setranks.plugin.menu.type.rank;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RankMenu extends BaseMenu {
    private final Player viewer;
    private final PaginatedGui paginatedGui;

    public RankMenu(SetRanksPlugin plugin, Player viewer) {
        super(plugin, plugin.getRankMenuManager(), plugin.getRankMenuManager().get("rank-menu", "title"), plugin.getRankMenuManager().getInt("rank-menu", "rows"), true, plugin.getRankMenuManager().getInt("rank-menu", "groups-per-page"));

        this.viewer = viewer;

        paginatedGui = Gui
                .paginated()
                .rows(rows)
                .title(Component.text(Utils.colorize(title)))
                .pageSize(pageSize)
                .disableAllInteractions()
                .create();

        setConfigManager(plugin.getRankMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(viewer, paginatedGui, getConfigManager(), getConfigManager().getSection("rank-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        populateGroups();
    }

    /**
     * Open the menu for the player passed.
     *
     * @param player The player to open the menu for.
     */
    @Override
    public void open(@NotNull HumanEntity... player) {
        setItems();
        Arrays.stream(player).forEach(paginatedGui::open);
    }

    private void populateGroups() {
        plugin.getLuckPermsAPI().get().getGroupManager().getLoadedGroups().forEach(group -> paginatedGui.addItem(ItemBuilder.from(createGroupItem(group)).asGuiItem()));
    }

    private ItemStack createGroupItem(@NotNull Group group) {
        return plugin.getElementBuilder().createFromSection(viewer, getConfigManager().getSection("rank-menu.items.group-item"),
                new String[][]{{"{GROUP_NAME}", group.getName()}});
    }

    private int getNextPosition() {
        return getConfigManager().getInt("rank-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("rank-menu", "previous-page-position");
    }
}

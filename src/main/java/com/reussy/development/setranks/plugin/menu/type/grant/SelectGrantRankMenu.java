package com.reussy.development.setranks.plugin.menu.type.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class SelectGrantRankMenu extends BaseMenu {

    private final Player viewer;
    private final OfflinePlayer granted;
    private final PaginatedGui paginatedGui;

    public SelectGrantRankMenu(SetRanksPlugin plugin, Player viewer, OfflinePlayer granted) {
        super(plugin, plugin.getGrantPlayerMenuManager().get("select-rank-menu", "title"), plugin.getGrantPlayerMenuManager().getInt("select-rank-menu", "rows"), true, plugin.getGrantPlayerMenuManager().getInt("select-rank-menu", "groups-per-page"));
        this.viewer = viewer;
        this.granted = granted;

        this.paginatedGui = Gui.paginated()
                .title(Component.text(Utils.colorize(title)))
                .rows(rows)
                .pageSize(pageSize)
                .disableAllInteractions()
                .create();

        setConfigManager(plugin.getGrantPlayerMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(granted, paginatedGui, getConfigManager(), getConfigManager().getSection("select-rank-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        plugin.getLuckPermsAPI().get().getGroupManager().getLoadedGroups().forEach(group -> paginatedGui.addItem(ItemBuilder.from(createRankItem(group)).asGuiItem(event -> new SelectGrantReasonMenu(plugin, viewer, granted, group).open(viewer))));
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

    private ItemStack createRankItem(@NotNull Group group) {
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("select-rank-menu.items.rank-item"),
                new String[][]{{"{RANK_NAME}", group.getName()},
                        {"{RANK_DISPLAY_NAME}", group.getDisplayName() == null ? "" : group.getDisplayName()}});
    }

    private int getNextPosition() {
        return getConfigManager().getInt("select-rank-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("select-rank-menu", "previous-page-position");
    }
}

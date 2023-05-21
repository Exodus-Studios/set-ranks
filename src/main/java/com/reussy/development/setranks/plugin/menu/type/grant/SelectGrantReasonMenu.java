package com.reussy.development.setranks.plugin.menu.type.grant;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.UserTypeChange;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

public class SelectGrantReasonMenu extends BaseMenu {
    private final Player viewer;
    private final OfflinePlayer granted;
    private final Group rank;
    private final PaginatedGui paginatedGui;
    public SelectGrantReasonMenu(SetRanksPlugin plugin, Player viewer, OfflinePlayer granted, Group rank) {
        super(plugin, plugin.getGrantPlayerMenuManager().get("select-reason-menu", "title"), plugin.getGrantPlayerMenuManager().getInt("select-reason-menu", "rows"), true, plugin.getGrantPlayerMenuManager().getInt("select-reason-menu", "reasons-per-page"));
        this.viewer = viewer;
        this.granted = granted;
        this.rank = rank;

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
        plugin.getElementBuilder().populateCustomItems(granted, paginatedGui, getConfigManager(), getConfigManager().getSection("select-rank-menu.custom-items"), new String[][]{{"{RANK_NAME}", rank.getName()}});
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        populateReasons();
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

    private void populateReasons(){
        for (String reason : getConfigManager().getSection("select-reason-menu.items").getKeys(false)){
            menu().addItem(ItemBuilder.from(createReasonItem(reason)).asGuiItem(event -> {
                plugin.getPluginScheduler().doAsync(() -> plugin.getQueryManager().insertUserHistory(new UserHistoryEntity(
                        granted.getUniqueId(),
                        viewer.getUniqueId(),
                        UserTypeChange.GRANT,
                        rank.getName(), // In this case, the field permission is used to store the group name.
                        new Date(),
                        getConfigManager().get("select-reason-menu.items." + reason, ".reason")
                )));
                Utils.send(viewer, plugin.getMessageManager().get(PluginMessages.GRANT_CREATED, false)
                        .replace("{PLAYER_NAME}", Objects.requireNonNull(granted.getName()))
                        .replace("{GROUP_NAME}", rank.getName())
                        .replace("{REASON}", getConfigManager().get("select-reason-menu.items." + reason, ".reason")));
                menu().close(viewer);
            }));
        }
    }

    private ItemStack createReasonItem(String reason){
        return plugin.getElementBuilder().createFromSection(granted, getConfigManager().getSection("select-reason-menu.items." + reason), null);
    }

    private int getNextPosition() {
        return getConfigManager().getInt("select-reason-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("select-reason-menu", "previous-page-position");
    }
}

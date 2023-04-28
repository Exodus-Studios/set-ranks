package com.reussy.development.setranks.plugin.menu.type.user;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class SelectRankMenu extends BaseMenu {

    private final Player manager;
    private final OfflinePlayer target;
    private final CompletableFuture<User> user;
    private final PaginatedGui paginatedGui;

    public SelectRankMenu(SetRanksPlugin plugin, Player manager, @NotNull OfflinePlayer target) {
        super(plugin, plugin.getUserRankMenuManager(), plugin.getUserRankMenuManager().get("select-rank-menu", "title"), plugin.getUserRankMenuManager().getInt("select-rank-menu", "rows"), true, plugin.getUserRankMenuManager().getInt("select-rank-menu", "ranks-per-page"));

        this.manager = manager;
        this.target = target;
        this.user = plugin.getLuckPermsAPI().get().getUserManager().loadUser(target.getUniqueId());

        paginatedGui = Gui
                .paginated()
                .rows(rows)
                .title(Component.text(Utils.colorize(title.replace("{PLAYER_NAME}", Objects.requireNonNull(target.getName())))))
                .pageSize(pageSize)
                .disableAllInteractions()
                .create();
        setConfigManager(plugin.getUserRankMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(target, paginatedGui, getConfigManager(), getConfigManager().getSection("select-rank-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        plugin.getLuckPermsAPI().get().getGroupManager().getLoadedGroups().forEach(group -> paginatedGui.addItem(ItemBuilder.from(createRankItem(group)).asGuiItem(event -> {
            try {
                new SelectTimeMenu(plugin, manager, target, user.get(), group).open(manager);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        })));
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

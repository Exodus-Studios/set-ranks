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

public class UserManagementMenu extends BaseMenu {

    private final Player manager;
    private final OfflinePlayer target;
    private final CompletableFuture<User> user;
    private final PaginatedGui paginatedGui;

    public UserManagementMenu(SetRanksPlugin plugin, Player manager, @NotNull OfflinePlayer target) {
        super(plugin, plugin.getUserMenuManager(), plugin.getUserMenuManager().get("user-management-menu", "title"), plugin.getUserMenuManager().getInt("user-management-menu", "rows"), true, plugin.getUserMenuManager().getInt("user-management-menu", "groups-per-page"));

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
        plugin.getElementBuilder().populateCustomItems(target, paginatedGui, getConfigManager(), getConfigManager().getSection("user-menu.custom-items"), null);
        plugin.getElementBuilder().setNavigationItems(paginatedGui, getNextPosition(), getPreviousPosition());

        plugin.getLuckPermsAPI().get().getGroupManager().getLoadedGroups().forEach(group -> paginatedGui.addItem(ItemBuilder.from(createRankItem(group)).asGuiItem(event -> {
            try {
                new UserRankMenu(plugin, manager, target, user.get(), group).open(manager);
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
        return plugin.getElementBuilder().createFromSection(getConfigManager().getSection("user-management-menu.items.rank-item"),
                new String[][]{{"{RANK_NAME}", group.getName()},
                        {"{RANK_DISPLAY_NAME}", group.getDisplayName() == null ? "" : group.getDisplayName()}});
    }

    private int getNextPosition() {
        return getConfigManager().getInt("user-menu", "next-page-position");
    }

    private int getPreviousPosition() {
        return getConfigManager().getInt("user-menu", "previous-page-position");
    }

    private @NotNull String permissionStatus(@NotNull User user, String permission) {
        PermissionNode node = PermissionNode.builder(permission).build();

        if (user.getNodes(NodeType.PERMISSION).contains(node)) {
            return "Already granted";
        } else {
            return "Not granted";
        }
    }

    private @NotNull String permissionClick(@NotNull User user, String permission) {
        if (permissionStatus(user, permission).equalsIgnoreCase("Already granted")) {
            return "Click to add";
        } else {
            return "Click to remove";
        }
    }
}

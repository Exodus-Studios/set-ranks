package com.reussy.development.setranks.plugin.menu.type.user;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.config.PluginMessages;
import com.reussy.development.setranks.plugin.exceptions.PluginErrorException;
import com.reussy.development.setranks.plugin.menu.BaseMenu;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

public class SelectTimeMenu extends BaseMenu {

    private final Player manager;
    private final OfflinePlayer target;
    private final User user;
    private final Group group;
    private final Gui gui;
    private Calendar calendar;
    private Date date;

    public SelectTimeMenu(SetRanksPlugin plugin, Player manager, @NotNull OfflinePlayer target, @NotNull User user, @NotNull Group group) {
        super(plugin, plugin.getUserRankMenuManager(), plugin.getUserRankMenuManager().get("select-time-menu", "title"), plugin.getUserRankMenuManager().getInt("select-time-menu", "rows"), false, 1);

        this.manager = manager;
        this.target = target;
        this.user = user;
        this.group = group;
        this.date = new Date();
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);

        gui = Gui
                .gui()
                .rows(rows)
                .title(Component.text(Utils.colorize(title.replace("{PLAYER_NAME}", Objects.requireNonNull(target.getName())).replace("{RANK_NAME}", group.getName()))))
                .disableAllInteractions()
                .create();
        setConfigManager(plugin.getUserRankMenuManager());
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
        plugin.getElementBuilder().populateCustomItems(target, gui, getConfigManager(), getConfigManager().getSection("select-time-menu.custom-items"), null);

        setItem(getBackPosition(), ItemBuilder.from(plugin.getElementBuilder().getBackItem()).asGuiItem(event -> new SelectRankMenu(plugin, manager, target).open(event.getWhoClicked())));

        populateTimes();

        setItem(getPermanentPosition(), ItemBuilder.from(createPermanentItem()).asGuiItem(event -> {
            if (Utils.runCommand(manager, "lp user " + target.getName() + " parent set " + group.getName())) {
                Utils.send(manager, plugin.getMessageManager().get(PluginMessages.SET_RANK_SUCCESSFUL, false),
                        new String[][]{{"{PLAYER_NAME}", target.getName()},
                                {"{GROUP_NAME}", group.getName()},
                                {"{DURATION}", "Permanent"}});
                gui.close(manager);
            }
        }));

        setItem(getGiveRankPosition(), ItemBuilder.from(createGiveRankItem()).asGuiItem(event -> {

            if (Utils.runCommand(manager, "lp user " + target.getName() + " parent addtemp " + group.getName() + " " + Utils.calculateTime(date.getTime() - System.currentTimeMillis()).replace(" ", ""))) {
                Utils.send(manager, plugin.getMessageManager().get(PluginMessages.SET_RANK_SUCCESSFUL, false),
                        new String[][]{{"{PLAYER_NAME}", target.getName()},
                                {"{GROUP_NAME}", group.getName()},
                                {"{DURATION}", Utils.calculateTime(date.getTime() - System.currentTimeMillis())}});
                gui.close(manager);
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
        Stream.of(player).forEach(gui::open);
    }

    private void populateTimes() {
        for (String time : getConfigManager().getSection("select-time-menu.items").getKeys(false)) {
            setItem(getPosition(time), ItemBuilder.from(createTimeItem(time)).asGuiItem(event -> {
                if (!modify(number(getConfigManager().get("select-time-menu.items." + time, "time")), unit(getConfigManager().get("select-time-menu.items." + time, "time")))) {
                    gui.updateTitle(Utils.colorize(getConfigManager().get("select-time-menu", "wrong-date-title").replace("{PLAYER_NAME}", target.getName()).replace("{RANK_NAME}", group.getName())));

                    plugin.getPluginScheduler().doSyncLater(() -> gui.updateTitle(Utils.colorize(getTitle().replace("{PLAYER_NAME}", target.getName()).replace("{RANK_NAME}", group.getName()))), 35L);
                }
                updateTimes();
            }));
        }
    }

    private void updateTimes() {
        for (String time : getConfigManager().getSection("select-time-menu.items").getKeys(false)) {
            gui.updateItem(getPosition(time), createTimeItem(time));
        }
    }

    private ItemStack createTimeItem(@NotNull String reason) {
        return plugin.getElementBuilder()
                .createFromSection(target.getName(), getConfigManager().getSection("select-time-menu.items." + reason),
                        new String[][]{{"{CURRENT_TIME}", Utils.calculateTime(date.getTime() - System.currentTimeMillis())}});
    }

    private int getPosition(String path) {
        return Integer.parseInt(getConfigManager().get("select-time-menu.items." + path, ".position"));
    }

    private ItemStack createPermanentItem() {
        return plugin.getElementBuilder()
                .createFromSection(target.getName(), getConfigManager().getSection("select-time-menu.items.permanent-rank-item"),
                        new String[][]{{"{CURRENT_TIME}", Utils.calculateTime(date.getTime() - System.currentTimeMillis())}});
    }

    private int getPermanentPosition() {
        return Integer.parseInt(getConfigManager().get("select-time-menu.items.permanent-rank-item", "position"));
    }

    private ItemStack createGiveRankItem() {
        return plugin.getElementBuilder()
                .createFromSection(target.getName(), getConfigManager().getSection("select-time-menu.items.give-rank-item"),
                        new String[][]{{"{CURRENT_TIME}", Utils.calculateTime(date.getTime() - System.currentTimeMillis())}});
    }

    private int getGiveRankPosition() {
        return getConfigManager().getInt("select-time-menu.items.give-rank-item", "position");
    }

    private int getBackPosition() {
        return getConfigManager().getInt("select-time-menu", "back-position");
    }

    private int number(@NotNull String time) {

        String[] split = time.split(":");

        if (split.length != 2) {
            throw new PluginErrorException("The time must be in the format of 'NUMBER:UNIT'", new IllegalArgumentException());
        }

        return Integer.parseInt(split[0]);
    }

    private String unit(@NotNull String time) {

        String[] split = time.split(":");

        if (split.length != 2) {
            throw new PluginErrorException("The time must be in the format of 'NUMBER:UNIT'", new IllegalArgumentException());
        } else {
            return split[1];
        }
    }

    private boolean modify(int time, @NotNull String type) {

        if (type.equalsIgnoreCase("day")) {
            calendar.add(Calendar.DAY_OF_YEAR, time);
        } else if (type.equalsIgnoreCase("hour")) {
            calendar.add(Calendar.HOUR_OF_DAY, time);
        } else if (type.equalsIgnoreCase("minute")) {
            calendar.add(Calendar.MINUTE, time);
        } else if (type.equalsIgnoreCase("second")) {
            calendar.add(Calendar.SECOND, time);
        } else if (type.equalsIgnoreCase("week")) {
            calendar.add(Calendar.WEEK_OF_MONTH, time);
        } else if (type.equalsIgnoreCase("month")) {
            calendar.add(Calendar.MONTH, time);
        } else if (type.equalsIgnoreCase("year")) {
            calendar.add(Calendar.YEAR, time);
        } else {
            throw new PluginErrorException("The time must be in the format of 'NUMBER:UNIT'", new IllegalArgumentException());
        }

        if (calendar.getTime().before(new Date())) {
            date = new Date();
            calendar = Calendar.getInstance();
            return false;
        }

        date = calendar.getTime();
        return true;
    }
}

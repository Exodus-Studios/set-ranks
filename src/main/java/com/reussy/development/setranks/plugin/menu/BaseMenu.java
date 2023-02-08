package com.reussy.development.setranks.plugin.menu;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.config.ConfigManager;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMenu {

    protected final SetRanksPlugin plugin;
    protected ConfigManager configManager;
    protected String title;
    protected int rows;
    protected boolean paginated;
    protected int pageSize;

    public BaseMenu(SetRanksPlugin plugin) {
        this.plugin = plugin;
    }

    public BaseMenu(SetRanksPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public BaseMenu(SetRanksPlugin plugin, ConfigManager configManager, String title, int rows, boolean paginated, int pageSize) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.title = title;
        this.rows = rows;
        this.paginated = paginated;
        this.pageSize = pageSize;
    }

    public BaseMenu(SetRanksPlugin plugin, String title, int rows, boolean paginated, int pageSize) {
        this.plugin = plugin;
        this.title = title;
        this.rows = rows;
        this.paginated = paginated;
        this.pageSize = pageSize;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    protected String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    protected boolean isPaginated() {
        return paginated;
    }

    public void setPaginated(boolean paginated) {
        this.paginated = paginated;
    }

    /**
     * @return The {@link Gui} or {@link PaginatedGui} created in the constructor.
     */
    public abstract BaseGui menu();

    /**
     * Set the items in the menu.
     */
    protected abstract void setItems();

    protected void setItem(int slot, GuiItem itemStack) {

        if (itemStack == null || slot < 0) return;

        if (slot > 0 && slot <= rows * 9) {
            menu().setItem(slot, itemStack);
        }
    }

    protected void setItem(int slot, GuiItem itemStack, boolean update) {

        setItem(slot, itemStack);

        if (update) {
            menu().updateItem(slot, itemStack);
        }

        menu().update();
    }

    protected void updateItem(int slot, GuiItem itemStack) {

        if (itemStack == null || slot < 0) return;

        if (slot > 0 && slot <= rows * 9) {
            menu().updateItem(slot, itemStack);
        }
    }

    /**
     * Open the menu for the player passed.
     *
     * @param player The player to open the menu for.
     */
    public abstract void open(@NotNull HumanEntity... player);

}

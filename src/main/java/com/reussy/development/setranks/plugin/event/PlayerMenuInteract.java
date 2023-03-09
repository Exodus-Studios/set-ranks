package com.reussy.development.setranks.plugin.event;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.ScrollingGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerMenuInteract implements Listener {

    /*
     * Cancel the event if the player is in a menu
     * of the Staff Facilities plugin
     */
    @EventHandler
    public void onInteract(@NotNull InventoryClickEvent event) {

        if (event.getInventory().getHolder() instanceof Gui
                || event.getInventory().getHolder() instanceof PaginatedGui
                || event.getInventory().getHolder() instanceof ScrollingGui) {
            event.setCancelled(true);
        }
    }

}

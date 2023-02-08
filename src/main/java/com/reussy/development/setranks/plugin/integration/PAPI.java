package com.reussy.development.setranks.plugin.integration;

import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.Bukkit;

public class PAPI implements IPluginIntegration {

    /**
     * @return true If this plugin was hooked successfully, otherwise false.
     */
    @Override
    public boolean isRunning() {
        return isPresent() && isEnabled();
    }

    /**
     * @return true If the plugin is present, otherwise false.
     */
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * @return true If the plugin is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Enable and instance the plugin.
     */
    @Override
    public boolean enable() {
        if (isPresent()) {
            Utils.sendConsoleMessage("&3PlaceholderAPI &ahas been enabled and hooked into Set-Ranks.");
            return true;
        } else {
            Utils.sendConsoleMessage("&cPlaceholderAPI is not present, please install it to enable PlaceholderAPI support.");
            return false;
        }
    }

    /**
     * Disable the instance of the plugin.
     */
    @Override
    public void disable() {

    }
}

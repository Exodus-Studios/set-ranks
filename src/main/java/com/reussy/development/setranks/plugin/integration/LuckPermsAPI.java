package com.reussy.development.setranks.plugin.integration;

import com.reussy.development.setranks.plugin.utils.Utils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsAPI implements IPluginIntegration {

    RegisteredServiceProvider<LuckPerms> provider;

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
        return Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
    }

    /**
     * @return true If the plugin is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    /**
     * Enable and instance the plugin.
     */
    @Override
    public boolean enable() {
        if (isPresent()) {
            this.provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            Utils.sendConsoleMessage("&2LuckPerms &ahas been enabled and hooked into Set-Ranks.");
            return true;
        } else {
            Utils.sendConsoleMessage("&2LuckPerms is not present, please install it to use the plugin.");
            Utils.disablePlugin();
            return false;
        }
    }

    /**
     * Disable the instance of the plugin.
     */
    @Override
    public void disable() {

    }

    public LuckPerms get() {
        return provider.getProvider();
    }
}

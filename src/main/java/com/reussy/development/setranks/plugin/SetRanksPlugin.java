package com.reussy.development.setranks.plugin;

import com.reussy.development.setranks.plugin.config.ConfigManager;
import com.reussy.development.setranks.plugin.config.MessageManager;
import com.reussy.development.setranks.plugin.integration.IPluginIntegration;
import com.reussy.development.setranks.plugin.integration.LuckPermsAPI;
import com.reussy.development.setranks.plugin.integration.PAPI;
import com.reussy.development.setranks.plugin.menu.element.ElementBuilder;
import com.reussy.development.setranks.plugin.utils.ExodusPluginStatus;
import com.reussy.development.setranks.plugin.utils.PluginStatus;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public class SetRanksPlugin extends JavaPlugin {

    private PluginStatus pluginStatus;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private ElementBuilder elementBuilder;
    private LuckPermsAPI luckPermsAPI;
    private PAPI placeholderAPI;

    @Override
    public void onEnable() {

        this.pluginStatus = new PluginStatus();

        this.configManager = new ConfigManager(this, null);
        this.messageManager = new MessageManager(this, null);

        populateIntegrations(this.luckPermsAPI = new LuckPermsAPI(), this.placeholderAPI = new PAPI());

        this.elementBuilder = new ElementBuilder(this);

        this.pluginStatus.setStatus(ExodusPluginStatus.ENABLED);
    }

    @Override
    public void onDisable() {

    }

    public PluginStatus getPluginStatus() {
        return pluginStatus;
    }

    public LuckPermsAPI getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public PAPI getPlaceholderAPI() {
        return placeholderAPI;
    }

    public ElementBuilder getElementBuilder() {
        return elementBuilder;
    }

    private void populateIntegrations(IPluginIntegration... pluginIntegrations) {
        Stream.of(pluginIntegrations).forEach(IPluginIntegration::enable);
    }

}

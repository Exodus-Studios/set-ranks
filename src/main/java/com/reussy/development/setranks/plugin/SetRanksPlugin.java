package com.reussy.development.setranks.plugin;

import com.reussy.development.setranks.plugin.command.admin.MainCommand;
import com.reussy.development.setranks.plugin.command.grant.ClearGrantHistoryCommand;
import com.reussy.development.setranks.plugin.command.grant.GrantCommand;
import com.reussy.development.setranks.plugin.command.grant.GrantHistoryCommand;
import com.reussy.development.setranks.plugin.command.grant.UnGrantCommand;
import com.reussy.development.setranks.plugin.command.rank.PromoteCommand;
import com.reussy.development.setranks.plugin.command.rank.RankCommand;
import com.reussy.development.setranks.plugin.command.rank.SetRankCommand;
import com.reussy.development.setranks.plugin.command.user.UserCommand;
import com.reussy.development.setranks.plugin.config.ConfigManager;
import com.reussy.development.setranks.plugin.config.MessageManager;
import com.reussy.development.setranks.plugin.controller.GroupController;
import com.reussy.development.setranks.plugin.event.PlayerMenuInteract;
import com.reussy.development.setranks.plugin.integration.IPluginIntegration;
import com.reussy.development.setranks.plugin.integration.LuckPermsAPI;
import com.reussy.development.setranks.plugin.integration.PAPI;
import com.reussy.development.setranks.plugin.menu.element.ElementBuilder;
import com.reussy.development.setranks.plugin.sql.ConnectionManager;
import com.reussy.development.setranks.plugin.sql.QueryManager;
import com.reussy.development.setranks.plugin.utils.ExodusPluginStatus;
import com.reussy.development.setranks.plugin.utils.PluginStatus;
import com.reussy.development.setranks.plugin.utils.Utils;
import com.reussy.development.setranks.plugin.utils.scheduler.PluginScheduler;
import com.reussy.development.setranks.plugin.utils.scheduler.SchedulerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SetRanksPlugin extends JavaPlugin {


    private final ConnectionManager connectionManager = new ConnectionManager(this);
    private QueryManager queryManager;
    private PluginStatus pluginStatus;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private ConfigManager rankMenuManager;
    private ConfigManager userMenuManager;
    private ConfigManager grantHistoryMenuManager;
    private List<ConfigManager> configManagers;
    private ElementBuilder elementBuilder;
    private LuckPermsAPI luckPermsAPI;
    private PAPI placeholderAPI;
    private GroupController groupController;
    private PluginScheduler pluginScheduler;

    @Override
    public void onEnable() {

        this.pluginStatus = new PluginStatus();

        this.configManager = new ConfigManager(this, null);
        this.messageManager = new MessageManager(this, null);
        this.rankMenuManager = new ConfigManager(this, "game-menus/rank-menu.yml");
        this.userMenuManager = new ConfigManager(this, "game-menus/user-menu.yml");
        this.grantHistoryMenuManager = new ConfigManager(this, "game-menus/grant-history-menu.yml");
        this.configManagers = Arrays.asList(configManager, userMenuManager, rankMenuManager, grantHistoryMenuManager, messageManager);

        populateIntegrations(this.luckPermsAPI = new LuckPermsAPI(), this.placeholderAPI = new PAPI());

        createConnectionDDBB();

        this.elementBuilder = new ElementBuilder(this);

        this.groupController = new GroupController(this);

        this.pluginScheduler = new SchedulerWrapper(this);

        populateCommands();
        registerEvents();

        this.pluginStatus.setStatus(ExodusPluginStatus.ENABLED);

        this.getConfigManagers().forEach(ConfigManager::reload);
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

    public ConfigManager getRankMenuManager() {
        return rankMenuManager;
    }

    public ConfigManager getUserMenuManager() {
        return userMenuManager;
    }

    public ConfigManager getGrantHistoryMenuManager() {
        return grantHistoryMenuManager;
    }

    public List<ConfigManager> getConfigManagers() {
        return configManagers;
    }

    public PAPI getPlaceholderAPI() {
        return placeholderAPI;
    }

    public ElementBuilder getElementBuilder() {
        return elementBuilder;
    }

    public GroupController getGroupController() {
        return groupController;
    }

    public PluginScheduler getPluginScheduler() {
        return pluginScheduler;
    }

    private void populateIntegrations(IPluginIntegration... pluginIntegrations) {
        Stream.of(pluginIntegrations).forEach(IPluginIntegration::enable);
    }

    private void populateCommands() {

        new MainCommand(getConfigManager().get("commands", "main-command"), this);

        new RankCommand(getConfigManager().get("commands", "rank"), this);
        new SetRankCommand(getConfigManager().get("commands", "set-rank"), this);
        new PromoteCommand(getConfigManager().get("commands", "promote"), this);
        new GrantCommand(getConfigManager().get("commands", "grant"), this);
        new UnGrantCommand(getConfigManager().get("commands", "ungrant"), this);
        new GrantHistoryCommand(getConfigManager().get("commands", "grant-history"), this);
        new ClearGrantHistoryCommand(getConfigManager().get("commands", "clear-grant-history"), this);
        new UserCommand(getConfigManager().get("commands", "user"), this);
    }

    private void createConnectionDDBB() {

        connectionManager.connect(configManager.get("storage", "database"),
                configManager.get("storage", "address"),
                configManager.get("storage", "username"),
                configManager.get("storage", "password"),
                configManager.get("storage", "port"),
                configManager.get("storage", "useSSL"));

        if (queryManager == null) {
            queryManager = new QueryManager(this, connectionManager.getConnection(), configManager.get("storage", "database"));
        }

        if (connectionManager.isConnected()) {
            Utils.sendDebugMessage("connected to the database!");
            queryManager.createTables();
        }
    }


    public QueryManager getQueryManager() {
        return queryManager;
    }

    public void setData(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    private void registerEvents() {

        Bukkit.getPluginManager().registerEvents(new PlayerMenuInteract(), this);

    }
}

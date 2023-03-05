package com.reussy.development.setranks.plugin.config;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.exceptions.PluginErrorException;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class ConfigManager {

    /* Clase para gestionar la configuración del plugin. Esta clase actúa como caché sobre el archivo
     * de configuración, por lo tanto es lo más óptimo para no tener que leer siempre la config.
     * Le falta programar el getList()  */

    private final SetRanksPlugin plugin;
    private File file;
    private HashMap<String, HashMap<String, String>> configCache = new HashMap<>();
    private FileConfiguration config;


    /* Constructor de la clase, recibe la instancia del plugin y el nombre del archivo de configuración (opcional) */
    public ConfigManager(SetRanksPlugin plugin, String fileName) {
        this.plugin = plugin;
        create(fileName);
    }

    private void create(String fileName) {

        if (fileName == null) fileName = "config.yml";

        file = new File(plugin.getDataFolder(), fileName);
        config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public String get(String path, String key) {
        if (configCache.containsKey(path)) {

            HashMap<String, String> map = configCache.get(path);
            if (map.containsKey(key)) {
                return map.get(key);
            }

            configCache.put(path, new HashMap<>() {{
                put(key, config.getString(path + "." + key, "N/A"));
            }});
        }

        String value = config.getString(path + "." + key, "N/A");
        byte[] bytes = value != null ? value.getBytes(StandardCharsets.ISO_8859_1) : new byte[0];
        value = new String(bytes, StandardCharsets.UTF_8);
        String finalValue = value;

        configCache.put(path, new HashMap<>() {{
            put(key, finalValue);
        }});

        if (value.equals("N/A")) {
            Utils.sendWarnMessage("The value " + path + "." + key + " is not set in the " + file.getName() + " file!");
        }

        return value;
    }

    public int getInt(String path, String key) {
        String value = get(path, key);

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new PluginErrorException("The value " + path + "." + key + " is not a number in the " + file.getName() + " file!", new IllegalArgumentException());
        }
    }

    public boolean getBoolean(String path, String key) {

        String value = get(path, key);

        if (value == null) {
            return false;
        }

        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new PluginErrorException("The value " + path + "." + key + " is not seted as boolean in the " + file.getName() + " file!", new IllegalArgumentException());
        }

        return Boolean.parseBoolean(value);
    }

    public List<String> getList(String path, String key) {
        return config.getStringList(path + "." + key);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public void reload() {

        Utils.sendWarnMessage("Reloading config file " + file.getName() + " (" +
                this.getClass().getSimpleName().replace("Manager", "") + ")... with " + configCache.size() + " values");
        configCache.clear();
        config = YamlConfiguration.loadConfiguration(file);
    }
}

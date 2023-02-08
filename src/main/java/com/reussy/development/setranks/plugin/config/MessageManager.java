package com.reussy.development.setranks.plugin.config;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class MessageManager extends ConfigManager {

    /* Clase para gestionar los mensajes del plugin
     * Solo añade un prefix al String de la config */

    private String path = "messages";

    public MessageManager(SetRanksPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    // Segundo constructor, por si el path no es "messages"
    public MessageManager(SetRanksPlugin plugin, String fileName, String path) {
        super(plugin, fileName);
        this.path = path;
    }


    // get(key, false) -> No añade el prefix
    // get(key) -> Añade el prefix
    // get(key, true) -> Añade el prefix
    public String get(PluginMessages key, Boolean @NotNull ... prefix) {
        return prefix.length != 0 && !prefix[0] ? get(this.path, key.get()) : Utils.PREFIX + get(this.path, key.get());
    }

    public void reload() {
        super.reload();
    }
}

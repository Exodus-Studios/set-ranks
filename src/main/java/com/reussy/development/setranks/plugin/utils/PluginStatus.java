package com.reussy.development.setranks.plugin.utils;

public class PluginStatus {

    /* Clase para gestionar el estado del plugin */

    private ExodusPluginStatus status = ExodusPluginStatus.STARTING;

    public boolean isEnabled() {
        return status == ExodusPluginStatus.ENABLED;
    }

    public boolean isDisabled() {
        return status == ExodusPluginStatus.DISABLED;
    }

    public boolean isDisabling() {
        return status == ExodusPluginStatus.DISABLING;
    }

    public ExodusPluginStatus getStatus() {
        return status;
    }

    public void setStatus(ExodusPluginStatus status) {
        this.status = status;
    }


}

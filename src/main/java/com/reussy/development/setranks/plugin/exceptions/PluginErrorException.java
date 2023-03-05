package com.reussy.development.setranks.plugin.exceptions;

import com.reussy.development.setranks.plugin.utils.Utils;

public class PluginErrorException extends RuntimeException {

    /* Clase para gestionar los errores graves del plugin (controlados)
       Cuando se lanza un error grave, se lanza un mensaje de error en la consola y se detiene el plugin */

    public PluginErrorException(String message) {
        super(null, null, true, false);
        Utils.sendErrorMessage(message);
        Utils.disablePlugin();

    }

    public PluginErrorException(String message, Exception e) {
        super(null, null, true, false);
        Utils.sendErrorMessage(message + "\n\n" +
                "&3Message: &7" + e.getMessage() + "\n" +
                "&3Cause: &7" + e.getCause() + "\n" +
                "&3StackTrace: \n&c" + Utils.cleanStackTree(e.getStackTrace()).replace("\n", "\n&c"));

    }
}
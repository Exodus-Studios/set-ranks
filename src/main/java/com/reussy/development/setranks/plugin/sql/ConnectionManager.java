package com.reussy.development.setranks.plugin.sql;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.exceptions.PluginErrorException;
import com.reussy.development.setranks.plugin.exceptions.PluginSQLException;
import com.reussy.development.setranks.plugin.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {


    /* Clase para gestionar la conexión a la base de datos
       Se usa un singleton para que solo haya una instancia de la clase y no se creen más conexiones
       Se gestiona los errores más comunes y se informa al usuario. */

    private final SetRanksPlugin plugin;
    private Connection connection;

    public ConnectionManager(final SetRanksPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect(@NotNull String database, @NotNull String host, @NotNull String user, @NotNull String password, @NotNull String port, @NotNull String ssl) {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true" + "&useSSL=" + ssl + "&useUnicode=true&characterEncoding=UTF-8", user, password);
                //connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + parseBoolean(ssl) + "&autoReconnect=true", user, password);
            } catch (SQLException e) {

                int code = e.getErrorCode();

                // Switch para los errores de conexión
                switch (code) {

                    case 1045:
                        throw new PluginErrorException("Error connecting to the database: Incorrect username or password.");

                        // La base de datos no existe, intentamos crear la base de datos y las tablas
                    case 1049: {
                        try {
                            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true" + "&useSSL=" + ssl + "&useUnicode=true&characterEncoding=UTF-8", user, password);
                            plugin.setData(new QueryManager(plugin, connection, database));
                            plugin.getQueryManager().createDatabase(database);
                            connect(database, host, user, password, port, ssl);
                            plugin.setData(new QueryManager(plugin, connection, database));
                            plugin.getQueryManager().createTables();
                        } catch (SQLException ignored) {
                        }
                    }

                    case 0: {
                        throw new PluginErrorException("Error connecting to the database: Connection refused." + "\nCheck if the database is running, check the host, port and SSL. " + "\nIf you are running the Minecraft server on a different machine than the SQL service check that you allow remote access in the SQL service config or if the firewall is blocking the connection.");
                    }

                    default: {
                        throw new PluginErrorException("Error connecting to the database: \n More info: \n" + "Error code: " + code + e.getErrorCode() + "\n" + "Error Message: " + e.getMessage());
                    }
                }
            }
        }

        if (isConnected()) {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {

                if (isConnected()) {
                    try {
                        Utils.sendDebugMessage("Connection refresh");
                        connection.createStatement().execute("SELECT 1");
                    } catch (SQLException e) {
                        throw new PluginSQLException("Error while checking the connection to the database: " + e.getMessage(), e);
                    }
                }
            }, 1000, 20 * 60 * 30);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();

            } catch (SQLException ignored) {
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


}

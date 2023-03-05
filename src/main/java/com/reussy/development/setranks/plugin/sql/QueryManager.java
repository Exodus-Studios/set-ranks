package com.reussy.development.setranks.plugin.sql;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.exceptions.PluginSQLException;
import com.reussy.development.setranks.plugin.utils.Utils;

import java.sql.*;

public class QueryManager {

    /* Clase para gestionar las query's de la base de datos
     *  Deberemos de lanzar una tarea asíncrona para que no se
     *  bloquee el servidor antes de llamar a la función
     *
     */

    private final SetRanksPlugin plugin;
    private final String database;
    private final Connection connection;

    public QueryManager(SetRanksPlugin main, Connection connection, String database) {
        this.plugin = main;
        this.connection = connection;
        this.database = database;
    }

    public void createTables() {
        Statement statement = null;
        try {

            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, String.valueOf(SQLTables.USER_HISTORY), null);

            if (!tables.next()) {
                connection.setCatalog(database);

                Utils.sendDebugMessage("Creating tables...");

                statement = connection.createStatement();
                statement.addBatch("CREATE TABLE " + SQLTables.USER_HISTORY + " ("
                        + SQLTables._USER_HISTORY.USER_CHANGED + " VARCHAR(36) NOT NULL,"
                        + SQLTables._USER_HISTORY.USER_CHANGER + " VARCHAR(36) NOT NULL,"
                        + SQLTables._USER_HISTORY.TYPE + " VARCHAR(20) NOT NULL,"
                        + SQLTables._USER_HISTORY.PERMISSION + " VARCHAR(255) NOT NULL,"
                        + SQLTables._USER_HISTORY.DATE + " DATETIME NOT NULL,"
                        + SQLTables._USER_HISTORY.REASON + " VARCHAR(255) NULL);");

                Utils.sendDebugMessage("Table " + SQLTables.USER_HISTORY + " created.");

                statement.addBatch("CREATE TABLE " + SQLTables.RANK_HISTORY + " ("
                        + SQLTables._RANK_HISTORY.USER + " VARCHAR(36) NOT NULL,"
                        + SQLTables._RANK_HISTORY.RANK + " VARCHAR(255) NOT NULL,"
                        + SQLTables._RANK_HISTORY.TYPE + " VARCHAR(20) NOT NULL,"
                        + SQLTables._RANK_HISTORY.REASON + " VARCHAR(255) NULL,"
                        + SQLTables._RANK_HISTORY.DATE + " DATETIME NOT NULL);");

                Utils.sendDebugMessage("Table " + SQLTables.RANK_HISTORY + " created.");

                statement.executeBatch();

                Utils.sendDebugMessage("Tables created successfully.");
            }

        } catch (SQLException e) {
            throw new PluginSQLException("Error creating tables", e);
        }

    }

    public void createDatabase(String database) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Creating database " + database + "...");
            ps = connection.prepareStatement("CREATE DATABASE " + database + ";");

            ps.executeUpdate();
            Utils.sendDebugMessage("Database " + database + " created!");


        } catch (SQLException e) {
            throw new PluginSQLException("Error creating database " + database, e);
        }
    }


}


package com.reussy.development.setranks.plugin.sql;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.exceptions.PluginSQLException;
import com.reussy.development.setranks.plugin.sql.entity.RoleHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.RoleTypeChange;
import com.reussy.development.setranks.plugin.sql.entity.UserHistoryEntity;
import com.reussy.development.setranks.plugin.sql.entity.UserTypeChange;
import com.reussy.development.setranks.plugin.utils.Utils;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                        + SQLTables._USER_HISTORY.ID + " INT NOT NULL AUTO_INCREMENT,"
                        + SQLTables._USER_HISTORY.USER_CHANGED + " VARCHAR(36) NOT NULL,"
                        + SQLTables._USER_HISTORY.USER_CHANGER + " VARCHAR(36) NOT NULL,"
                        + SQLTables._USER_HISTORY.TYPE + " VARCHAR(20) NOT NULL,"
                        + SQLTables._USER_HISTORY.PERMISSION + " VARCHAR(255) NOT NULL,"
                        + SQLTables._USER_HISTORY.DATE + " DATETIME NOT NULL,"
                        + SQLTables._USER_HISTORY.REASON + " VARCHAR(255) NULL," +
                        "PRIMARY KEY (" + SQLTables._USER_HISTORY.ID + "));");

                Utils.sendDebugMessage("Table " + SQLTables.USER_HISTORY + " created.");

                statement.addBatch("CREATE TABLE " + SQLTables.RANK_HISTORY + " ("
                        + SQLTables._ROLE_HISTORY.ID + " INT NOT NULL AUTO_INCREMENT,"
                        + SQLTables._ROLE_HISTORY.USER + " VARCHAR(36) NOT NULL,"
                        + SQLTables._ROLE_HISTORY.RANK + " VARCHAR(255) NOT NULL,"
                        + SQLTables._ROLE_HISTORY.TYPE + " VARCHAR(20) NOT NULL,"
                        + SQLTables._ROLE_HISTORY.REASON + " VARCHAR(255) NULL,"
                        + SQLTables._ROLE_HISTORY.DATE + " DATETIME NOT NULL ," +
                        "PRIMARY KEY (" + SQLTables._ROLE_HISTORY.ID + "));");


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

    public void insertRoleHistory(RoleHistoryEntity entity) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Inserting role history...");
            ps = connection.prepareStatement("INSERT INTO " + SQLTables.RANK_HISTORY + " ("
                    + SQLTables._ROLE_HISTORY.USER + ","
                    + SQLTables._ROLE_HISTORY.RANK + ","
                    + SQLTables._ROLE_HISTORY.TYPE + ","
                    + SQLTables._ROLE_HISTORY.REASON + ","
                    + SQLTables._ROLE_HISTORY.DATE + ") VALUES (?,?,?,?,?);");

            ps.setString(1, entity.getUser().toString());
            ps.setString(2, entity.getRank());
            ps.setString(3, entity.getType().toString());
            ps.setString(4, entity.getReason());
            ps.setTimestamp(5, new Timestamp(entity.getDate().getTime()));

            ps.executeUpdate();
            Utils.sendDebugMessage("Role history inserted!");

        } catch (SQLException e) {
            throw new PluginSQLException("Error inserting role history", e);
        }
    }

    public void insertUserHistory(UserHistoryEntity entity) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Inserting user history...");
            ps = connection.prepareStatement("INSERT INTO " + SQLTables.USER_HISTORY + " ("
                    + SQLTables._USER_HISTORY.USER_CHANGED + ","
                    + SQLTables._USER_HISTORY.USER_CHANGER + ","
                    + SQLTables._USER_HISTORY.TYPE + ","
                    + SQLTables._USER_HISTORY.PERMISSION + ","
                    + SQLTables._USER_HISTORY.DATE + ","
                    + SQLTables._USER_HISTORY.REASON + ") VALUES (?,?,?,?,?,?);");

            ps.setString(1, entity.getUserChanged().toString());
            ps.setString(2, entity.getUserChanger().toString());
            ps.setString(3, entity.getType().toString());
            ps.setString(4, entity.getPermission());
            ps.setTimestamp(5, new Timestamp(entity.getDate().getTime()));
            ps.setString(6, entity.getReason());

            ps.executeUpdate();
            Utils.sendDebugMessage("User history inserted!");

        } catch (SQLException e) {
            throw new PluginSQLException("Error inserting user history", e);
        }
    }

    public List<UserHistoryEntity> getUsersHistory(){
        PreparedStatement ps;
        try {

            Utils.sendDebugMessage("Getting user history...");
            ps = connection.prepareStatement("SELECT * FROM " + SQLTables.USER_HISTORY + " ORDER BY " + SQLTables._USER_HISTORY.ID + " DESC LIMIT 1;");

            ResultSet rs = ps.executeQuery();

            List<UserHistoryEntity> userHistoryEntities = new ArrayList<>();

            while (rs.next()) {
                userHistoryEntities.add(new UserHistoryEntity(
                        BigInteger.valueOf(rs.getLong(String.valueOf(SQLTables._USER_HISTORY.ID))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGED))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGER))),
                        UserTypeChange.valueOf(rs.getString(String.valueOf(SQLTables._USER_HISTORY.TYPE))),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.PERMISSION)),
                        rs.getTimestamp(String.valueOf(SQLTables._USER_HISTORY.DATE)),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.REASON))));
            }

            return userHistoryEntities;

        } catch (SQLException e) {
            throw new PluginSQLException("Error getting role history", e);
        }
    }

    public UserHistoryEntity getUserHistory(long id){
        PreparedStatement ps;
        try {

            Utils.sendDebugMessage("Getting user history...");
            ps = connection.prepareStatement("SELECT * FROM " + SQLTables.USER_HISTORY + " WHERE "
                    + SQLTables._USER_HISTORY.ID + " = ?;");

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                UserHistoryEntity userHistoryEntity = new UserHistoryEntity(
                        BigInteger.valueOf(rs.getLong(String.valueOf(SQLTables._USER_HISTORY.ID))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGED))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGER))),
                        UserTypeChange.valueOf(rs.getString(String.valueOf(SQLTables._USER_HISTORY.TYPE))),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.PERMISSION)),
                        rs.getTimestamp(String.valueOf(SQLTables._USER_HISTORY.DATE)),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.REASON))
                );

                return userHistoryEntity;
            }

        } catch (SQLException e) {
            throw new PluginSQLException("Error getting role history", e);
        }

        return null;
    }

    public List<UserHistoryEntity> getUserHistoryList(UUID user) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Getting user history list...");
            ps = connection.prepareStatement("SELECT * FROM " + SQLTables.USER_HISTORY + " WHERE "
                    + SQLTables._USER_HISTORY.USER_CHANGED + " = ?;");

            ps.setString(1, user.toString());

            ResultSet rs = ps.executeQuery();

            List<UserHistoryEntity> list = new ArrayList<>();

            while (rs.next()) {
                Utils.sendDebugMessage("Adding user history to list...");
                UserHistoryEntity entity = new UserHistoryEntity(
                        BigInteger.valueOf(rs.getLong(String.valueOf(SQLTables._USER_HISTORY.ID))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGED))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._USER_HISTORY.USER_CHANGER))),
                        UserTypeChange.valueOf(rs.getString(String.valueOf(SQLTables._USER_HISTORY.TYPE))),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.PERMISSION)),
                        rs.getTimestamp(String.valueOf(SQLTables._USER_HISTORY.DATE)),
                        rs.getString(String.valueOf(SQLTables._USER_HISTORY.REASON))
                );

                list.add(entity);
            }

            return list;

        } catch (SQLException e) {
            throw new PluginSQLException("Error getting user history list", e);
        }
    }

    public List<RoleHistoryEntity> getRoleHistoryList(UUID user) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Getting role history list...");
            ps = connection.prepareStatement("SELECT * FROM " + SQLTables.RANK_HISTORY + " WHERE "
                    + SQLTables._ROLE_HISTORY.USER + " = ?;");

            ps.setString(1, user.toString());

            ResultSet rs = ps.executeQuery();

            List<RoleHistoryEntity> list = new ArrayList<>();

            while (rs.next()) {

                RoleHistoryEntity entity = new RoleHistoryEntity(
                        BigInteger.valueOf(rs.getLong(String.valueOf(SQLTables._ROLE_HISTORY.ID))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.USER))),
                        rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.RANK)),
                        rs.getTimestamp(String.valueOf(SQLTables._ROLE_HISTORY.DATE)),
                        rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.REASON)),
                        RoleTypeChange.valueOf(rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.TYPE)))
                );

                list.add(entity);
            }

            return list;

        } catch (SQLException e) {
            throw new PluginSQLException("Error getting role history list", e);
        }
    }

    public RoleHistoryEntity getRoleHistory(String rank) {

        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Getting role history...");
            ps = connection.prepareStatement("SELECT * FROM " + SQLTables.RANK_HISTORY + " WHERE "
                    + SQLTables._ROLE_HISTORY.RANK + " = ?;");

            ps.setString(1, rank);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                RoleHistoryEntity entity = new RoleHistoryEntity(
                        BigInteger.valueOf(rs.getLong(String.valueOf(SQLTables._ROLE_HISTORY.ID))),
                        UUID.fromString(rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.USER))),
                        rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.RANK)),
                        rs.getTimestamp(String.valueOf(SQLTables._ROLE_HISTORY.DATE)),
                        rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.REASON)),
                        RoleTypeChange.valueOf(rs.getString(String.valueOf(SQLTables._ROLE_HISTORY.TYPE)))
                );

                return entity;
            }

        } catch (SQLException e) {
            throw new PluginSQLException("Error getting role history", e);
        }

        return null;
    }

    public void deleteRoleHistory(BigInteger id){
        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Deleting role history...");
            ps = connection.prepareStatement("DELETE FROM " + SQLTables.RANK_HISTORY + " WHERE "
                    + SQLTables._ROLE_HISTORY.ID + " = ?;");

            ps.setLong(1, id.longValue());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PluginSQLException("Error deleting role history", e);
        }
    }

    public void deleteUserHistory(BigInteger id){
        PreparedStatement ps = null;
        try {

            Utils.sendDebugMessage("Deleting user history...");
            ps = connection.prepareStatement("DELETE FROM " + SQLTables.USER_HISTORY + " WHERE "
                    + SQLTables._USER_HISTORY.ID + " = ?;");

            ps.setLong(1, id.longValue());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PluginSQLException("Error deleting user history", e);
        }
    }

    public void clearUserHistory(UUID user){
        PreparedStatement ps;
        try {

            Utils.sendDebugMessage("Clearing user history...");
            ps = connection.prepareStatement("DELETE FROM " + SQLTables.USER_HISTORY + " WHERE "
                    + SQLTables._USER_HISTORY.USER_CHANGED + " = ?;");

            ps.setString(1, user.toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PluginSQLException("Error clearing user history", e);
        }
    }
}


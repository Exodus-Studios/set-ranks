package com.reussy.development.setranks.plugin.sql;

import com.reussy.development.setranks.plugin.SetRanksPlugin;

import java.sql.Connection;

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

    }

    public void createDatabase(String database) {

    }


}


package com.reussy.development.setranks.plugin.sql;

public enum SQLTables {

    // Tables Name
    USER_HISTORY,
    RANK_HISTORY;


    // Columns Name of each table

    public enum _USER_HISTORY {
        ID,
        USER_CHANGED,
        USER_CHANGER,
        TYPE,
        PERMISSION,
        DATE,
        REASON,
    }

    public enum _ROLE_HISTORY {
        ID,
        USER,
        RANK,
        TYPE,
        REASON,
        DATE,
    }
}

package com.reussy.development.setranks.plugin.sql;

public enum SQLTables {

    // Tables Name
    USER_HISTORY,
    RANK_HISTORY;


    // Columns Name of each table

    public enum _USER_HISTORY {
        USER_CHANGED,
        USER_CHANGER,
        TYPE,
        PERMISSION,
        DATE,
        REASON,
    }

    public enum _RANK_HISTORY {
        USER,
        RANK,
        TYPE,
        REASON,
        DATE,
    }
}

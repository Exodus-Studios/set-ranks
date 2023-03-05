package com.reussy.development.setranks.plugin.sql;

public enum SQLTables {

    // Tables Name
    USER,
    REPORT,
    SANCTION,
    LOGS,
    TICKET;

    // Columns Name of each table

    public enum _USER {
        UUID,
        NAME;
    }

    public enum _REPORT {
        ID,
        REPORTED,
        REPORTER,
        STATUS,
        REASON,
        OPEN,
        CLOSE,
        COMMENT;
    }

    public enum _LOGS {
        ID,
        USER,
        ACTION,
        DATE,
        NAME,
        IP;

    }

    public enum _SANCTIONS {
        ID,
        TYPE,
        USER,
        START,
        END,
        STAFF,
        ACTIVE,
        FORGIVER,
        REASON,
        IP;
    }

    public enum _TICKET {
        ID,
        CREATOR,
        ASSIGNED,
        OPEN,
        CLOSE,
        REASON,
        MESSAGES,
        STATUS,
        PRIORITY;
    }
}

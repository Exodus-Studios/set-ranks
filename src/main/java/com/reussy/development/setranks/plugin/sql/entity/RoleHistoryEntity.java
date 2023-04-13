package com.reussy.development.setranks.plugin.sql.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

public class RoleHistoryEntity {

    private final BigInteger id;
    private final UUID user;
    private final String rank;
    private final Date date;
    private final String reason;
    private final RoleTypeChange type;

    public RoleHistoryEntity(BigInteger id, UUID user, String rank, Date date, String reason, RoleTypeChange type) {
        this.id = id;
        this.user = user;
        this.rank = rank;
        this.date = date;
        this.reason = reason;
        this.type = type;
    }

    public RoleHistoryEntity(UUID user, String rank, Date date, String reason, RoleTypeChange type) {
        this.id = BigInteger.valueOf(System.currentTimeMillis());
        this.user = user;
        this.rank = rank;
        this.date = date;
        this.reason = reason;
        this.type = type;
    }

    public BigInteger getId() {
        return id;
    }

    public UUID getUser() {
        return user;
    }

    public String getRank() {
        return rank;
    }

    public Date getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public RoleTypeChange getType() {
        return type;
    }
}

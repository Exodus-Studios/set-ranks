package com.reussy.development.setranks.plugin.sql.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

public class UserHistoryEntity {

    private final UUID userChanged;
    private final UUID userChanger;
    private final UserTypeChange type;
    private final String permission;
    private final Date date;
    private final String reason;
    private BigInteger id;

    public UserHistoryEntity(BigInteger id, UUID userChanged, UUID userChanger, UserTypeChange type, String permission, Date date, String reason) {
        this.id = id;
        this.userChanged = userChanged;
        this.userChanger = userChanger;
        this.type = type;
        this.permission = permission;
        this.date = date;
        this.reason = reason;
    }

    public UserHistoryEntity(UUID userChanged, UUID userChanger, UserTypeChange type, String permission, Date date, String reason) {
        this.userChanged = userChanged;
        this.userChanger = userChanger;
        this.type = type;
        this.permission = permission;
        this.date = date;
        this.reason = reason;
    }

    public BigInteger getId() {
        return id;
    }

    public UUID getUserChanged() {
        return userChanged;
    }

    public UUID getUserChanger() {
        return userChanger;
    }

    public UserTypeChange getType() {
        return type;
    }

    public String getRank() {
        return permission;
    }

    public Date getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }
}

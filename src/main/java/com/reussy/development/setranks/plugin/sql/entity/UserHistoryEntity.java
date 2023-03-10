package com.reussy.development.setranks.plugin.sql.entity;

import java.math.BigInteger;
import java.util.UUID;

public class UserHistoryEntity {

    private final BigInteger id;
    private final UUID userChanged;
    private final UUID userChanger;
    private final UserTypeChange type;
    private final String permission;
    private final String date;
    private final String reason;

    public UserHistoryEntity(BigInteger id, UUID userChanged, UUID userChanger, UserTypeChange type, String permission, String date, String reason) {
        this.id = id;
        this.userChanged = userChanged;
        this.userChanger = userChanger;
        this.type = type;
        this.permission = permission;
        this.date = date;
        this.reason = reason;
    }

        public UserHistoryEntity(UUID userChanged, UUID userChanger, UserTypeChange type, String permission, String date, String reason) {
        this.id = BigInteger.valueOf(System.currentTimeMillis());
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

    public String getPermission() {
        return permission;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }
}

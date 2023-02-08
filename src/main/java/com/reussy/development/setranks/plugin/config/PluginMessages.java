package com.reussy.development.setranks.plugin.config;

public enum PluginMessages {

    PLUGIN_PREFIX("plugin-prefix"),
    MUST_BE_PLAYER("must-be-player"),
    PLAYER_NOT_FOUND("player-not-found"),
    NO_PERMISSION("no-permission"),
    SET_RANK_USAGE("set-rank.usage"),
    SET_RANK_RANK_NOT_FOUND("set-rank.rank-not-found"),
    SET_RANK_INVALID_DURATION("set-rank.invalid-duration"),
    SET_RANK_ALREADY_IN_GROUP("set-rank.already-in-group"),
    SET_RANK_SUCCESSFUL("set-rank.success"),;

    private final String prefix;

    PluginMessages(String prefix) {
        this.prefix = prefix;
    }

    public String get() {
        return prefix;
    }
}

package com.reussy.development.setranks.plugin.config;

public enum PluginMessages {

    PLUGIN_PREFIX("plugin-prefix"),
    PLUGIN_RELOADED("plugin-reloaded"),
    ONLY_CONSOLE("only-console"),
    MUST_BE_PLAYER("must-be-player"),
    PLAYER_NOT_FOUND("player-not-found"),
    NO_PERMISSION("no-permission"),
    SET_RANK_USAGE("set-rank.usage"),
    SET_RANK_RANK_NOT_FOUND("set-rank.rank-not-found"),
    SET_RANK_INVALID_DURATION("set-rank.invalid-duration"),
    SET_RANK_ALREADY_IN_GROUP("set-rank.already-in-group"),
    SET_RANK_SUCCESSFUL("set-rank.success"),
    PROMOTE_USAGE("promote.usage"),
    PROMOTE_NO_TRACKS("promote.no-tracks"),
    PROMOTE_UNKNOWN_TRACK("promote.unknown-track"),
    PROMOTE_ALREADY_IN_GROUP("promote.already-in-group"),
    PROMOTE_SUCCESSFUL("promote.success"),
    PROMOTE_FAILED("promote.failed"),
    GRANT_USAGE("grant.grant-usage"),
    UNGRANT_USAGE("grant.ungrant-usage"),
    GRANT_HISTORY_USAGE("grant.grant-history-usage"),
    GRANT_CLEAR_HISTORY_USAGE("grant.clear-grant-history-usage"),
    NO_GRANT_HISTORY("grant.no-history"),
    GRANT_HISTORY_CLEARED("grant.grant-history-cleared"),
    GRANT_CREATED("grant.grant-created"),
    GRANT_NOT_FOUND("grant.grant-not-found"),
    UNGRANT_CREATED("grant.ungrant-created");

    private final String prefix;

    PluginMessages(String prefix) {
        this.prefix = prefix;
    }

    public String get() {
        return prefix;
    }
}

package com.reussy.development.setranks.plugin.controller;

import com.reussy.development.setranks.plugin.SetRanksPlugin;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupController {

    private final SetRanksPlugin plugin;

    public GroupController(SetRanksPlugin plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> hasGroup(UUID who, String groupName) {
        return plugin.getLuckPermsAPI().get().getUserManager().loadUser(who)
                .thenApplyAsync(user -> {
                    Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
                    return inheritedGroups.stream().anyMatch(group -> group.getName().equals(groupName));
                });
    }

    public Node buildNode(String groupName, Duration duration) {
        return Node.builder("group." + groupName)
                .expiry(duration)
                .build();
    }
}

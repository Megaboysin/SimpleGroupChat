package kz.megabob.simpleGroupChat.groups;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Group {
    private String name;
    private UUID owner;
    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> joinRequests = new HashSet<>();

    public Group(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void addJoinRequest(UUID uuid) {
        joinRequests.add(uuid);
    }

    public boolean hasRequested(UUID uuid) {
        return joinRequests.contains(uuid);
    }

    public boolean removeRequest(UUID uuid) {
        joinRequests.remove(uuid);
        return false;
    }

    public Set<UUID> getJoinRequests() {
        return joinRequests;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }
}
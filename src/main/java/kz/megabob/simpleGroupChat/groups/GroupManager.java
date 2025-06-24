package kz.megabob.simpleGroupChat.groups;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class GroupManager {
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<UUID, String> playerToGroup = new HashMap<>();

    public boolean createGroup(String name, UUID owner) {
        if (groups.containsKey(name)) return false;
        Group group = new Group(name, owner);
        groups.put(name, group);
        playerToGroup.put(owner, name);
        return true;
    }

    public boolean denyRequest(UUID owner, UUID target) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;
        return group.removeRequest(target);
    }

    public boolean kickMember(UUID owner, UUID target) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;
        if (!group.getMembers().contains(target)) return false;
        if (target.equals(owner)) return false; // нельзя кикнуть себя

        group.removeMember(target);
        playerToGroup.remove(target);
        return true;
    }

    public boolean groupExists(String name) {
        return groups.values().stream()
                .anyMatch(group -> group.getName().equalsIgnoreCase(name));
    }

    public boolean leaveGroup(UUID player) {
        String groupName = playerToGroup.remove(player);
        if (groupName == null) return false;

        Group group = groups.get(groupName);
        if (group == null) return false;

        group.removeMember(player);

        if (group.getOwner().equals(player)) {
            for (UUID member : group.getMembers()) {
                playerToGroup.remove(member);
            }
            groups.remove(groupName);
        }

        return true;
    }

    public Group getGroup(UUID player) {
        String groupName = playerToGroup.get(player);
        return groupName != null ? groups.get(groupName) : null;
    }

    public List<String> listGroups() {
        return new ArrayList<>(groups.keySet());
    }

    public boolean requestToJoin(String groupName, UUID player) {
        Group group = groups.get(groupName);
        if (group == null || group.isMember(player)) return false;
        group.addJoinRequest(player);
        return true;
    }

    public Set<UUID> getRequests(UUID player) {
        Group group = getGroup(player);
        if (group != null && group.getOwner().equals(player)) {
            return group.getJoinRequests();
        }
        return Collections.emptySet();
    }

    public boolean acceptRequest(UUID owner, UUID requester) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;

        if (group.hasRequested(requester)) {
            group.addMember(requester);
            group.removeRequest(requester);
            playerToGroup.put(requester, group.getName());
            return true;
        }

        return false;
    }

    public boolean renameGroup(UUID owner, String newName) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;

        if (groups.containsKey(newName)) return false;

        groups.remove(group.getName());
        group.setName(newName);
        groups.put(newName, group);

        return true;
    }
}


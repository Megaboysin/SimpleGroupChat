package kz.megabob.simpleGroupChat.groups;

import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;
import kz.megabob.simpleGroupChat.language.LangManager;


public class GroupManager {
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<UUID, String> playerToGroup = new HashMap<>();
    private LangManager langManager;

    public GroupManager(LangManager langManager){
        this.langManager = langManager;
    }

    public boolean createGroup(String name, UUID owner) {
        if (groups.containsKey(name)) return false;
        Group group = new Group(name, owner);
        groups.put(name, group);
        playerToGroup.put(owner, name);
        return true;
    }

    public boolean denyInvite(UUID player) {
        for (Group group : groups.values()) {
            if (group.hasInvitation(player)) {
                group.removeInvitation(player);

                Player p = Bukkit.getPlayer(player);
                String playerName = (p != null) ? p.getName() : "Unknown";

                Player owner = Bukkit.getPlayer(group.getOwner());
                if (owner != null) {
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Denied"));
                    owner.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                            .replace("%player%", playerName));
                }

                return true;
            }
        }
        return false;
    }

    /*public boolean denyRequest(UUID owner, UUID target) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;

        return group.removeRequest(target);
    }*/

    public Set<String> getInvitations(UUID player) {
        return groups.entrySet().stream()
                .filter(entry -> entry.getValue().getInvitations().contains(player))
                .map(Map.Entry::getKey) // возвращаем название группы
                .collect(Collectors.toSet());
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
        Player leaver = Bukkit.getPlayer(player);
        String playerName = (leaver != null) ? leaver.getName() : "Unknown";

        // Уведомляем остальных участников
        for (UUID memberUUID : group.getMembers()) {
            if (!memberUUID.equals(player)) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null) {
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Leave.Notify"));
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                            .replace("%player%", playerName));
                }
            }
        }

        // Если владелец покинул — удаляем всю группу
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

    public boolean inviteToGroup(String groupName, UUID player) {
        Group group = groups.get(groupName);
        if (group == null || group.isMember(player)) return false;

        return group.addInvitation(player); // true, если впервые приглашён
    }

    public boolean requestToJoin(String groupName, UUID player) {
        Group group = groups.get(groupName);
        if (group == null || group.isMember(player)) return false;

        boolean added = group.addJoinRequest(player);
        if (!added) return false;

        // Отправка уведомления владельцу, если он онлайн
        UUID ownerUUID = group.getOwner();
        Player owner = Bukkit.getPlayer(ownerUUID);
        Player requester = Bukkit.getPlayer(player);

        if (owner != null && owner.isOnline() && requester != null) {
            String ownerLang = langManager.getDefaultLang(); // Используем язык из config.yml
            String msg = HexColorUtil.translateHexColorCodes(langManager.get(ownerLang,"messages.Request.Notify"));
            owner.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("%player%", requester.getName())
                    .replace("%group%", groupName));

            String msg2 = HexColorUtil.translateHexColorCodes(langManager.get(ownerLang,"messages.Request.AcceptHint"));
            owner.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2)
                    .replace("%player%", requester.getName()));

            String msg3 = HexColorUtil.translateHexColorCodes(langManager.get(ownerLang,"messages.Request.DenyHint"));
            owner.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3)
                    .replace("%player%", requester.getName()));
        }

        return true;
    }


    public Set<UUID> getRequests(UUID player) {
        Group group = getGroup(player);
        if (group != null && group.getOwner().equals(player)) {
            return group.getJoinRequests();
        }
        return Collections.emptySet();
    }

    // Проверяет, есть ли приглашение, и возвращает группу
    public Group getInvitationGroup(UUID player) {
        for (Group group : groups.values()) {
            if (group.hasInvitation(player)) {
                return group;
            }
        }
        return null;
    }

    public boolean acceptInvite(UUID player) {
        Group group = getInvitationGroup(player);
        if (group == null) return false;

        group.addMember(player);
        group.removeInvitation(player);
        playerToGroup.put(player, group.getName());

        // Уведомляем всех участников, кроме вступающего
        Player joining = Bukkit.getPlayer(player);
        String playerName = (joining != null) ? joining.getName() : "Unknown";

        for (UUID memberUUID : group.getMembers()) {
            if (!memberUUID.equals(player)) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null) {
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Join.Notify"));
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                            .replace("%player%", playerName));
                }
            }
        }

        return true;
    }

    public boolean acceptRequest(UUID owner, UUID requester) {
        Group group = getGroup(owner);
        if (group == null || !group.getOwner().equals(owner)) return false;

        if (!group.hasRequested(requester)) return false;

        group.addMember(requester);
        group.removeRequest(requester);
        playerToGroup.put(requester, group.getName());

        return true;
    }

}


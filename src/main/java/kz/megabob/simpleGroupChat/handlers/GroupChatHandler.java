package kz.megabob.simpleGroupChat.handlers;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.FormatResolver;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GroupChatHandler implements Listener {
    private final Set<UUID> groupChatUsers = new HashSet<>();
    private final GroupManager groupManager;
    private final FormatResolver formatResolver;
    private final LangManager langManager;

    public GroupChatHandler(GroupManager groupManager, FormatResolver formatResolver, LangManager langManager) {
        this.groupManager = groupManager;
        this.formatResolver = formatResolver;
        this.langManager = langManager;
    }

    public void toggleGroupChat(Player player) {
        UUID uuid = player.getUniqueId();
        String lang = langManager.getDefaultLang();

        if (groupChatUsers.contains(uuid)) {
            groupChatUsers.remove(uuid);
            String msg = HexColorUtil.translateHexColorCodes(langManager.get(langManager.getDefaultLang(), "messages.GroupChatToggle.Off"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            groupChatUsers.add(uuid);
            String msg = HexColorUtil.translateHexColorCodes(langManager.get(langManager.getDefaultLang(), "messages.GroupChatToggle.On"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public boolean isInGroupChatMode(UUID uuid) {
        return groupChatUsers.contains(uuid);
    }

    public void setGroupChatMode(Player player, boolean enabled) {
        UUID uuid = player.getUniqueId();
        if (enabled) {
            groupChatUsers.add(uuid);
        } else {
            groupChatUsers.remove(uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        UUID uuid = sender.getUniqueId();
        String message = event.getMessage();

        if (!isInGroupChatMode(uuid)) return;

        event.setCancelled(true);
        Group group = groupManager.getGroup(uuid);
        if (group == null) {
            groupChatUsers.remove(uuid);
            String msg = HexColorUtil.translateHexColorCodes(langManager.get(langManager.getDefaultLang(), "messages.General.NotInGroup"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return;
        }

        // Отправляем сообщение всем участникам группы
        for (UUID memberUUID : group.getMembers()) {
            Player receiver = Bukkit.getPlayer(memberUUID);
            if (receiver != null && receiver.isOnline()) {
                String formatted = formatResolver.resolve("group", sender, receiver, message);
                receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));

                if (receiver.equals(sender)) {
                    Bukkit.getConsoleSender().sendMessage("[GroupChat] " + ChatColor.stripColor(formatted));
                }
            }
        }
    }
}

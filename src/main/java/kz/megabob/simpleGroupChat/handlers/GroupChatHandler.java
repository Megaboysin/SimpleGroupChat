package kz.megabob.simpleGroupChat.handlers;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.utils.FormatResolver;
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
    private GroupManager groupManager;
    private FormatResolver formatResolver;

    public GroupChatHandler(GroupManager groupManager, FormatResolver formatResolver){
        this.formatResolver = formatResolver;
        this.groupManager = groupManager;
    }

    public void toggleGroupChat(Player player) {
        UUID uuid = player.getUniqueId();
        if (groupChatUsers.contains(uuid)) {
            groupChatUsers.remove(uuid);
            player.sendMessage("§7Групповой чат §cвыключен§7.");
        } else {
            groupChatUsers.add(uuid);
            player.sendMessage("§7Групповой чат §aвключен§7. Теперь вы пишете только своей группе.");
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

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player sender = event.getPlayer();
        UUID uuid = sender.getUniqueId();
        String message = event.getMessage();
        Bukkit.getConsoleSender().sendMessage("§7[DEBUG] Чат от " + sender.getName() + ", групповой: " + isInGroupChatMode(uuid));

        if (isInGroupChatMode(sender.getUniqueId())) {
            event.setCancelled(true);
            // отправка по своей логике
        }
        Bukkit.getConsoleSender().sendMessage("§e[DEBUG2] Групповой режим: " + isInGroupChatMode(sender.getUniqueId()));
        Bukkit.getConsoleSender().sendMessage("§e[DEBUG2] Отменено: " + event.isCancelled());
        // Проверка: находится ли игрок в групповом режиме
        if (!isInGroupChatMode(uuid)) return;

        // Отменяем отправку сообщения в глобальный чат
        event.setCancelled(true);

        Bukkit.getConsoleSender().sendMessage("§e[DEBUG3] Групповой режим: " + isInGroupChatMode(sender.getUniqueId()));
        Bukkit.getConsoleSender().sendMessage("§e[DEBUG3] Отменено: " + event.isCancelled());

        // Получаем группу игрока
        Group group = groupManager.getGroup(uuid);
        if (group == null) {
            sender.sendMessage("§cВы больше не состоите в группе.");
            groupChatUsers.remove(uuid);
            return;
        }

        // Отправляем сообщение всем участникам группы
        for (UUID memberUUID : group.getMembers()) {
            Player receiver = Bukkit.getPlayer(memberUUID);
            String formatted = formatResolver.resolve("group", sender, receiver, message);
            if (receiver != null && receiver.isOnline()) {
                receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            }
            if (receiver.equals(sender)) {
                Bukkit.getConsoleSender().sendMessage("[GroupChat] " + ChatColor.stripColor(formatted));
            }
        }

    }
}



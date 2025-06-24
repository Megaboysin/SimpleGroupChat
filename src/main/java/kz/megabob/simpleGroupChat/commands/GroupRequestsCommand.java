package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class GroupRequestsCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupRequestsCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        Set<UUID> requests = groupManager.getRequests(player.getUniqueId());

        if (requests.isEmpty()) {
            sender.sendMessage("§7Нет входящих заявок.");
            return true;
        }

        sender.sendMessage("§eЗаявки на вступление:");
        for (UUID uuid : requests) {
            Player reqPlayer = Bukkit.getPlayer(uuid);
            String name = (reqPlayer != null) ? reqPlayer.getName() : "Неизвестно";
            sender.sendMessage(" - §a" + name);
        }

        return true;
    }
}

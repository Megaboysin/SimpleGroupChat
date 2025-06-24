package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupListCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupListCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Список групп:");
        for (String name : groupManager.listGroups()) {
            sender.sendMessage(ChatColor.YELLOW + " - " + name);
        }
        return true;
    }
}
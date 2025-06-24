package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupLeaveCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupLeaveCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (groupManager.getGroup(uuid) == null) {
            sender.sendMessage("§cВы не состоите в группе.");
            return true;
        }

        boolean result = groupManager.leaveGroup(uuid);
        if (result) {
            sender.sendMessage("§eВы вышли из группы.");
        } else {
            sender.sendMessage("§cПроизошла ошибка при выходе из группы.");
        }

        return true;
    }
}

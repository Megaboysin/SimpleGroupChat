package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupRenameCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupRenameCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Использование: /gsetname <новое_имя>");
            return true;
        }

        String newName = args[0];
        if (groupManager.renameGroup(player.getUniqueId(), newName)) {
            sender.sendMessage("Группа переименована в: " + newName);
        } else {
            sender.sendMessage("Не удалось переименовать группу. Возможно, имя занято или вы не владелец.");
        }
        return true;
    }
}
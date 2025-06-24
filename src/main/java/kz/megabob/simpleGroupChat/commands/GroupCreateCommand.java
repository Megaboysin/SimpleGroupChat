package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCreateCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupCreateCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /gcreate <название>");
            return true;
        }

        String groupName = args[0];

        if (groupManager.getGroup(player.getUniqueId()) != null) {
            sender.sendMessage("§cВы уже состоите в группе. Выйдите из неё перед созданием новой.");
            return true;
        }

        if (groupManager.groupExists(groupName)) {
            sender.sendMessage("§cГруппа с таким именем уже существует.");
            return true;
        }

        groupManager.createGroup(groupName, player.getUniqueId());
        sender.sendMessage("§aГруппа \"" + groupName + "\" успешно создана!");
        return true;
    }
}

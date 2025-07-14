package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GroupDenyInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupDenyInviteCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        boolean denied = groupManager.denyInvite(player.getUniqueId());
        if (denied) {
            player.sendMessage("§cВы отклонили приглашение в группу.");
        } else {
            player.sendMessage("§cУ вас нет активных приглашений.");
        }

        return true;
    }
}

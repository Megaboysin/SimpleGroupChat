package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupAcceptInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupAcceptInviteCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько игроки могут использовать эту команду.");
            return true;
        }

        Group group = groupManager.getInvitationGroup(player.getUniqueId());
        if (group == null) {
            player.sendMessage("§cУ вас нет активных приглашений в группы.");
            return true;
        }

        boolean success = groupManager.acceptInvite(player.getUniqueId());
        if (success) {
            player.sendMessage("§aВы приняли приглашение и вступили в группу §f" + group.getName() + "§a.");
        } else {
            player.sendMessage("§cНе удалось принять приглашение.");
        }

        return true;
    }
}

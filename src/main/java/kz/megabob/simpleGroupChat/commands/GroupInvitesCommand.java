package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupInvitesCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupInvitesCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько игроки могут использовать эту команду.");
            return true;
        }

        Group group = groupManager.getGroup(player.getUniqueId());
        if (group != null) {
            player.sendMessage("§cВы уже состоите в группе.");
            return true;
        }

        var invitations = groupManager.getInvitations(player.getUniqueId());
        if (invitations.isEmpty()) {
            player.sendMessage("§7У вас нет приглашений в группы.");
            return true;
        }

        player.sendMessage("§eПриглашения в группы:");
        for (String groupName : invitations) {
            player.sendMessage(" §f- §a" + groupName);
        }

        return true;
    }
}

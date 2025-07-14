package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupInviteCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько игроки могут использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cИспользование: /ginvite <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cИгрок не найден или оффлайн.");
            return true;
        }

        Group group = groupManager.getGroup(player.getUniqueId());
        if (group == null || !group.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cВы не являетесь владельцем группы.");
            return true;
        }

        boolean success = groupManager.inviteToGroup(group.getName(), target.getUniqueId());
        if (success) {
            player.sendMessage("§aВы пригласили игрока §f" + target.getName() + " §aв свою группу.");
            target.sendMessage("§eИгрок §f" + player.getName() + " §eпригласил вас в свою группу §7(" + group.getName() + ")§e.");
            target.sendMessage("§7Введите §a/gaccept " + player.getName() + "§7, чтобы принять.");
        } else {
            player.sendMessage("§cНе удалось отправить приглашение. Возможно, игрок уже в группе или уже приглашён.");
        }

        return true;
    }
}

package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupKickCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupKickCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /gkick <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        if (groupManager.kickMember(player.getUniqueId(), target.getUniqueId())) {
            sender.sendMessage("§eИгрок был исключён из группы.");
            target.sendMessage("§cВы были исключены из группы.");
        } else {
            sender.sendMessage("§cНе удалось исключить игрока. Возможно, вы не владелец или он не в группе.");
        }

        return true;
    }
}

package kz.megabob.simpleGroupChat.commands;


import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;


public class GroupDenyRequestCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupDenyRequestCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /gdenyrequest <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        Group group = groupManager.getGroup(player.getUniqueId());
        if (group == null || !group.getOwner().equals(player.getUniqueId())) {
            sender.sendMessage("§cВы не владелец группы.");
            return true;
        }

        if (group.removeRequest(target.getUniqueId())) {
            sender.sendMessage("§aЗаявка игрока §f" + target.getName() + " §aотклонена.");
            target.sendMessage("§cВаша заявка была отклонена владельцем группы.");
        } else {
            sender.sendMessage("§cУ этого игрока нет активной заявки.");
        }

        return true;
    }
}

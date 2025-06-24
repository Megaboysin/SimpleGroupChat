package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupAcceptCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupAcceptCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Использование: /gaccept <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Игрок не найден или оффлайн.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        if (groupManager.acceptRequest(player.getUniqueId(), targetUUID)) {
            sender.sendMessage("Игрок принят в группу.");
            target.sendMessage("Вас приняли в группу!");
        } else {
            sender.sendMessage("Не удалось принять игрока. Возможно, нет заявки или вы не владелец.");
        }
        return true;
    }
}
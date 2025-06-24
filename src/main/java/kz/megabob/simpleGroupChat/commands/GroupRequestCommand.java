package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupRequestCommand implements CommandExecutor {
    private final GroupManager groupManager;

    public GroupRequestCommand(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Использование: /grequest <название_группы>");
            return true;
        }

        String targetGroup = args[0];
        if (groupManager.requestToJoin(targetGroup, player.getUniqueId())) {
            sender.sendMessage("Заявка отправлена группе: " + targetGroup);
        } else {
            sender.sendMessage("Не удалось отправить заявку. Возможно, вы уже в группе или такой группы не существует.");
        }
        return true;
    }
}
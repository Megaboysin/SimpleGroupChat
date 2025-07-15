package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupRequestCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupRequestCommand(GroupManager groupManager, LangManager langManager) {
        this.groupManager = groupManager;
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.General.OnlyPlayers"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (args.length < 1) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.General.EmptyCommand"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        String targetGroup = args[0];
        if (groupManager.requestToJoin(targetGroup, player.getUniqueId())) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Send"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("%group%", targetGroup));
        } else {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Fail"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return true;
    }
}

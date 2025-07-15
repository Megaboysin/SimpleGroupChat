package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCreateCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupCreateCommand(GroupManager groupManager, LangManager langManager) {
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

        String groupName = args[0];

        if (groupManager.getGroup(player.getUniqueId()) != null) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Create.AlreadyInGroup"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (groupManager.groupExists(groupName)) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Create.AlreadyExists"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        groupManager.createGroup(groupName, player.getUniqueId());
        String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Create.Success"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                .replace("%group%", groupName));
        return true;
    }
}

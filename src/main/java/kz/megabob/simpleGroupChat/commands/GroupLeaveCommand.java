package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupLeaveCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupLeaveCommand(GroupManager groupManager, LangManager langManager) {
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

        UUID uuid = player.getUniqueId();
        if (groupManager.getGroup(uuid) == null) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Leave.NotInGroup"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        boolean result = groupManager.leaveGroup(uuid);
        if (result) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Leave.Success"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Leave.Fail"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return true;
    }
}

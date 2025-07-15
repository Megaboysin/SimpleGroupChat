package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupListCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupListCommand(GroupManager groupManager, LangManager langManager) {
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

        String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.List.Header"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        for (String name : groupManager.listGroups()) {
            String msg2 = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.List.Entry"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2)
                    .replace("%group%", name));
        }
        return true;
    }
}

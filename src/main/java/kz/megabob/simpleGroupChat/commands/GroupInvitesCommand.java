package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupInvitesCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupInvitesCommand(GroupManager groupManager, LangManager langManager) {
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

        Group group = groupManager.getGroup(player.getUniqueId());
        if (group != null) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invites.AlreadyInGroup"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        var invitations = groupManager.getInvitations(player.getUniqueId());
        if (invitations.isEmpty()) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invites.None"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        player.sendMessage("§8§m------------------------------------");
        String header = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invites.ListHeader"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', header));

        for (String groupName : invitations) {
            String entry = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invites.ListFormat"))
                    .replace("%group%", groupName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', entry));
        }

        player.sendMessage("§8§m------------------------------------");
        return true;
    }
}

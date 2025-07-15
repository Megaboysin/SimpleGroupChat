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

public class GroupAcceptInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupAcceptInviteCommand(GroupManager groupManager, LangManager langManager) {
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

        Group group = groupManager.getInvitationGroup(player.getUniqueId());
        if (group == null) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.NoActive"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        boolean success = groupManager.acceptInvite(player.getUniqueId());
        if (success) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Accept"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("%group%", group.getName()));
        } else {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.AcceptFail"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return true;
    }
}

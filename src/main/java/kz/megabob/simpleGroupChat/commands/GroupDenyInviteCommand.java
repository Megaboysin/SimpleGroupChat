package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupDenyInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupDenyInviteCommand(GroupManager groupManager, LangManager langManager) {
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

        boolean denied = groupManager.denyInvite(player.getUniqueId());
        if (denied) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Deny"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.NoActive"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return true;
    }
}

package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupInviteCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupInviteCommand(GroupManager groupManager, LangManager langManager) {
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
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.General.NotFound"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        Group group = groupManager.getGroup(player.getUniqueId());
        if (group == null || !group.getOwner().equals(player.getUniqueId())) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.NotOwner"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        boolean success = groupManager.inviteToGroup(group.getName(), target.getUniqueId());
        if (success) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Send"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("%player%", target.getName()));
            String msg2 = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Notify"));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2)
                    .replace("%player%", player.getName())
                    .replace("%group%", group.getName()));
            String msg3 = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.AcceptHint"));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3)
                    .replace("%player%", player.getName()));
        } else {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Invite.Failed"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return true;
    }
}

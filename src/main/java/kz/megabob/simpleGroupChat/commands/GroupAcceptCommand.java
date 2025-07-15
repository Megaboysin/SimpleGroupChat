package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupAcceptCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupAcceptCommand(GroupManager groupManager, LangManager langManager) {
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

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.General.NotFound"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        if (groupManager.acceptRequest(player.getUniqueId(), targetUUID)) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Accept.Success"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("%player%", target.getName()));
            String msg2 = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Accept.Notify"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2)
                    .replace("%player%", player.getName()));
        } else {
            String msg3 = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Group.Accept.Fail"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg3));
        }
        return true;
    }
}

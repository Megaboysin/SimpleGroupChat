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

import java.util.Set;
import java.util.UUID;

public class GroupRequestsCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    public GroupRequestsCommand(GroupManager groupManager, LangManager langManager) {
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

        Set<UUID> requests = groupManager.getRequests(player.getUniqueId());

        if (requests.isEmpty()) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request..Requests.NoRequests"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Requests.List"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        for (UUID uuid : requests) {
            Player reqPlayer = Bukkit.getPlayer(uuid);
            String name = (reqPlayer != null) ? reqPlayer.getName() : HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Requests.Unknown"));
            String msgg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Requests.ShowPlayer"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgg)
                    .replace("%player%", name));
        }
        return true;
    }
}

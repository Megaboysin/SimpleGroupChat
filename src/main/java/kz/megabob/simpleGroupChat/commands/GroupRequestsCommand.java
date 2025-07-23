package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Requests.NoRequests"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        String header = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.Request.Requests.List"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', header));

        for (UUID uuid : requests) {
            Player reqPlayer = Bukkit.getPlayer(uuid);
            String name = (reqPlayer != null) ? reqPlayer.getName() : langManager.getDefault("messages.Request.Requests.Unknown");
            String msg = HexColorUtil.translateHexColorCodes(
                    langManager.getDefault("messages.Request.Requests.ShowPlayer")
                            .replace("%player%", name)
            );

            TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/g accept " + name));
            player.spigot().sendMessage(component);
        }

        return true;
    }
}

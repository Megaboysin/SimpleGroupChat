package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.handlers.GroupChatHandler;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GroupChatToggleCommand implements CommandExecutor, TabCompleter {
    private final GroupChatHandler handler;
    private final LangManager langManager;

    public GroupChatToggleCommand(GroupChatHandler handler, LangManager langManager) {
        this.handler = handler;
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.General.OnlyPlayers"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("toggle")) {
            handler.toggleGroupChat(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on" -> {
                if (handler.isInGroupChatMode(player.getUniqueId())) {
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("mmessages.GroupChatToggle.AlreadyOn"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else {
                    handler.setGroupChatMode(player, true);
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.GroupChatToggle.true"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
            }
            case "off" -> {
                if (!handler.isInGroupChatMode(player.getUniqueId())) {
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.GroupChatToggle.AlreadyOff"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                } else {
                    handler.setGroupChatMode(player, false);
                    String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.GroupChatToggle.false"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
            }
            case "status" -> {
                boolean enabled = handler.isInGroupChatMode(player.getUniqueId());
                player.sendMessage(langManager.getDefault(
                        enabled ? "messages.GroupChatToggle.true" : "messages.GroupChatToggle.false"
                ));
            }
            default -> {
                String msg = HexColorUtil.translateHexColorCodes(langManager.getDefault("messages.GroupChatToggle.Usage"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("on", "off", "status");
        }
        return List.of();
    }
}

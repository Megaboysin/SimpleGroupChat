package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.handlers.GroupChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GroupChatToggleCommand implements CommandExecutor, TabCompleter {
    private final GroupChatHandler handler;

    public GroupChatToggleCommand(GroupChatHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("toggle")) {
            handler.toggleGroupChat(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on" -> {
                if (handler.isInGroupChatMode(player.getUniqueId())) {
                    player.sendMessage("§eГрупповой чат уже включён.");
                } else {
                    handler.setGroupChatMode(player, true);
                    player.sendMessage("§aГрупповой чат включён.");
                }
            }
            case "off" -> {
                if (!handler.isInGroupChatMode(player.getUniqueId())) {
                    player.sendMessage("§eГрупповой чат уже выключен.");
                } else {
                    handler.setGroupChatMode(player, false);
                    player.sendMessage("§cГрупповой чат выключен.");
                }
            }
            case "status" -> {
                boolean enabled = handler.isInGroupChatMode(player.getUniqueId());
                player.sendMessage(enabled ? "§aГрупповой чат включён." : "§cГрупповой чат выключен.");
            }
            default -> {
                player.sendMessage("§cИспользование: /gchat [on|off|status]");
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

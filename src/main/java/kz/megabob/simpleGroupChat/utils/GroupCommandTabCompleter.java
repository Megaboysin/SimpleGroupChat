package kz.megabob.simpleGroupChat.utils;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GroupCommandTabCompleter implements TabCompleter {

    private final GroupManager groupManager;

    public GroupCommandTabCompleter(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player) || args.length == 0)
            return Collections.emptyList();

        // Команды без автодополнения
        switch (command.getName().toLowerCase()) {
            case "gleave":
            case "glist":
            case "gcreate":
            case "ginvites":
            case "grequests":
                return Collections.emptyList();
        }

        String prefix = args[0].toLowerCase();

        switch (command.getName().toLowerCase()) {
            case "grequest" -> {
                return groupManager.listGroups().stream()
                        .filter(name -> args.length == 1 && name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            case "gacceptrequest", "gdenyrequest" -> {
                return groupManager.getRequests(player.getUniqueId()).stream()
                        .map(uuid -> {
                            Player p = Bukkit.getPlayer(uuid);
                            return (p != null) ? p.getName() : null;
                        })
                        .filter(name -> name != null && args.length == 1 && name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            case "gkick" -> {
                var group = groupManager.getGroup(player.getUniqueId());
                if (group == null) return Collections.emptyList();

                return group.getMembers().stream()
                        .map(uuid -> {
                            Player p = Bukkit.getPlayer(uuid);
                            return (p != null && !p.getUniqueId().equals(player.getUniqueId())) ? p.getName() : null;
                        })
                        .filter(name -> name != null && args.length == 1 && name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            case "ginvite" -> {
                Group group = groupManager.getGroup(player.getUniqueId());
                if (group == null) return Collections.emptyList();

                // Уже приглашённые игроки
                List<UUID> invited = group.getInvitations().stream().toList();

                return Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))          // не сам себя
                        .filter(p -> groupManager.getGroup(p.getUniqueId()) == null)         // не в группе
                        .filter(p -> !invited.contains(p.getUniqueId()))                     // ещё не приглашён
                        .filter(p -> p.getName().toLowerCase().startsWith(prefix))
                        .map(Player::getName)
                        .collect(Collectors.toList());
            }

            default -> {
                return Collections.emptyList();
            }

        }
    }
}

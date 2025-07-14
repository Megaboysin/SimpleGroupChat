package kz.megabob.simpleGroupChat.utils;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GroupCommandTabCompleter implements TabCompleter {

    private final GroupManager groupManager;

    public GroupCommandTabCompleter(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) return Collections.emptyList();

        // Команды без автодополнения
        switch (command.getName().toLowerCase()) {
            case "gleave":
            case "glist":
            case "gcreate":
            case "ginvites":
            case "grequests":
                return Collections.emptyList();
        }

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
            case "gkick", "ginvite" -> {
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
        }

        return Collections.emptyList();
    }
}

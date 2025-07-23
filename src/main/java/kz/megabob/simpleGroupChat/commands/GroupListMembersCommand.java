package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GroupListMembersCommand implements CommandExecutor {

    private final GroupManager groupManager;
    private final LangManager lang;
    private final int MEMBERS_PER_PAGE = 8;

    public GroupListMembersCommand(GroupManager groupManager, LangManager lang) {
        this.groupManager = groupManager;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only.");
            return true;
        }

        String langKey = lang.getDefaultLang();
        Group group = groupManager.getGroup(player.getUniqueId());

        if (group == null) {
            player.sendMessage(lang.get(langKey, "messages.Group.NotInGroup"));
            return true;
        }

        List<UUID> members = new ArrayList<>(group.getMembers());
        int totalPages = (int) Math.ceil((double) members.size() / MEMBERS_PER_PAGE);
        int page = 1;

        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(lang.get(langKey, "messages.Group.Members.InvalidPage").replace("%page%", args[0]));
                return true;
            }
        }

        if (page < 1 || page > totalPages) {
            player.sendMessage(lang.get(langKey, "messages.Group.Members.PageIndicator")
                    .replace("%current%", String.valueOf(page))
                    .replace("%total%", String.valueOf(totalPages)));
            return true;
        }

        int start = (page - 1) * MEMBERS_PER_PAGE;
        int end = Math.min(start + MEMBERS_PER_PAGE, members.size());

        player.sendMessage("§8§m------------------------------------");
        player.sendMessage(lang.get(langKey, "messages.Group.Members.Header")
                .replace("%group%", group.getName())
                + " §7(" + lang.get(langKey, "messages.Group.Members.PageIndicator")
                .replace("%current%", String.valueOf(page))
                .replace("%total%", String.valueOf(totalPages)) + ")");

        for (int i = start; i < end; i++) {
            UUID uuid = members.get(i);
            Player member = Bukkit.getPlayer(uuid);
            String name = (member != null) ? member.getName() : Bukkit.getOfflinePlayer(uuid).getName();
            String role = uuid.equals(group.getOwner())
                    ? lang.get(langKey, "messages.Group.Members.OwnerSuffix")
                    : "";
            player.sendMessage("§a- §f" + name + " " + role);
        }

        if (totalPages > 1) {
            TextComponent nav = new TextComponent("§7[ ");

            if (page > 1) {
                TextComponent prev = new TextComponent("§b◀ " + lang.get(langKey, "messages.Group.Members.PreviousPage") + " |");
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/glistmembers " + (page - 1)));
                prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7" + lang.get(langKey, "messages.Group.Members.PageIndicator")
                                .replace("%current%", String.valueOf(page - 1))
                                .replace("%total%", String.valueOf(totalPages))).create()));
                nav.addExtra(prev);
                nav.addExtra(" ");
            }

            if (page < totalPages) {
                TextComponent next = new TextComponent("§b" + lang.get(langKey, "messages.Group.Members.NextPage") + " ▶");
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/glistmembers " + (page + 1)));
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7" + lang.get(langKey, "messages.Group.Members.PageIndicator")
                                .replace("%current%", String.valueOf(page + 1))
                                .replace("%total%", String.valueOf(totalPages))).create()));
                nav.addExtra(next);
            }

            nav.addExtra("§7 ]");
            player.spigot().sendMessage(nav);
        }

        player.sendMessage("§8§m------------------------------------");
        return true;
    }
}

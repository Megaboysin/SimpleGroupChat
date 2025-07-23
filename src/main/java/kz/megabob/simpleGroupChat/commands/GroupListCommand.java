package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.groups.GroupManager;
import kz.megabob.simpleGroupChat.language.LangManager;
import kz.megabob.simpleGroupChat.utils.HexColorUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GroupListCommand implements CommandExecutor {
    private final GroupManager groupManager;
    private final LangManager langManager;

    private static final int GROUPS_PER_PAGE = 7;

    public GroupListCommand(GroupManager groupManager, LangManager langManager) {
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

        List<String> groups = groupManager.listGroups();
        int totalPages = (int) Math.ceil((double) groups.size() / GROUPS_PER_PAGE);
        int page = 1;

        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1 || page > totalPages) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                String invalid = langManager.get(String.valueOf(player), "messages.Group.Help.InvalidPage")
                        .replace("%page%", args[0]);
                player.sendMessage(HexColorUtil.translateHexColorCodes(invalid));
                return true;
            }
        }

        int start = (page - 1) * GROUPS_PER_PAGE;
        int end = Math.min(start + GROUPS_PER_PAGE, groups.size());

        String header = langManager.get(String.valueOf(player), "messages.Group.List.Header");
        player.sendMessage(HexColorUtil.translateHexColorCodes(header));

        for (int i = start; i < end; i++) {
            String name = groups.get(i);
            String entry = langManager.get(String.valueOf(player), "messages.Group.List.Entry")
                    .replace("%group%", name);
            player.sendMessage(HexColorUtil.translateHexColorCodes(entry));
        }

        // Навигация
        TextComponent nav = new TextComponent("");

        if (page > 1) {
            String prevLabel = langManager.get(String.valueOf(player), "messages.Group.Help.PreviousPage");
            TextComponent prev = new TextComponent(HexColorUtil.translateHexColorCodes("&a◀ " + prevLabel + " "));
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " " + (page - 1)));
            nav.addExtra(prev);
        }

        String current = langManager.get(String.valueOf(player), "messages.Group.Help.PageIndicator")
                .replace("%current%", String.valueOf(page))
                .replace("%total%", String.valueOf(totalPages));
        nav.addExtra(new TextComponent(HexColorUtil.translateHexColorCodes("&7" + current + " ")));

        if (page < totalPages) {
            String nextLabel = langManager.get(String.valueOf(player), "messages.Group.Help.NextPage");
            TextComponent next = new TextComponent(HexColorUtil.translateHexColorCodes("&a " + nextLabel + " ▶"));
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " " + (page + 1)));
            nav.addExtra(next);
        }

        player.spigot().sendMessage(nav);
        return true;
    }
}

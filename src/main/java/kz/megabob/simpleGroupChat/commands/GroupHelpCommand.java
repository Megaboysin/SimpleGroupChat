package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.language.LangManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupHelpCommand implements CommandExecutor {

    private final LangManager lang;

    private final List<String> commandList = List.of(
            "gcreate", "glist", "grequest", "gacceptrequest", "gacceptinvite",
            "gdenyrequest", "gdenyinvite", "gkick", "ginvite", "ginvites",
            "gleave", "grequests", "gchat", "glistmembers"
    );

    private final int COMMANDS_PER_PAGE = 6;

    public GroupHelpCommand(LangManager lang) {
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String langKey = lang.getDefaultLang();

        int totalPages = (int) Math.ceil((double) commandList.size() / COMMANDS_PER_PAGE);
        int page = 1;

        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                sender.sendMessage(lang.get(langKey, "messages.Group.Help.InvalidPage").replace("%page%", args[0]));
                return true;
            }
        }

        if (page < 1 || page > totalPages) {
            sender.sendMessage("§c" + lang.get(langKey, "messages.Group.Help.PageIndicator")
                    .replace("%current%", String.valueOf(page))
                    .replace("%total%", String.valueOf(totalPages)));
            return true;
        }

        int start = (page - 1) * COMMANDS_PER_PAGE;
        int end = Math.min(start + COMMANDS_PER_PAGE, commandList.size());

        sender.sendMessage("§8§m------------------------------------");
        sender.sendMessage(lang.get(langKey, "commands.ghelp.title") + " §7(" +
                lang.get(langKey, "messages.Group.Help.PageIndicator")
                        .replace("%current%", String.valueOf(page))
                        .replace("%total%", String.valueOf(totalPages)) + ")");

        for (int i = start; i < end; i++) {
            String cmd = commandList.get(i);
            String usage = lang.get(langKey, "commands." + cmd + ".usage");
            String desc = lang.get(langKey, "commands." + cmd + ".description");
            sender.sendMessage("§a" + usage + " §7- " + desc);
        }

        // Кликабельная навигация
        if (sender instanceof org.bukkit.entity.Player player && totalPages > 1) {
            TextComponent nav = new TextComponent("§7[ ");

            if (page > 1) {
                TextComponent prev = new TextComponent("§b◀ " + lang.get(langKey, "messages.Group.Help.PreviousPage") + " |");
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ghelp " + (page - 1)));
                prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7" + lang.get(langKey, "messages.Group.Help.PageIndicator")
                                .replace("%current%", String.valueOf(page - 1))
                                .replace("%total%", String.valueOf(totalPages)))
                                .create()));
                nav.addExtra(prev);
                nav.addExtra(" ");
            }

            if (page < totalPages) {
                TextComponent next = new TextComponent("§b" + lang.get(langKey, "messages.Group.Help.NextPage") + " ▶");
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ghelp " + (page + 1)));
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7" + lang.get(langKey, "messages.Group.Help.PageIndicator")
                                .replace("%current%", String.valueOf(page + 1))
                                .replace("%total%", String.valueOf(totalPages)))
                                .create()));
                nav.addExtra(next);
            }

            nav.addExtra("§7 ]");
            player.spigot().sendMessage(nav);
        }

        sender.sendMessage("§8§m------------------------------------");
        return true;
    }
}

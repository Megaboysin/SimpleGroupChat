package kz.megabob.simpleGroupChat.commands;

import kz.megabob.simpleGroupChat.language.LangManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GroupReloadCommand implements CommandExecutor {

    private final LangManager lang;

    public GroupReloadCommand(LangManager lang) {
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        lang.reloadLanguages();
        sender.sendMessage(lang.get(lang.getDefaultLang(), "messages.Reload.Success"));
        return true;
    }
}

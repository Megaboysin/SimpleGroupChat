package kz.megabob.simpleGroupChat;

import kz.megabob.simpleGroupChat.commands.*;
import kz.megabob.simpleGroupChat.handlers.*;
import kz.megabob.simpleGroupChat.groups.*;
import kz.megabob.simpleGroupChat.language.*;
import kz.megabob.simpleGroupChat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SimpleGroupChat extends JavaPlugin {
    public static SimpleGroupChat instance;
    private GroupChatHandler groupChatHandler;
    private FileConfiguration config;
    private GroupManager groupManager;
    private FormatResolver formatResolver;
    private LangManager langManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveLangFile("lang/ru.yml");
        saveLangFile("lang/eng.yml");
        config = this.getConfig();
        boolean usePapi = config.getBoolean("use-placeholderapi");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI is found. Support enabled.");
        } else {
            getLogger().warning("PlaceholderAPI NOT found. Non-PAPI formats are used.");
            getLogger().warning("usePapi is set to false");
            usePapi = false;
        }

        String lang = getConfig().getString("language", "eng");
        this.langManager = new LangManager(this, lang);
        this.groupManager = new GroupManager(langManager);
        this.formatResolver = new FormatResolver(this, usePapi, groupManager);
        this.groupChatHandler = new GroupChatHandler(groupManager, formatResolver, langManager);
        GroupChatToggleCommand toggleCommand = new GroupChatToggleCommand(groupChatHandler, langManager);
        GroupCommandTabCompleter tabCompleter = new GroupCommandTabCompleter(groupManager);

        getCommand("gcreate").setExecutor(new GroupCreateCommand(groupManager, langManager));
        getCommand("glist").setExecutor(new GroupListCommand(groupManager, langManager));
        getCommand("grequest").setExecutor(new GroupRequestCommand(groupManager, langManager));
        getCommand("gacceptrequest").setExecutor(new GroupAcceptCommand(groupManager, langManager));
        getCommand("gacceptinvite").setExecutor(new GroupAcceptInviteCommand(groupManager, langManager));
        getCommand("gleave").setExecutor(new GroupLeaveCommand(groupManager, langManager));
        getCommand("grequests").setExecutor(new GroupRequestsCommand(groupManager, langManager));
        getCommand("gdenyrequest").setExecutor(new GroupDenyRequestCommand(groupManager, langManager));
        getCommand("gdenyinvite").setExecutor(new GroupDenyInviteCommand(groupManager, langManager));
        getCommand("gkick").setExecutor(new GroupKickCommand(groupManager, langManager));
        getCommand("ginvites").setExecutor(new GroupInvitesCommand(groupManager, langManager));
        getCommand("gchat").setExecutor(toggleCommand);
        getCommand("ginvite").setExecutor(new GroupInviteCommand(groupManager, langManager));
        getCommand("ghelp").setExecutor(new GroupHelpCommand(langManager));
        getCommand("glistmembers").setExecutor(new GroupListMembersCommand(groupManager, langManager));
        getCommand("greload").setExecutor(new GroupReloadCommand(langManager));

        getCommand("grequest").setTabCompleter(tabCompleter);
        getCommand("gacceptrequest").setTabCompleter(tabCompleter);
        getCommand("gdenyrequest").setTabCompleter(tabCompleter);
        getCommand("gkick").setTabCompleter(tabCompleter);
        getCommand("ginvite").setTabCompleter(tabCompleter);
        getCommand("glist").setTabCompleter(tabCompleter);
        getCommand("gcreate").setTabCompleter(tabCompleter);
        getCommand("grequests").setTabCompleter(tabCompleter);
        getCommand("gleave").setTabCompleter(tabCompleter);
        getCommand("ginvites").setTabCompleter(tabCompleter);
        getCommand("gchat").setTabCompleter(toggleCommand);


        getServer().getPluginManager().registerEvents(groupChatHandler, this);

        getServer().getLogger().info("§2[SGC] Plugin is ready");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("§4[SGC] Plugin was shutdown!");
    }

    private void saveLangFile(String path) {
        File targetFile = new File(getDataFolder(), path);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            saveResource(path, false);
        }
    }

    public static SimpleGroupChat getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }
}

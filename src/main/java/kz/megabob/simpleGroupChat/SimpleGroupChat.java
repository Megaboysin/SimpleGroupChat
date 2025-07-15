package kz.megabob.simpleGroupChat;

import kz.megabob.simpleGroupChat.commands.*;
import kz.megabob.simpleGroupChat.handlers.*;
import kz.megabob.simpleGroupChat.groups.*;
import kz.megabob.simpleGroupChat.language.*;
import kz.megabob.simpleGroupChat.utils.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleGroupChat extends JavaPlugin {
    private GroupChatHandler groupChatHandler;
    private FileConfiguration config;
    private GroupManager groupManager;
    private FormatResolver formatResolver;
    private LangManager langManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        String lang = getConfig().getString("language", "eng");
        this.langManager = new LangManager(this, lang);
        this.groupManager = new GroupManager(langManager);
        this.formatResolver = new FormatResolver(this, true, groupManager);
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

        getCommand("grequest").setTabCompleter(tabCompleter);
        // getCommand("gaccept").setTabCompleter(tabCompleter);
        // getCommand("gdeny").setTabCompleter(tabCompleter);
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
}

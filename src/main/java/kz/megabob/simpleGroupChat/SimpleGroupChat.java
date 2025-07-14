package kz.megabob.simpleGroupChat;

import kz.megabob.simpleGroupChat.commands.*;
import kz.megabob.simpleGroupChat.handlers.*;
import kz.megabob.simpleGroupChat.groups.*;
import kz.megabob.simpleGroupChat.utils.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleGroupChat extends JavaPlugin {
    private GroupChatHandler groupChatHandler;
    private FileConfiguration config;
    private GroupManager groupManager;
    private FormatResolver formatResolver;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        this.groupManager = new GroupManager();
        this.formatResolver = new FormatResolver(this, true);
        this.groupChatHandler = new GroupChatHandler(groupManager, formatResolver);
        GroupChatToggleCommand toggleCommand = new GroupChatToggleCommand(groupChatHandler);
        GroupCommandTabCompleter tabCompleter = new GroupCommandTabCompleter(groupManager);


        getCommand("gcreate").setExecutor(new GroupCreateCommand(groupManager));
        getCommand("glist").setExecutor(new GroupListCommand(groupManager));
        getCommand("grequest").setExecutor(new GroupRequestCommand(groupManager));
        getCommand("gacceptrequest").setExecutor(new GroupAcceptCommand(groupManager));
        getCommand("gacceptinvite").setExecutor(new GroupAcceptInviteCommand(groupManager));
        getCommand("gleave").setExecutor(new GroupLeaveCommand(groupManager));
        getCommand("grequests").setExecutor(new GroupRequestsCommand(groupManager));
        getCommand("gdenyrequest").setExecutor(new GroupDenyRequestCommand(groupManager));
        getCommand("gdenyinvite").setExecutor(new GroupDenyInviteCommand(groupManager));
        getCommand("gkick").setExecutor(new GroupKickCommand(groupManager));
        getCommand("ginvites").setExecutor(new GroupInvitesCommand(groupManager));
        getCommand("gchat").setExecutor(toggleCommand);
        getCommand("ginvite").setExecutor(new GroupInviteCommand(groupManager));

        getCommand("grequest").setTabCompleter(tabCompleter);
        getCommand("gaccept").setTabCompleter(tabCompleter);
        getCommand("gdeny").setTabCompleter(tabCompleter);
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

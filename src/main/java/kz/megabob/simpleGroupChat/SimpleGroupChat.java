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


        getCommand("gcreate").setExecutor(new GroupCreateCommand(groupManager));
        getCommand("glist").setExecutor(new GroupListCommand(groupManager));
        getCommand("gsetname").setExecutor(new GroupRenameCommand(groupManager));
        getCommand("grequest").setExecutor(new GroupRequestCommand(groupManager));
        getCommand("gaccept").setExecutor(new GroupAcceptCommand(groupManager));
        getCommand("gleave").setExecutor(new GroupLeaveCommand(groupManager));
        getCommand("grequests").setExecutor(new GroupRequestsCommand(groupManager));
        getCommand("gdeny").setExecutor(new GroupDenyCommand(groupManager));
        getCommand("gkick").setExecutor(new GroupKickCommand(groupManager));
        getCommand("gchat").setExecutor(toggleCommand);
        getCommand("gchat").setTabCompleter(toggleCommand);

        getServer().getPluginManager().registerEvents(groupChatHandler, this);

        getServer().getLogger().info("§2[SGC] Plugin is ready");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("§4[SGC] Plugin was shutdown!");
    }
}

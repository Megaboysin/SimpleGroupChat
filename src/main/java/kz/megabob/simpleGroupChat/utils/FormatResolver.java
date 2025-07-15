package kz.megabob.simpleGroupChat.utils;

import kz.megabob.simpleGroupChat.SimpleGroupChat;
import kz.megabob.simpleGroupChat.groups.Group;
import kz.megabob.simpleGroupChat.groups.GroupManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FormatResolver {

    private final SimpleGroupChat plugin;
    private final boolean usePapi;
    private final GroupManager groupManager;

    public FormatResolver(SimpleGroupChat plugin, boolean usePapi, GroupManager groupManager) {
        this.plugin = plugin;
        this.usePapi = usePapi;
        this.groupManager = groupManager;
    }

    public String resolve(String channel, Player sender, Player receiver, String message) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(channel.toLowerCase());

        if (section == null) {
            return "§c[ERROR] Invalid format section:" + channel;
        }



        String format = section.getString("msgformat", "{player}: {message}");
        String prefix = section.getString("prefix", "");
        String groupName = "";
        Group group = groupManager.getGroup(sender.getUniqueId());
        if (group != null) groupName = group.getName();
        String nameColor = section.getString("name-color", "");
        String receivercolor = section.getString("receiver-color", "");
        String messageColor = section.getString("message-color", "");
        String placeholderPrefix = section.getString("placeholder-prefix", "");
        String receiverprefix = section.getString("receiver-prefix", "");
        String worldName = sender.getWorld().getName();

        String worldColor = switch (worldName.toLowerCase()) {
            case "world" -> "§2";
            case "world_nether" -> "§4";
            case "world_the_end" -> "§5";
            default -> "§3";
        };

        String worldPrefix = "";
        ConfigurationSection worldPrefixSection = section.getConfigurationSection("world-prefix");
        if (worldPrefixSection != null && worldPrefixSection.getBoolean("enabled")) {
            String worldFormat = worldPrefixSection.getString("format", "§8[{world}] ");
            worldPrefix = worldFormat.replace("{world}", sender.getWorld().getName());
        }

        String playerName = sender.getName();
        String receiverName = (receiver != null) ? receiver.getName() : "";

        if (usePapi) {
            format = PlaceholderAPI.setPlaceholders(sender, format);
            prefix = PlaceholderAPI.setPlaceholders(sender, prefix);
            receiverprefix = PlaceholderAPI.setPlaceholders(receiver, receiverprefix);
            placeholderPrefix = PlaceholderAPI.setPlaceholders(sender, placeholderPrefix);
            if (placeholderPrefix == null || placeholderPrefix.equals("%luckperms_prefix%")) {
                placeholderPrefix = "";
            }
            if (receiverprefix == null || receiverprefix.equals("%luckperms_prefix%")) {
                receiverprefix = "";
            }
        }

        String resolved = format
                .replace("{world-color}", worldColor)
                .replace("{world-prefix}", worldPrefix)
                .replace("{prefix}", prefix)
                .replace("{group}", groupName)
                .replace("{placeholder-prefix}", placeholderPrefix)
                .replace("{name-color}", nameColor)
                .replace("{message-color}", messageColor)
                .replace("{player}", playerName)
                .replace("{receiver-prefix}", receiverprefix)
                .replace("{receiver-color}", receivercolor)
                .replace("{receiver}", receiverName)
                .replace("{sender}", playerName)
                .replace("{message}", message);

        return HexColorUtil.translateHexColorCodes(resolved);
    }
}
package kz.megabob.simpleGroupChat.language;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LangManager {
    private final Map<String, YamlConfiguration> languages = new HashMap<>();
    private final String defaultLang;
    private final JavaPlugin plugin;

    public LangManager(JavaPlugin plugin, String defaultLang) {
        this.plugin = plugin;
        this.defaultLang = defaultLang.toLowerCase();
        loadLanguage("ru");
        loadLanguage("eng");
    }

    private void loadLanguage(String lang) {
        try {
            InputStream stream = plugin.getResource("lang/" + lang + ".yml");

            if (stream == null) {
                System.out.println("[LangManager] Could not find resource: lang/" + lang + ".yml");
                return;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(stream, StandardCharsets.UTF_8)
            );
            languages.put(lang, config);
            System.out.println("[LangManager] Loaded keys for " + lang + ": " + config.getKeys(true));
            for (String key : config.getKeys(true)) {
                System.out.println("  - " + key);
            }
        } catch (Exception e) {
            System.out.println("[LangManager] Failed to load " + lang + ".yml");
            e.printStackTrace();
        }
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public String get(String lang, String key) {
        YamlConfiguration config = languages.getOrDefault(lang, languages.get(defaultLang));
        return config.getString(key, "§cMissing key: " + key);
    }

    public String getDefault(String key) {
        return get(defaultLang, key);
    }
}

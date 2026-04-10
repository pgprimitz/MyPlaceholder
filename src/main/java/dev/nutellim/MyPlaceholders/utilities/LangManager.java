package dev.nutellim.MyPlaceholders.utilities;

import dev.nutellim.MyPlaceholders.MyPlaceholder;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LangManager {

    private final MyPlaceholder plugin;
    private FileConfig langFile;
    private final List<String> availableLanguages = Arrays.asList(
        "ar_SA", "cs_CZ", "da_DK", "de_DE", "en_US", "es_ES", "fr_FR", "hu_HU",
        "id_ID", "it_IT", "iw_IL", "ja_JP", "ko_KR", "lt_LT", "nl_NL", "pl_PL",
        "pt_BR", "ro_RO", "ru_RU", "sk_SK", "sv_SE", "th_TH", "tr_TR", "uk_UA",
        "vi_VN", "zh_CN"
    );

    public LangManager(MyPlaceholder plugin) {
        this.plugin = plugin;
        loadAllLanguages();
        loadSelectedLanguage();
    }

    private void loadAllLanguages() {
        // Generate all language files if they don't exist
        for (String langCode : availableLanguages) {
            new FileConfig(plugin, "lang/" + langCode + ".yml");
            if (plugin.isDebug()) {
                plugin.getLogger().info("Loaded language: " + langCode);
            }
        }
    }

    private void loadSelectedLanguage() {
        String lang = plugin.getMainConfig().getRawString("lang");
        
        // Default to en_US if not set or invalid
        if (lang == null || lang.isEmpty() || !availableLanguages.contains(lang)) {
            lang = "en_US";
        }
        
        langFile = new FileConfig(plugin, "lang/" + lang + ".yml");
        if (plugin.isDebug()) {
            plugin.getLogger().info("Using language: " + lang);
        }
    }

    public void reload() {
        loadAllLanguages();
        loadSelectedLanguage();
    }

    public String get(String path) {
        String value = langFile.getRawString(path);
        return value != null ? value : "§c[Missing lang key: " + path + "]";
    }

    public String getPrefix() {
        return get("prefix");
    }
}

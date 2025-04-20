package marvtechnology.marvtime.lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class LanguageManager {

    private static final Map<String, YamlConfiguration> LANGS = new HashMap<>();
    private static final List<String> SUPPORTED = List.of("en", "ja", "zh", "ko", "es", "fr", "de");

    public static void loadLanguages(Plugin plugin) {
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists() && !langDir.mkdirs()) {
            plugin.getLogger().warning("[MarvTime] langフォルダの作成に失敗しました。");
            return;
        }

        for (String code : SUPPORTED) {
            try {
                File langFile = new File(langDir, code + ".yml");

                if (!langFile.exists()) {
                    InputStream in = LanguageManager.class.getResourceAsStream("/lang/" + code + ".yml");
                    if (in != null) {
                        Files.copy(in, langFile.toPath());
                    }
                }

                LANGS.put(code, YamlConfiguration.loadConfiguration(langFile));

            } catch (Exception e) {
                plugin.getLogger().warning("[MarvTime] 言語ファイル読み込み失敗: " + code);
            }
        }
    }

    public static String get(Player player, String key, Map<String, String> placeholders) {
        String langCode = player.locale().getLanguage();
        YamlConfiguration lang = LANGS.getOrDefault(langCode, LANGS.get("en"));
        String msg = lang.getString(key, key);

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        return msg;
    }
}


package marvtechnology.marvtime;

import marvtechnology.marvtime.command.MarvTimeCommand;
import marvtechnology.marvtime.config.PluginConfig;
import marvtechnology.marvtime.data.*;
import marvtechnology.marvtime.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Marvtime extends JavaPlugin {

    private static StorageProvider storage;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        LanguageManager.loadLanguages(this);

        PluginConfig config = new PluginConfig(getConfig());

        if (config.getStorageType() == StorageType.MARIADB) {
            storage = new MariaDbStorage(config.getMariaDbConfig(), getLogger());
        } else {
            storage = new JsonStorage(this);
        }

        PluginCommand marvtimeCommand = getCommand("marvtime");
        if (marvtimeCommand != null) {
            marvtimeCommand.setExecutor(new MarvTimeCommand(Bukkit.getWorld("world")));
        } else {
            getLogger().warning("コマンド 'marvtime' が plugin.yml に定義されていません。");
        }

        getLogger().info("MarvTime プラグインが起動しました。");
    }

    @Override
    public void onDisable() {
        getLogger().info("MarvTime プラグインが停止しました。");
    }

    public static StorageProvider getStorage() {
        return storage;
    }
}

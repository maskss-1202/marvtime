package marvtechnology.marvtime.config;

import marvtechnology.marvtime.data.StorageType;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {

    private final StorageType storageType;
    private final MariaDbConfig mariadb;

    public PluginConfig(FileConfiguration config) {
        String typeStr = config.getString("storage.type", "json").toLowerCase();

        // switch 警告回避のため if 文で書き換え
        if ("mariadb".equals(typeStr)) {
            this.storageType = StorageType.MARIADB;
        } else {
            this.storageType = StorageType.JSON;
        }

        this.mariadb = new MariaDbConfig(
                config.getString("storage.mariadb.host", "localhost"),
                config.getInt("storage.mariadb.port", 3306),
                config.getString("storage.mariadb.database", "marvtime"),
                config.getString("storage.mariadb.username", "root"),
                config.getString("storage.mariadb.password", "")
        );
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public MariaDbConfig getMariaDbConfig() {
        return mariadb;
    }

    public record MariaDbConfig(
            String host,
            int port,
            String database,
            String username,
            String password
    ) {}
}

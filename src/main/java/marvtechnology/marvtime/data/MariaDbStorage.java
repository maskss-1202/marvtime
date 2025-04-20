package marvtechnology.marvtime.data;

import marvtechnology.marvtime.config.PluginConfig;

import java.sql.*;
import java.util.logging.Logger;

public class MariaDbStorage implements StorageProvider {

    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final Logger logger;

    public MariaDbStorage(PluginConfig.MariaDbConfig config, Logger logger) {
        this.user = config.username();
        this.password = config.password();
        this.logger = logger;
        this.jdbcUrl = "jdbc:mariadb://" + config.host() + ":" + config.port() + "/" + config.database()
                + "?useUnicode=true&characterEncoding=utf8";

        setupDatabase();
    }

    private void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS marvtime_records (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    year BIGINT NOT NULL,
                    day BIGINT NOT NULL,
                    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            logger.info("MariaDB: テーブル marvtime_records を確認しました。");
        } catch (SQLException e) {
            logger.severe("MariaDB 初期化エラー: " + e.getMessage());
        }
    }

    @Override
    public void saveTime(long year, long day) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO marvtime_records (year, day) VALUES (?, ?)"
             )) {
            stmt.setLong(1, year);
            stmt.setLong(2, day);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("MariaDB 保存失敗: " + e.getMessage());
        }
    }
}

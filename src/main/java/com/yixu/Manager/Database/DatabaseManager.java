package com.yixu.Manager.Database;

import com.yixu.AdvancedEnchantingTable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private final JavaPlugin plugin;
    private HikariDataSource dataSource;

    private final String databaseType;

    private final String sqliteName = AdvancedEnchantingTable.getInstance().getName();

    public DatabaseManager(JavaPlugin plugin) throws SQLException {
        this.plugin = plugin;
        this.databaseType = getDatabaseConfig().getString("Type").toLowerCase();
        initDataSource();
        createTable();
    }

    private FileConfiguration getDatabaseConfig() {
        return AdvancedEnchantingTable.getInstance().getConfigManager().getConfig("database.yml");
    }

    private void initDataSource() throws SQLException {
        FileConfiguration databaseConfig = getDatabaseConfig();

        if (databaseType.equals("sqlite")) {
            File dbFile = new File(plugin.getDataFolder(), sqliteName + ".db");
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    throw new SQLException("无法创建 SQLite 数据库文件", e);
                }
            }
            return;
        }

        if (databaseType.equals("mysql")) {
            String host = databaseConfig.getString("mysql.host");
            int port = databaseConfig.getInt("mysql.port");
            String database = databaseConfig.getString("mysql.database");
            String user = databaseConfig.getString("mysql.user");
            String password = databaseConfig.getString("mysql.password");
            String parameters = databaseConfig.getString("mysql.parameters", "");
            int poolSize = databaseConfig.getInt("mysql.pool-size", 10);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + parameters);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.setMaximumPoolSize(poolSize);
            hikariConfig.setPoolName(AdvancedEnchantingTable.class.getName() + "-HikariPool");

            this.dataSource = new HikariDataSource(hikariConfig);
            return;
        }

        throw new IllegalArgumentException("不支持的数据库类型: " + databaseType);
    }

    private void createTable() throws SQLException {
        String sql = "";

        if (databaseType.equals("sqlite")) {
            sql = """
                        CREATE TABLE IF NOT EXISTS player_enchants (
                            uuid CHAR(36) NOT NULL PRIMARY KEY,
                            enchants TEXT NOT NULL
                        );                    
                    """;
        }

        if (databaseType.equals("mysql")) {
            sql = """
                        CREATE TABLE IF NOT EXISTS player_enchants (
                            uuid CHAR(36) NOT NULL PRIMARY KEY,
                            enchants TEXT NOT NULL
                        );                    
                    """;
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public Connection getConnection() throws SQLException {
        if (databaseType.equals("sqlite")) {
            String filePath = plugin.getDataFolder() + "/" + sqliteName + ".db";
            return DriverManager.getConnection("jdbc:sqlite:" + filePath);
        }

        if (databaseType.equals("mysql")) {
            return dataSource.getConnection();
        }

        throw new SQLException("不支持的数据库类型: " + databaseType);
    }

    public void onClosed() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

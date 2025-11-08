package com.yixu.Util.Database.Enchant;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yixu.AdvancedEnchantingTable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EnchantDatabaseOperation {

    private static final Gson gson = new Gson();

    // 查询玩家附魔信息
    public static List<Enchantment> getEnchants(UUID uuid) {
        String query = "SELECT enchants FROM player_enchants WHERE uuid = ?";
        try (Connection connection = AdvancedEnchantingTable.getInstance().getDatabaseManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String enchantsJson = resultSet.getString("enchants");

                if (enchantsJson.isEmpty()) {
                    return null;
                }

                // 使用 TypeToken 来反序列化为 List<NamespacedKey> 然后再转回 Enchantment
                List<String> enchantmentKeys = gson.fromJson(enchantsJson, new TypeToken<List<String>>(){}.getType());
                List<Enchantment> enchantments = new ArrayList<>();
                for (String key : enchantmentKeys) {
                    Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.fromString(key));
                    if (enchantment != null) {
                        enchantments.add(enchantment);
                    }
                }
                return enchantments;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addEnchantment(UUID uuid, List<Enchantment> enchants) {

        List<String> enchantmentKeys = new ArrayList<>();
        for (Enchantment enchantment : enchants) {
            enchantmentKeys.add(enchantment.getKey().getKey()); // 获取 Enchantment 的 key
        }

        String enchantsJson = gson.toJson(enchantmentKeys);

        String databaseType = AdvancedEnchantingTable.getInstance().getDatabaseManager().getDatabaseType();

        String query;
        if (databaseType.equals("mysql")) {
            query = "INSERT INTO player_enchants (uuid, enchants) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE enchants = VALUES(enchants)";
        } else {
            query = "INSERT OR REPLACE INTO player_enchants (uuid, enchants) VALUES (?, ?)";
        }

        try (Connection connection = AdvancedEnchantingTable.getInstance().getDatabaseManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, enchantsJson);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean clearEnchants(UUID uuid) {
        String query = "UPDATE player_enchants SET enchants = '' WHERE uuid = ?";

        try (Connection connection = AdvancedEnchantingTable.getInstance().getDatabaseManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

package com.yixu.Util.Enchant;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Model.Enchant.EnchantCost;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

/**
 * 从配置中读取附魔消耗信息
 */
public class EnchantCostUtil {

    public static EnchantCost getEnchantCost(Enchantment enchant) {

        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();
        FileConfiguration enchantConfig = advancedEnchantingTableAPI.getConfigManager().getConfig("enchant.yml");

        String path = "Enchants." + enchant.getKey().getKey();

        EnchantCost enchantCost = new EnchantCost();
        enchantCost.money = enchantConfig.getDouble(path + ".Money");
        enchantCost.vanillaLevel = enchantConfig.getInt(path + ".Vanilla_Level");
        enchantCost.adventureLevel = enchantConfig.getInt(path + ".Adventure_Level");
        enchantCost.items = enchantConfig.getMapList(path + ".Items");

        return enchantCost;

    }
}

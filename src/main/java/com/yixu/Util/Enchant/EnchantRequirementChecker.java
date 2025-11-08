package com.yixu.Util.Enchant;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Model.Enchant.EnchantCost;
import com.yixu.Util.Item.ItemRequirementChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import top.cpjinan.akarilevel.level.LevelGroup;

/**
 * 校验附魔所需条件，并可统一扣除
 */
public class EnchantRequirementChecker {

    private static final AdvancedEnchantingTable INSTANCE = AdvancedEnchantingTable.getInstance();

    public static boolean hasEnough(Player player, EnchantCost enchantCost) {
        Economy economyAPI = INSTANCE.getEconomyAPI();

        if (!economyAPI.has(player, enchantCost.money)) {
            player.sendMessage("金币不足！");
            return false;
        }

        if (player.getLevel() < enchantCost.vanillaLevel) {
            player.sendMessage("原版等级不足！");
            return false;
        }

        LevelGroup adventure = LevelGroup.getLevelGroups().get("Adventure_Level");
        if (adventure == null || adventure.getMemberLevel(player.getName()) < enchantCost.adventureLevel) {
            player.sendMessage("冒险等级不足！");
            return false;
        }

        if (!ItemRequirementChecker.hasRequiredItems(player, enchantCost.items)) {
            player.sendMessage("所需材料不足！");
            return false;
        }

        return true;
    }

    public static void consumeRequire(Player player, EnchantCost cost) {
        Economy eco = INSTANCE.getEconomyAPI();
        eco.withdrawPlayer(player, cost.money);

        player.setLevel(player.getLevel() - cost.vanillaLevel);
        LevelGroup adventure = LevelGroup.getLevelGroups().get("Adventure_Level");

        if (adventure != null) {
            adventure.removeMemberLevel(player.getName(), cost.adventureLevel, "COMMAND_REMOVE_EXP");
        }

        ItemRequirementChecker.removeRequiredItems(player, cost.items);
    }
}

package com.yixu.Util.Item;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;

public class ItemRequirementChecker {

    /**
     * 检查玩家是否拥有配置文件中要求的所有物品
     *
     * @param player 玩家
     * @param requireItems 配置文件中 "Items" 列表
     * @return 是否拥有足够的物品
     */
    public static boolean hasRequiredItems(Player player, List<Map<?, ?>> requireItems) {
        for (Map<?, ?> itemData : requireItems) {
            String id = String.valueOf(itemData.get("ID"));
            int amount = (int) itemData.get("Amount");

            ItemStack required = ItemStackResolver.getItemStackByItemMaterial(id);
            if (required == null) {
                player.sendMessage("§c物品解析错误: §f" + id);
                return false;
            }

            if (!hasItem(player, required, amount)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从玩家背包中移除配置文件中要求的物品
     *
     * @param player 玩家
     * @param requireItems 配置文件中 "Items" 列表
     */
    public static void removeRequiredItems(Player player, List<Map<?, ?>> requireItems) {
        for (Map<?, ?> itemData : requireItems) {
            String id = String.valueOf(itemData.get("ID"));
            int amount = (int) itemData.get("Amount");

            ItemStack required = ItemStackResolver.getItemStackByItemMaterial(id);
            if (required == null) {
                continue;
            }

            removeItem(player, required, amount);
        }
    }

    // =====================================
    // 以下为通用的基础方法，可在其他地方复用
    // =====================================

    public static boolean hasItem(Player player, ItemStack target, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(target)) {
                count += item.getAmount();
                if (count >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeItem(Player player, ItemStack target, int amount) {
        PlayerInventory inv = player.getInventory();
        int remaining = amount;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) {
                continue;
            }

            if (item.isSimilar(target)) {
                int stackAmount = item.getAmount();

                if (stackAmount <= remaining) {
                    inv.clear(i);
                    remaining -= stackAmount;
                } else {
                    item.setAmount(stackAmount - remaining);
                    inv.setItem(i, item);
                    return;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }
    }
}

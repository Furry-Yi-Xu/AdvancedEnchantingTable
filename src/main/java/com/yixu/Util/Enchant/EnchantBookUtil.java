package com.yixu.Util.Enchant;

import com.yixu.Util.Database.Enchant.EnchantDatabaseOperation;
import com.yixu.Util.Thread.ServerThreadUtil;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;
import java.util.stream.Collectors;

public class EnchantBookUtil {

    private static final Random RANDOM = new Random();

    public static List<Enchantment> getRandomEnchantByItemStack(ItemStack itemStack, Player player, List<Integer> bookSlots) {
        // 参数检查，避免 NPE
        if (itemStack == null || player == null || bookSlots == null || bookSlots.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集可用附魔（仅自定义附魔 + 可应用于该物品）
        List<Enchantment> availableEnchants = Bukkit.getRegistry(Enchantment.class).stream()
                .filter(enchant ->
                        !StoredVanillaEnchantKeys.isVanilla(String.valueOf(enchant.key())) &&
                                enchant.canEnchantItem(itemStack))
                .collect(Collectors.toList());

        if (availableEnchants.isEmpty()) {
            return Collections.emptyList();
        }

        // 随机抽取附魔
        List<Enchantment> selectedEnchants = new ArrayList<>(bookSlots.size());
        for (int i = 0; i < bookSlots.size(); i++) {
            Enchantment randomEnchant = availableEnchants.get(RANDOM.nextInt(availableEnchants.size()));
            selectedEnchants.add(randomEnchant);
        }

        EnchantDatabaseOperation.addEnchantment(player.getUniqueId(), selectedEnchants);

        return selectedEnchants;
    }

    public static Map.Entry<Enchantment, Integer> resolveEnchantBook(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            Map<Enchantment, Integer> stored = meta.getStoredEnchants();
            if (!stored.isEmpty()) {
                return stored.entrySet().iterator().next();
            }
        }
        return null;
    }

}

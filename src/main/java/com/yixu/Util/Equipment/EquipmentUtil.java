package com.yixu.Util.Equipment;

import com.yixu.AdvancedEnchantingTable;
import mc.obliviate.inventory.Gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EquipmentUtil {

    public static boolean checkEquipmentSlot(Gui gui) {

        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

        FileConfiguration guiConfig = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml");

        int equipmentSlot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        ItemStack equipmentItemStack = gui.getInventory().getItem(equipmentSlot);

        if (equipmentItemStack == null) {
            return false;
        }

        return true;
    }

    public static boolean checkItemType(Gui gui) {

        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

        FileConfiguration guiConfig = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml");

        int equipmentSlot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        ItemStack equipmentItemStack = gui.getInventory().getItem(equipmentSlot);

        String materialName = equipmentItemStack.getType().name();

        // 检查盔甲
        if (materialName.endsWith("_HELMET") || materialName.endsWith("_CHESTPLATE") ||
                materialName.endsWith("_LEGGINGS") || materialName.endsWith("_BOOTS")) {
            return true;
        }

        // 检查工具和武器
        if (materialName.endsWith("_SWORD") || materialName.endsWith("_AXE") ||
                materialName.endsWith("_PICKAXE") || materialName.endsWith("_SHOVEL") ||
                materialName.endsWith("_HOE") || materialName.endsWith("_BOW") || materialName.endsWith("_CROSSBOW")) {
            return true;
        }

        // 可以根据需要继续扩展，比如弩、钓鱼竿等可附魔物品
        if (materialName.endsWith("_TRIDENT") || materialName.endsWith("_ELYTRA") || materialName.endsWith("_SHIELD")) {
            return true;
        }

        return false;
    }

}

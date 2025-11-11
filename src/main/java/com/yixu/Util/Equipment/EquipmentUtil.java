package com.yixu.Util.Equipment;

import com.yixu.AdvancedEnchantingTable;
import mc.obliviate.inventory.Gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;

import java.util.Set;

public class EquipmentUtil {

    private static final Set<String> ENCHANTABLE_SUFFIXES = Set.of(
            "_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS",
            "_SWORD", "_AXE", "_PICKAXE", "_SHOVEL", "_HOE",
            "_BOW", "BOW", "_CROSSBOW", "_TRIDENT", "_ELYTRA", "_SHIELD"
    );

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

        Material materialType = equipmentItemStack.getType();

        String materialName = materialType.name();
        for (String suffix : ENCHANTABLE_SUFFIXES) {
            if (materialName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}


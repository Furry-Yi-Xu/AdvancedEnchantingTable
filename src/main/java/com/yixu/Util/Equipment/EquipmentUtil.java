package com.yixu.Util.Equipment;

import com.yixu.AdvancedEnchantingTable;
import mc.obliviate.inventory.Gui;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EquipmentUtil {

    public static boolean checkEquipmentSlot(Gui gui, Player player) {

        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

        FileConfiguration guiConfig = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml");

        int equipmentSlot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        ItemStack equipmentItemStack = gui.getInventory().getItem(equipmentSlot);

        if (equipmentItemStack == null) {
            return false;
        }

        return true;
    }

}

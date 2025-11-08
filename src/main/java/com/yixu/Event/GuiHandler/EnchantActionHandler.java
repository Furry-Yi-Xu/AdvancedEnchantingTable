package com.yixu.Event.GuiHandler;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Builder.GuiBuilder;
import com.yixu.Gui.EnchantingTableGui;
import com.yixu.Model.Enchant.EnchantCost;
import com.yixu.Util.Database.Enchant.EnchantDatabaseOperation;
import com.yixu.Util.Enchant.EnchantBookUtil;
import com.yixu.Util.Enchant.EnchantCostUtil;
import com.yixu.Util.Equipment.EquipmentUtil;
import com.yixu.Util.Enchant.EnchantRequirementChecker;
import com.yixu.Util.Message.MessageUtil;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * 附魔逻辑处理类
 */
public class EnchantActionHandler {

    private final AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

    public void handle(Gui gui, Icon icon, Player player, InventoryClickEvent event) {
        if (!EquipmentUtil.checkEquipmentSlot(gui, player)) {
            MessageUtil.sendMessage(player, "Enchant.Please-Put-Equipment-In-Slot");
            return;
        }

        ItemStack equipment = getEquipmentItem(gui);
        Map.Entry<Enchantment, Integer> enchantBook = EnchantBookUtil.resolveEnchantBook(icon.getItem());
        if (enchantBook == null) {
            MessageUtil.sendMessage(player, "Enchant.Invalid-Enchantment-Book");
            return;
        }

        if (event.isLeftClick()) {
            applyEnchant(gui, player, equipment, enchantBook);
        } else if (event.isRightClick()) {
            showEnchantRequirements(gui, enchantBook.getKey());
        }
    }

    private ItemStack getEquipmentItem(Gui gui) {
        int slot = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml").getInt("Icons.EquipmentDisplay.slot");
        return gui.getInventory().getItem(slot);
    }

    private void applyEnchant(Gui gui, Player player, ItemStack item, Map.Entry<Enchantment, Integer> data) {
        Enchantment enchant = data.getKey();
        int level = data.getValue();

        if (!enchant.canEnchantItem(item)) {
            MessageUtil.sendMessage(player, "Enchant.Enchantment-Not-Compatible");
            return;
        }

        EnchantCost cost = EnchantCostUtil.getEnchantCost(enchant);
        if (!EnchantRequirementChecker.hasEnough(player, cost)) {
            return;
        }

        EnchantRequirementChecker.consumeRequire(player, cost);
        item.addEnchantment(enchant, level);

        MessageUtil.sendMessage(player, "Enchant.Enchantment-Success");

        if (gui instanceof EnchantingTableGui enchantingTableGui) {
            GuiBuilder guiBuilder = enchantingTableGui.getGuiBuilder();
            guiBuilder.clearGui();
            EnchantDatabaseOperation.clearEnchants(player.getUniqueId());
        }
    }

    private void showEnchantRequirements(Gui gui, Enchantment enchant) {
        if (gui instanceof EnchantingTableGui enchantingTableGui) {
            GuiBuilder guiBuilder = enchantingTableGui.getGuiBuilder();
            guiBuilder.buildEnchantBookRequireMaterial(enchant);
        }
    }
}

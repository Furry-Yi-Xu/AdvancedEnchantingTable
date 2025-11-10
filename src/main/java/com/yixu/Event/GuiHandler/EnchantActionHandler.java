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
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 附魔逻辑处理类
 */
public class EnchantActionHandler {

    private final AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

    public void handle(Gui gui, Icon icon, Player player, InventoryClickEvent event, AdvancedSlotManager advancedSlotManager) {

        ItemStack equipment = getEquipmentItem(gui);

        Map.Entry<Enchantment, Integer> enchantBook = EnchantBookUtil.resolveEnchantBook(icon.getItem());

        if (enchantBook == null) {
            MessageUtil.sendMessage(player, "Enchant.Invalid-Enchantment-Book");
            return;
        }

        if (event.isLeftClick()) {

            if (!EquipmentUtil.checkEquipmentSlot(gui)) {
                MessageUtil.sendMessage(player, "Enchant.Please-Put-Equipment-In-Slot");
                return;
            }

            applyEnchant(gui, player, equipment, enchantBook, advancedSlotManager);
        }

        if (event.isRightClick()) {
            showEnchantRequirements(gui, enchantBook.getKey());
            return;
        }

    }

    private ItemStack getEquipmentItem(Gui gui) {
        int slot = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml").getInt("Icons.EquipmentDisplay.slot");
        return gui.getInventory().getItem(slot);
    }

    private void applyEnchant(Gui gui, Player player, ItemStack item, Map.Entry<Enchantment, Integer> data, AdvancedSlotManager advancedSlotManager) {
        Enchantment enchant = data.getKey();
        int level = data.getValue();

        if (!enchant.canEnchantItem(item) && item.getType() != Material.BOOK) {
            MessageUtil.sendMessage(player, "Enchant.Enchantment-Not-Compatible");
            return;
        }

        EnchantCost cost = EnchantCostUtil.getEnchantCost(enchant);
        if (!EnchantRequirementChecker.hasEnough(player, cost)) {
            return;
        }

        EnchantRequirementChecker.consumeRequire(player, cost);

        if (item.getType() == Material.BOOK) {
            if (gui instanceof EnchantingTableGui enchantingTableGui) {
                GuiBuilder guiBuilder = enchantingTableGui.getGuiBuilder();
                guiBuilder.buildEquipmentSlotByEnchantBook(enchant, level);
            }
        } else {
            item.addEnchantment(enchant, level);
        }

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

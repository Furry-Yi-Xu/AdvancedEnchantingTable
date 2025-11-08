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
            player.sendMessage("请将装备放置于装备栏！");
            return;
        }

        ItemStack equipment = getEquipmentItem(gui);
        Map.Entry<Enchantment, Integer> enchantBook = EnchantBookUtil.resolveEnchantBook(icon.getItem());
        if (enchantBook == null) {
            player.sendMessage("无效的附魔书！");
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
            player.sendMessage("这个附魔与这个装备类型不符合！");
            return;
        }

        EnchantCost cost = EnchantCostUtil.getEnchantCost(enchant);
        if (!EnchantRequirementChecker.hasEnough(player, cost)) {
            return;
        }

        EnchantRequirementChecker.consumeRequire(player, cost);
        item.addEnchantment(enchant, level);

        player.sendMessage("你已成功附魔！");

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

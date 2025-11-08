package com.yixu.Event.GuiHandler;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Builder.GuiBuilder;
import com.yixu.Gui.EnchantingTableGui;
import com.yixu.Interface.GuiState;
import com.yixu.Util.Equipment.EquipmentUtil;
import com.yixu.Util.Message.MessageUtil;
import mc.obliviate.inventory.Gui;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * 刷新附魔书逻辑处理类
 */
public class RefreshActionHandler {

    private final AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();

    public void handleGuiDisplayRefresh(Gui gui, Player player, InventoryClickEvent event) {
        if (!EquipmentUtil.checkEquipmentSlot(gui, player)) {
            MessageUtil.sendMessage(player, "Enchant.Please-Put-Equipment-In-Slot");
            return;
        }

        if (event.isLeftClick()) {
            handleMoneyRefresh(player);
        } else if (event.isRightClick()) {
            handlePointRefresh(player);
        }

        if (gui instanceof EnchantingTableGui enchantingTableGui) {
            GuiBuilder guiBuilder = enchantingTableGui.getGuiBuilder();
            guiBuilder.buildRefreshButton();
            guiBuilder.clearAboutEnchantDisplay();
            guiBuilder.buildAllEnchantBook(GuiState.REFRESH);
        }

    }

    private void handleMoneyRefresh(Player player) {
        Economy economyAPI = advancedEnchantingTableAPI.getEconomyAPI();
        double cost = advancedEnchantingTableAPI.getConfigManager().getConfig("config.yml").getDouble("RefreshCost.Money");

        if (!economyAPI.has(player, cost)) {
            MessageUtil.sendMessage(player, "Enchant.Insufficient-Coins-For-Refresh");
            return;
        }

        economyAPI.withdrawPlayer(player, cost);
        MessageUtil.sendMessage(player, "Enchant.All-Enchantment-Books-Refreshed");
    }

    private void handlePointRefresh(Player player) {
        PlayerPointsAPI points = advancedEnchantingTableAPI.getPlayerPointsAPI();
        int cost = advancedEnchantingTableAPI.getConfigManager().getConfig("config.yml").getInt("RefreshCost.Points");

        int current = points.look(player.getUniqueId());
        if (current < cost) {
            MessageUtil.sendMessage(player, "Enchant.Insufficient-Points-For-Refresh");
            return;
        }

        points.take(player.getUniqueId(), cost);
        MessageUtil.sendMessage(player, "Enchant.All-Enchantment-Books-Refreshed");
    }
}

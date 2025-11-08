package com.yixu.Event.GuiHandler;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Builder.GuiBuilder;
import com.yixu.Gui.EnchantingTableGui;
import com.yixu.Interface.GuiState;
import com.yixu.Util.Equipment.EquipmentUtil;
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
            player.sendMessage("请将装备放置于装备栏！");
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
            player.sendMessage("金币不足，无法刷新！");
            return;
        }

        economyAPI.withdrawPlayer(player, cost);
        player.sendMessage("已成功刷新所有附魔书！");
    }

    private void handlePointRefresh(Player player) {
        PlayerPointsAPI points = advancedEnchantingTableAPI.getPlayerPointsAPI();
        int cost = advancedEnchantingTableAPI.getConfigManager().getConfig("config.yml").getInt("RefreshCost.Points");

        int current = points.look(player.getUniqueId());
        if (current < cost) {
            player.sendMessage("点券不足，无法刷新！");
            return;
        }

        points.take(player.getUniqueId(), cost);
        player.sendMessage("已成功刷新所有附魔书！");
    }
}

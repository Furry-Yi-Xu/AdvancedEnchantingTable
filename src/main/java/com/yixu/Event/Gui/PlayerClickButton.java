package com.yixu.Event.Gui;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Event.GuiHandler.EnchantActionHandler;
import com.yixu.Event.GuiHandler.RefreshActionHandler;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

/**
 * GUI 按钮点击事件分发入口
 */
public class PlayerClickButton {

    private final EnchantActionHandler enchantHandler = new EnchantActionHandler();
    private final RefreshActionHandler refreshHandler = new RefreshActionHandler();

    public void onPlayerClickButton(Gui gui, Player player, Icon icon, AdvancedSlotManager advancedSlotManager) {
        icon.onClick(event -> handleClick(gui, player, icon, event, advancedSlotManager));
    }

    private void handleClick(Gui gui, Player player, Icon icon, InventoryClickEvent event, AdvancedSlotManager advancedSlotManager) {
        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();
        FileConfiguration guiConfig = advancedEnchantingTableAPI.getConfigManager().getConfig("gui.yml");

        int refreshSlot = guiConfig.getInt("Icons.Refresh.slot");
        List<Integer> enchantSlots = guiConfig.getIntegerList("Icons.EnchantBook.slot");

        int slot = event.getSlot();

        if (slot == refreshSlot) {
            refreshHandler.handleGuiDisplayRefresh(gui, player, event);
        } else if (enchantSlots.contains(slot)) {
            enchantHandler.handle(gui, icon, player, event, advancedSlotManager);
        }
    }
}

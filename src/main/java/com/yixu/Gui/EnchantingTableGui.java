package com.yixu.Gui;

import com.yixu.Builder.GuiBuilder;
import com.yixu.Util.Thread.ServerThreadUtil;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class EnchantingTableGui extends Gui {

    private final AdvancedSlotManager advancedSlotManager = new AdvancedSlotManager(this);
    private GuiBuilder guiBuilder;

    public EnchantingTableGui(Player player, String guiTitle, int guiRows) {
        super(player, "enchanting-table-gui", guiTitle, guiRows);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.guiBuilder = new GuiBuilder(player, this, advancedSlotManager);
        guiBuilder.buildGui();
    }

    public GuiBuilder getGuiBuilder() {
        return guiBuilder;
    }

    public AdvancedSlotManager getAdvancedSlotManager() {
        return advancedSlotManager;
    }
}

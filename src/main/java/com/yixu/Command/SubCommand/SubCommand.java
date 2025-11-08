package com.yixu.Command.SubCommand;


import com.yixu.AdvancedEnchantingTable;
import com.yixu.Builder.GuiBuilder;
import com.yixu.Gui.EnchantingTableGui;
import com.yixu.Manager.Config.ServerFileConfigManager;
import com.yixu.Util.Message.ColorUtil;
import com.yixu.Util.Message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SubCommand {

    public void reloadConfig(Player player) {
        ServerFileConfigManager configManager = AdvancedEnchantingTable.getInstance().getConfigManager();
        configManager.reloadAllConfigs();
        MessageUtil.sendMessage(player, "Command.Succeed-Reload");
    }

    public void openEnchantingTableForPlayer(String playerName) {
        if (playerName.equals("")) {
            return;
        }

        Player player = Bukkit.getPlayer(playerName);

        ServerFileConfigManager configManager = AdvancedEnchantingTable.getInstance().getConfigManager();
        FileConfiguration guiConfig = configManager.getConfig("gui.yml");

        String guiTitle = ColorUtil.colorMessage(guiConfig.getString("Title"));
        int guiRow = guiConfig.getInt("Row");

        new EnchantingTableGui(player, guiTitle, guiRow).open();

    }

}
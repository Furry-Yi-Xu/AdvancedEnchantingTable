package com.yixu.Util.Message;

import com.yixu.AdvancedEnchantingTable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static FileConfiguration MessageConfig() {
        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();
        return advancedEnchantingTableAPI.getConfigManager().getConfig("message.yml");
    }

    public static String formatPrefix(String rawMessage) {
        String rawPrefix = MessageConfig().getString("Prefix");
        return rawMessage.replaceAll("%Prefix%", rawPrefix);
    }

    public static String getMessage(CommandSender sender, String path) {
        String rawMessage = MessageConfig().getString(path);
        String formattedMessage = formatPrefix(rawMessage);
        if (sender instanceof Player player) {
            String placeholdersMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
            return ColorUtil.colorMessage(placeholdersMessage);
        } else {
            return ColorUtil.colorMessage(formattedMessage);
        }
    }

    public static void sendMessage(CommandSender sender, String path) {
        String placeholdersMessage = getMessage(sender, path);
        sender.sendMessage(placeholdersMessage);
    }

}

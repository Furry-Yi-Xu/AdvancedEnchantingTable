package com.yixu.Util.Message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ColorUtil {

    public static String colorMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String colorMessage(String message, Player player) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, message));
    }

    public static List<String> colorList(List<String> list) {
        List<String> coloredLore = list.stream()
                .map(string -> ChatColor.translateAlternateColorCodes('&', string))
                .toList();
        return coloredLore;
    }

    public static List<String> colorList(List<String> list, Player player) {
        return list.stream()
                .map(string -> {
                    String withPlaceholders = PlaceholderAPI.setPlaceholders(player, string);
                    return ChatColor.translateAlternateColorCodes('&', withPlaceholders);
                })
                .toList();
    }

}

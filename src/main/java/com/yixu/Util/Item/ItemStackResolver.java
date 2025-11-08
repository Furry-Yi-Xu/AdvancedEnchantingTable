package com.yixu.Util.Item;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackResolver {

    public static ItemStack getItemStackByItemMaterial(String itemName) {

        itemName = itemName.toLowerCase();

        String[] itemNameSplit = itemName.split(":", 3);
        String itemType = itemNameSplit[0];

        ItemStack itemStack = null;

        switch (itemType) {
            case "craftengine":
                Key itemKey = new Key(itemNameSplit[1], itemNameSplit[2]);
                ItemStack customItemStack = CraftEngineItems.byId(itemKey).buildItemStack();
                boolean isCustomItem = CraftEngineItems.isCustomItem(customItemStack);
                if (!isCustomItem) {
                    Bukkit.getLogger().severe("材质 " + itemName + " 发生解析错误，请查看配置文件！");
                    return itemStack;
                }
                itemStack = customItemStack;
                break;

            case "minecraft":
                Material itemMaterial = Material.matchMaterial(itemNameSplit[1]);
                boolean isItemMaterial = itemMaterial.isItem();
                if (!isItemMaterial) {
                    Bukkit.getLogger().severe("材质 " + itemName + " 发生解析错误，请查看配置文件！");
                    return itemStack;
                }
                itemStack = new ItemStack(itemMaterial);
                break;
        }

        return itemStack;
    }

}

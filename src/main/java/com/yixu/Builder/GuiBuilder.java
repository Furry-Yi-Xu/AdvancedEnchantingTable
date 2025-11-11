package com.yixu.Builder;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Event.Gui.PlayerClickButton;
import com.yixu.Interface.GuiState;
import com.yixu.Manager.Config.ServerFileConfigManager;
import com.yixu.Util.Database.Enchant.EnchantDatabaseOperation;
import com.yixu.Util.Enchant.EnchantBookUtil;
import com.yixu.Util.Item.ItemStackResolver;
import com.yixu.Util.Message.ColorUtil;
import com.yixu.Util.Thread.ServerThreadUtil;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiBuilder {

    private final ServerFileConfigManager configManager;
    private final FileConfiguration guiConfig;
    private final FileConfiguration enchantConfig;
    private final PlayerClickButton clickHandler;

    private final Player player;
    private final Gui gui;
    private final AdvancedSlotManager advancedSlotManager;

    public GuiBuilder(Player player, Gui gui, AdvancedSlotManager advancedSlotManager) {
        this.player = player;
        this.gui = gui;
        this.advancedSlotManager = advancedSlotManager;

        AdvancedEnchantingTable advancedEnchantingTableAPI = AdvancedEnchantingTable.getInstance();
        this.configManager = advancedEnchantingTableAPI.getConfigManager();
        this.guiConfig = configManager.getConfig("gui.yml");
        this.enchantConfig = configManager.getConfig("enchant.yml");
        this.clickHandler = new PlayerClickButton();
    }

    /**
     * 初始化主 GUI
     */
    public void buildGui() {
        buildRefreshButton();
        buildEquipmentSlot();
        buildAllEnchantBook(GuiState.OPENED);
    }

    public void clearGui() {
        clearAboutEnchantDisplay();
        clearEnchantBookDisplay();
        buildRefreshButton();
    }

    /**
     * 构建刷新按钮
     */
    public void buildRefreshButton() {
        int buttonSlot = guiConfig.getInt("Icons.Refresh.slot");

        ItemStack item = ItemStackResolver.getItemStackByItemMaterial(
                guiConfig.getString("Icons.Refresh.display.material")
        );

        Icon icon = new Icon(item)
                .setName(ColorUtil.colorMessage(guiConfig.getString("Icons.Refresh.display.name"), player))
                .setLore(ColorUtil.colorList(guiConfig.getStringList("Icons.Refresh.display.lore"), player));

        gui.addItem(buttonSlot, icon);
        clickHandler.onPlayerClickButton(gui, player, icon, advancedSlotManager);

    }

    /**
     * 构建所有附魔书
     */
    public void buildAllEnchantBook(GuiState guiState) {

        UUID uuid = player.getUniqueId();

        List<Integer> bookSlots = guiConfig.getIntegerList("Icons.EnchantBook.slot");
        int equipmentSlot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        ItemStack equipment = gui.getInventory().getItem(equipmentSlot);
        ItemStack baseItem = ItemStackResolver.getItemStackByItemMaterial(guiConfig.getString("Icons.EnchantBook.display.material"));

        String displayName = ColorUtil.colorMessage(guiConfig.getString("Icons.EnchantBook.display.name"), player);
        List<String> baseLore = guiConfig.getStringList("Icons.EnchantBook.display.lore");

        // 异步读取数据库
        ServerThreadUtil.runAsyncTask(() -> {
            List<Enchantment> enchantList;

            if (guiState.equals(GuiState.OPENED)) {
                enchantList = EnchantDatabaseOperation.getEnchants(uuid);
            } else if (guiState.equals(GuiState.REFRESH)) {
                enchantList = EnchantBookUtil.getRandomEnchantByItemStack(equipment, player, bookSlots);
            } else {
                enchantList = null;
            }

            // 异步读取失败或返回空数据，直接中止
            if (enchantList == null || enchantList.isEmpty()) {
                return;
            }

            // 回到主线程更新 GUI
            ServerThreadUtil.runSyncTask(() -> {

                for (int i = 0; i < bookSlots.size(); i++) {
                    Enchantment randomEnchant = enchantList.get(i);
                    Integer enchantBookSlot = bookSlots.get(i);

                    if (randomEnchant == null) {
                        continue;
                    }

                    String enchantKey = randomEnchant.getKey().getKey();
                    double money = enchantConfig.getDouble("Enchants." + enchantKey + ".Money");
                    int vanillaLevel = enchantConfig.getInt("Enchants." + enchantKey + ".Vanilla_Level");
                    int adventureLevel = enchantConfig.getInt("Enchants." + enchantKey + ".Adventure_Level");

                    List<String> finalLore = new ArrayList<>();
                    for (String line : baseLore) {
                        line = line
                                .replace("%consume_money%", String.valueOf(money))
                                .replace("%consume_vanilla_level%", String.valueOf(vanillaLevel))
                                .replace("%consume_adventure_level%", String.valueOf(adventureLevel));
                        finalLore.add(ColorUtil.colorMessage(line, player));
                    }

                    Icon icon = new Icon(baseItem.clone())
                            .setName(displayName)
                            .setLore(finalLore)
                            .enchant(randomEnchant, 1);

                    gui.addItem(enchantBookSlot, icon);
                    clickHandler.onPlayerClickButton(gui, player, icon, advancedSlotManager);
                }

            });
        });
    }


    /**
     * 构建装备栏
     */
    public void buildEquipmentSlot() {
        int slot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        ItemStack item = ItemStackResolver.getItemStackByItemMaterial(
                guiConfig.getString("Icons.EquipmentDisplay.display.material")
        );

        Icon icon = new Icon(item)
                .setName(ColorUtil.colorMessage(guiConfig.getString("Icons.EquipmentDisplay.display.name"), player))
                .setLore(ColorUtil.colorList(guiConfig.getStringList("Icons.EquipmentDisplay.display.lore"), player));

        advancedSlotManager.addAdvancedIcon(slot, icon);
    }

    /**
     * 如果附魔的物品是书，清除书，并且重新构建附魔书
     */
    public void buildEquipmentSlotByEnchantBook(Enchantment enchant, int level) {

        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
        meta.addStoredEnchant(enchant, level, true);
        enchantedBook.setItemMeta(meta);

        int slot = guiConfig.getInt("Icons.EquipmentDisplay.slot");

        Icon icon = new Icon(Material.AIR);
        AdvancedSlot advancedSlot = advancedSlotManager.addAdvancedIcon(slot, icon);
        advancedSlotManager.putIconToAdvancedSlot(advancedSlot, enchantedBook);

    }

    /**
     * 构建附魔书所需材料显示
     */
    public void buildEnchantBookRequireMaterial(Enchantment enchant) {
        List<Integer> materialSlots = guiConfig.getIntegerList("Icons.MaterialDisplay.slot");
        String enchantName = enchant.getKey().getKey();
        List<Map<?, ?>> materials = enchantConfig.getMapList("Enchants." + enchantName + ".Items");

        for (int i = 0; i < Math.min(materialSlots.size(), materials.size()); i++) {
            Map<?, ?> itemData = materials.get(i);
            String id = (String) itemData.get("ID");
            int amount = (int) itemData.get("Amount");

            ItemStack item = ItemStackResolver.getItemStackByItemMaterial(id);
            Icon icon = new Icon(item).setAmount(amount);
            gui.addItem(materialSlots.get(i), icon);
        }
    }

    /**
     * 清除附魔材料显示
     */
    public void clearAboutEnchantDisplay() {
        List<Integer> materialSlots = guiConfig.getIntegerList("Icons.MaterialDisplay.slot");
        List<Integer> enchantBookSlots = guiConfig.getIntegerList("Icons.EnchantBook.slot");

        for (Integer slot : materialSlots) {
            gui.addItem(slot, new Icon(new ItemStack(Material.AIR)));
        }

        for (Integer slot : enchantBookSlots) {
            gui.addItem(slot, new Icon(new ItemStack(Material.AIR)));
        }

    }

    public void clearEnchantBookDisplay() {
        List<Integer> enchantBookSlots = guiConfig.getIntegerList("Icons.EnchantBook.slot");

        for (Integer slot : enchantBookSlots) {
            gui.addItem(slot, new Icon(new ItemStack(Material.AIR)));
        }

    }

}

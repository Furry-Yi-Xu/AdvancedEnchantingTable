package com.yixu;

import com.yixu.Command.CommandTabCompleter;
import com.yixu.Manager.Command.CommandManager;
import com.yixu.Manager.Config.ServerFileConfigManager;
import com.yixu.Manager.Database.DatabaseManager;
import com.yixu.Manager.Event.EventManager;
import com.yixu.Util.Thread.ServerThreadUtil;
import mc.obliviate.inventory.InventoryAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class AdvancedEnchantingTable extends JavaPlugin {

    private static AdvancedEnchantingTable instance;

    private EventManager eventManager;
    private ServerFileConfigManager serverFileConfigManager;

    private DatabaseManager databaseManager;

    private Economy economyAPI;
    private PlayerPointsAPI playerPointsAPI;

    @Override
    public void onEnable() {
        instance = this;

        this.eventManager = new EventManager(this);
        this.serverFileConfigManager = new ServerFileConfigManager(this);

        try {
            this.databaseManager = new DatabaseManager(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ServerThreadUtil.init(this);

        this.economyAPI = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        this.playerPointsAPI = PlayerPoints.getInstance().getAPI();

        new InventoryAPI(this).init();

        getCommand("AdvancedEnchantingTable").setExecutor(new CommandManager());
        getCommand("AdvancedEnchantingTable").setTabCompleter(new CommandTabCompleter());

        getLogger().info(this.getName() + " 插件已成功加载！");
    }

    @Override
    public void onDisable() {
        databaseManager.onClosed();
        getLogger().info(this.getName() + " 插件已成功卸载！");
    }

    public static AdvancedEnchantingTable getInstance() {
        return instance;
    }

    public ServerFileConfigManager getConfigManager() {
        return serverFileConfigManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public Economy getEconomyAPI() {
        return economyAPI;
    }

    public PlayerPointsAPI getPlayerPointsAPI() {
        return playerPointsAPI;
    }
}

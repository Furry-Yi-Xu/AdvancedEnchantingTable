package com.yixu.Util.Thread;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * 线程调度工具类
 * 负责安全地在同步/异步线程中执行任务
 */
public final class ServerThreadUtil {

    private static Plugin plugin;

    public ServerThreadUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 初始化插件实例（在 onEnable 调用一次） */
    public static void init(Plugin plugin) {
        ServerThreadUtil.plugin = plugin;
    }

    /** 异步任务 */
    public static void runAsyncTask(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    /** 同步任务（主线程） */
    public static void runSyncTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}

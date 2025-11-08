package com.yixu.Manager.Command;

import com.yixu.AdvancedEnchantingTable;
import com.yixu.Command.SubCommand.SubCommand;
import com.yixu.Util.Message.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String subCommand = args[0].toLowerCase();

        if (sender instanceof Player player) {
            String pluginName = AdvancedEnchantingTable.class.getName().toLowerCase();
            if (!player.hasPermission(pluginName + "." + subCommand)) {
                MessageUtil.sendMessage(sender, "Command.No-Permission-Command");
                return false;
            }
        }

        if (args.length == 0) {
            MessageUtil.sendMessage(sender, "Command.Usage-Command");
            return false;
        }

        switch (subCommand) {
            case "reload":
                new SubCommand().reloadConfig(sender);
                break;

            case "open":
                new SubCommand().openEnchantingTableForPlayer(args[1]);
                break;

            default:
                MessageUtil.sendMessage(sender, "Command.Usage-Command");
                return false;
        }

        return false;
    }
}

package com.github.awittyusername.rankshortcut;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.awittyusername.rankshortcut.Main.plugin;

public class CommandRSPlaytime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if (commandSender.hasPermission("rankshortcut.playtime")) {
                commandSender.sendMessage(ChatColor.AQUA + "You have played for " + ChatColor.YELLOW + plugin.hourMinuteFormatter(((Player) commandSender).getStatistic(Statistic.PLAY_ONE_MINUTE) / 1200) + ChatColor.AQUA + ".");
            }
        }
        else{
        commandSender.sendMessage("This command is only supported for players!");
        }
        return true;
    }
}

package com.github.awittyusername.rankshortcut;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import static com.github.awittyusername.rankshortcut.Main.plugin;

public class CommandRSCheck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if (commandSender.hasPermission("rankshortcut.check")) {
                int playtime = plugin.getEffectivePlayTime(((Player) commandSender).getUniqueId());
                @Nullable Rank next = plugin.getNextRank(playtime);
                if (next != null) {
                    commandSender.sendMessage(ChatColor.AQUA + "The next rank available for you is: " + ChatColor.YELLOW + next.getRankName() + ".");
                    commandSender.sendMessage(ChatColor.AQUA + "You have to play for another " + ChatColor.YELLOW + plugin.hourMinuteFormatter((next.getRankTime() - playtime) / 60) + ChatColor.AQUA +
                            " to reach it.");
                    if (plugin.getConfig().getBoolean("costEnabled")) {
                        commandSender.sendMessage(ChatColor.AQUA + "you can also pay" + ChatColor.YELLOW + " $" + plugin.calculateEffectiveCost(next.getRankCost(), (float) playtime / next.getRankTime()) + ChatColor.AQUA + " to rank up now, use /rsbuy!");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.AQUA + "You have already reached the highest rank, congratulations!");

                }
            }
        }
        else{
            commandSender.sendMessage("This command is only supported for players!");
        }
        return true;
    }
}

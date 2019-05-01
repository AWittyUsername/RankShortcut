package com.github.awittyusername.rankshortcut;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
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
        if(commandSender.hasPermission("rankshortcut.playtime.other")) {
            if (strings.length > 0) {
                if (Bukkit.getPlayer(strings[0])!= null){
                    commandSender.sendMessage(ChatColor.YELLOW + Bukkit.getPlayer(strings[0]).getName() + ChatColor.AQUA + " has played for " + ChatColor.YELLOW + plugin.hourMinuteFormatter((Bukkit.getPlayer(strings[0])).getStatistic(Statistic.PLAY_ONE_MINUTE) / 1200) + ChatColor.AQUA + ".");
                } else {
                    commandSender.sendMessage(ChatColor.AQUA + "Player "+ChatColor.YELLOW + strings[0] + ChatColor.AQUA + " was not found!");
                }
                return true;
            }
        }
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

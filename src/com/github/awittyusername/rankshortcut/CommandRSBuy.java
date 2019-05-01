package com.github.awittyusername.rankshortcut;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static com.github.awittyusername.rankshortcut.Main.plugin;

public class CommandRSBuy implements CommandExecutor {
    HashMap<String,Long> commandUsers = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (Main.plugin.getConfig().getBoolean("costEnabled")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (commandSender.hasPermission("rankshortcut.buy")) {
                if (strings.length > 0) {
                    if (strings[0].toLowerCase().equals("confirm")) {
                        if (commandUsers.get(commandSender.getName()) != null) {
                            //Confirmation succesful
                            int playtime = Main.plugin.getEffectivePlayTime(((Player) commandSender).getUniqueId());
                            Rank nextrank = Main.plugin.getNextRank(playtime);
                            if (plugin.deductMoney(plugin.calculateEffectiveCost(nextrank.getRankCost(), (float) playtime / nextrank.getRankTime()), (Player) commandSender)) {
                                plugin.rankup((Player) commandSender, (plugin.playerData.getInt(((Player) commandSender).getUniqueId().toString() + ".rank")));
                                int currBonusTime = plugin.playerData.getInt(((Player) commandSender).getUniqueId().toString() + ".bonusplaytime");
                                int nextRankTime = plugin.getConfig().getInt("ranks." + plugin.playerData.getInt(((Player) commandSender).getUniqueId().toString() + ".rank") + ".time");
                                int previousRankTime = plugin.getConfig().getInt("ranks." + (plugin.playerData.getInt(((Player) commandSender).getUniqueId().toString() + ".rank") - 1) + ".time");
                                int addedBonusTime = nextRankTime - previousRankTime;
                                Bukkit.getConsoleSender().sendMessage("ranks." + plugin.playerData.getInt(((Player) commandSender).getUniqueId().toString() + ".rank") + ".time");
                                plugin.playerData.set(((Player) commandSender).getUniqueId().toString() + ".bonusplaytime", currBonusTime + addedBonusTime);
                                plugin.savePlayerDataFile(plugin.playerData, plugin.playerDataFile);
                            } else {
                                commandSender.sendMessage(ChatColor.AQUA + "You don't have enough money to cover the transaction!");
                            }
                            commandSender.sendMessage("Command was confirmed!");
                            commandUsers.remove(commandSender.getName());
                        }
                    } else {
                        //Invalid arguments
                        commandSender.sendMessage(ChatColor.RED + "Invalid command usage!");
                    }

                }
                //initiate command
                else {
                    commandUsers.remove(commandSender.getName());
                    int playtime = Main.plugin.getEffectivePlayTime(((Player) commandSender).getUniqueId());
                    Rank nextrank = Main.plugin.getNextRank(playtime);
                    if (nextrank != null) {
                        commandUsers.put(commandSender.getName(), System.currentTimeMillis());
                        commandSender.sendMessage(ChatColor.AQUA + "Buying a shortcut to " + ChatColor.YELLOW + nextrank.getRankName() + ChatColor.AQUA + " will cost you " + ChatColor.YELLOW + "$" + plugin.calculateEffectiveCost(nextrank.getRankCost(), (float) playtime / nextrank.getRankTime()) + ChatColor.AQUA + ".");
                        commandSender.sendMessage(ChatColor.AQUA + "Are you sure you want to do this? Confirm with " + ChatColor.YELLOW + "/rsbuy confirm" + ChatColor.AQUA + ".");
                    } else {
                        commandSender.sendMessage(ChatColor.AQUA + "You have already reached the highest rank, congratulations!");
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "The buy module is currently disabled!");
            }
        }
        else{
            commandSender.sendMessage("You do not have permission to use /rsbuy!");
        }
        return true;
    }

}

package com.github.awittyusername.rankshortcut;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRSReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender.hasPermission("rankshortcut.reload")) {
            Main.plugin.reloadConfig();
            Bukkit.getConsoleSender().sendMessage("RankShortcut config is being reloaded!");
            Main.plugin.populateRanks();
            sender.sendMessage("Config reloaded succesfully!");
        }
        return true;
    }
}

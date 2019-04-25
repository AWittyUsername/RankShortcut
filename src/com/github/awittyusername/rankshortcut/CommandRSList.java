package com.github.awittyusername.rankshortcut;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.github.awittyusername.rankshortcut.Main.ranks;

public class CommandRSList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof BlockCommandSender){
            return false;
        }
        if(sender.hasPermission("rankshortcut.list")) {
            for (int i = 0; i < ranks.length; i++) {
                sender.sendMessage(ranks[i].toString());
            }
        }
        return true;
    }
}

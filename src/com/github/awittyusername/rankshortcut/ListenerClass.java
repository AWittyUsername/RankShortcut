package com.github.awittyusername.rankshortcut;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerClass implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent){
        if(!joinEvent.getPlayer().hasPlayedBefore()||!(Main.plugin.playerData.isSet(joinEvent.getPlayer().getUniqueId().toString()))){
            Bukkit.getConsoleSender().sendMessage("Registered player with UUID " + joinEvent.getPlayer().getUniqueId() +" to data.yml.");
            Main.plugin.registerNewPlayer(joinEvent.getPlayer().getUniqueId().toString());
        }
    }
}

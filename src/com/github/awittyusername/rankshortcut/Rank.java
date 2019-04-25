package com.github.awittyusername.rankshortcut;

import org.bukkit.ChatColor;

public class Rank {
    private String name;
    private int playtime;
    private int cost;

    public Rank(String name,int playtime,int cost){
        this.name = name;
        this.playtime = playtime;
        this.cost = cost;
    }
    public String getRankName(){
        return name;
    }
    public int getRankTime(){
        return playtime;
    }
    public int getRankCost(){
        return cost;
    }
    @Override
    public String toString(){
        return ChatColor.DARK_AQUA + "Rank name: " + this.name +", playtime required: " + playtime + ", base cost: " + cost;
    }
}

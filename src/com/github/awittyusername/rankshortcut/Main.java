package com.github.awittyusername.rankshortcut;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
    public static Main plugin;
    public static Rank[] ranks;
    LuckPermsApi lpApi;
    File playerDataFile = new File(getDataFolder(),"data.yml");
    FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    Economy eco;
    @Override
    public void onEnable(){
        if(!enableEconomy()){
            Bukkit.getConsoleSender().sendMessage("RankShortcut was disabled because the Vault dependency is missing!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        lpApi = LuckPerms.getApi();
        getServer().getPluginManager().registerEvents(new ListenerClass(),this);
        saveDefaultConfig();
        savePlayerDataFile(playerData,playerDataFile);
        populateRanks();
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> pollEligibility(),0L,100L);

        registerCommands();
        Bukkit.getConsoleSender().sendMessage("***\nRankShortcut loaded succesfully!\n***");
    }
    @Override
    public void onDisable(){
    }

    public void registerCommands(){
        this.getCommand("rslist").setExecutor(new CommandRSList());
        this.getCommand("rsreload").setExecutor(new CommandRSReload());
        this.getCommand("rscheck").setExecutor(new CommandRSCheck());
        this.getCommand("rsplaytime").setExecutor(new CommandRSPlaytime());
        this.getCommand("rsbuy").setExecutor(new CommandRSBuy());
    }
    public void populateRanks(){
        FileConfiguration config = this.getConfig();
        Set<String> rankNames = config.getConfigurationSection("ranks").getKeys(false);
        this.ranks = new Rank[rankNames.size()];
        for(String s:rankNames){
            ranks[Integer.parseInt(s)] = new Rank(config.getString("ranks."+s+".name"),config.getInt("ranks."+s+".time"),config.getInt("ranks."+s+".cost"));
        }
        Arrays.sort(ranks, Comparator.comparing(Rank::getRankTime));
    }
    public Rank getNextRank(int timePlayed){
        int i = 0;
        if(timePlayed > ranks[ranks.length-1].getRankTime()){
            return null;
        }
        while(timePlayed > ranks[i].getRankTime()){
            i++;
        }
        return ranks[i];
    }
    public String hourMinuteFormatter(int minutes){
        int hours = minutes/ 60;
        minutes %= 60;
        return (hours + " hour(s) and "+minutes+" minutes");
    }
    public int calculateEffectiveCost(int cost,float fraction){
        return (int)(cost-(cost*fraction));
    }
    public void pollEligibility(){
        for(Player player:Bukkit.getOnlinePlayers()){
            Bukkit.getConsoleSender().sendMessage("Found player: "+ player.getName());
            if(player.hasPermission("rankshortcut.exempt")){
                continue;
            }
            String UUID = player.getUniqueId().toString();
            int currentRank = playerData.getInt(UUID+".rank");
            if(currentRank < ranks.length-1) {
                if ((getEffectivePlayTime(player.getUniqueId())> ranks[currentRank+1].getRankTime())){
                    rankup(player,currentRank);
                }
            }
        }
        savePlayerDataFile(playerData,playerDataFile);
    }
    public void savePlayerDataFile(FileConfiguration playerData,File playerDataFile){
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void registerNewPlayer(String UUID){
        playerData.set(UUID+".rank",0);
        playerData.set(UUID+".bonusplaytime",0);
        savePlayerDataFile(playerData,playerDataFile);
    }
    public void rankup(Player player,int currentRank){
        playerData.set(player.getUniqueId().toString()+".rank",currentRank+1);
        player.sendMessage(ChatColor.AQUA+"Congratulations! You have been promoted to "+ChatColor.YELLOW+ranks[currentRank+1].getRankName()+ChatColor.AQUA+"!");
        Bukkit.getConsoleSender().sendMessage("Ranked up "+player.getUniqueId().toString());
        Bukkit.getConsoleSender().sendMessage(player.getDisplayName());
        Bukkit.getConsoleSender().sendMessage("lp user "+player.getDisplayName()+ " group set "+ ranks[currentRank+1].getRankName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+player.getName()+ " group set "+ ranks[currentRank+1].getRankName());
    }
    public boolean enableEconomy(){
        if(Bukkit.getPluginManager().getPlugin("Vault")==null){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }
    public boolean deductMoney(int amount, Player target){
        EconomyResponse e = eco.withdrawPlayer(target,amount);
        return e.transactionSuccess();
    }
    public int getEffectivePlayTime(UUID uuid){
        int effectivePlayTime = (playerData.getInt(uuid.toString() + ".bonusplaytime") + ((Bukkit.getPlayer(uuid).getStatistic(Statistic.PLAY_ONE_MINUTE)/20)));
        return effectivePlayTime;
    }
}

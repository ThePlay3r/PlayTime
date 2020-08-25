package me.pljr.playtime.utils;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.objects.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class TimeUtil {

    public static void startAutoUpdate(Plugin plugin, int seconds){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, ()->{
            PlayTime.getQueryManager().updateDates();
            for (Player player : Bukkit.getOnlinePlayers()){
                TimeUtil.update(player.getUniqueId());
            }
        }, 0, 20*seconds);
    }

    public static void update(UUID uuid){
        CorePlayer corePlayer = PlayTime.getPlayerManager().getCorePlayer(uuid);
        long daily = corePlayer.getDaily();
        long weekly = corePlayer.getWeekly();
        long monthly = corePlayer.getMontly();
        long all = corePlayer.getAll();

        long currentTime = System.currentTimeMillis();
        long playTime = currentTime - corePlayer.getInitialized();
        long afkTime = corePlayer.getAfkTime();
        long addTime = playTime - afkTime;
        if (addTime < 0) addTime = 0;

        corePlayer.setDaily(daily+addTime);
        corePlayer.setWeekly(weekly+addTime);
        corePlayer.setMontly(monthly+addTime);
        corePlayer.setAll(all+addTime);
        PlayTime.getPlayerManager().setCorePlayer(uuid, corePlayer);
    }
}

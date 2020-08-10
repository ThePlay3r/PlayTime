package me.pljr.playtime.utils;

import me.pljr.playtime.managers.PlayerManager;
import me.pljr.playtime.objects.CorePlayer;

import java.util.UUID;

public class TimeUtil {

    public static void update(UUID uuid){
        CorePlayer corePlayer = PlayerManager.getCorePlayer(uuid);
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
    }
}

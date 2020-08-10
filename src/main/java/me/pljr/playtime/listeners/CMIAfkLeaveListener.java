package me.pljr.playtime.listeners;

import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import me.pljr.playtime.managers.PlayerManager;
import me.pljr.playtime.objects.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class CMIAfkLeaveListener implements Listener {

    @EventHandler
    public void onAfkLeave(CMIAfkLeaveEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        CorePlayer corePlayer = PlayerManager.getCorePlayer(uuid);
        corePlayer.setAfk(false);
        long currentAfk = System.currentTimeMillis() - corePlayer.getAfkStart();
        long oldAfk = corePlayer.getAfkTime();
        corePlayer.setAfkTime(currentAfk + oldAfk);
        PlayerManager.setCorePlayer(uuid, corePlayer);
    }
}

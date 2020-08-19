package me.pljr.playtime.listeners;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import me.pljr.playtime.PlayTime;
import me.pljr.playtime.objects.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class CMIAfkEnterListener implements Listener {

    @EventHandler
    public void onAfkEnter(CMIAfkEnterEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        CorePlayer corePlayer = PlayTime.getPlayerManager().getCorePlayer(uuid);
        corePlayer.setAfk(true);
        corePlayer.setAfkStart(System.currentTimeMillis());
        PlayTime.getPlayerManager().setCorePlayer(uuid, corePlayer);
    }
}

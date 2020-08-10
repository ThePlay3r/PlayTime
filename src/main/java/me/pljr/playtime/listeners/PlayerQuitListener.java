package me.pljr.playtime.listeners;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.managers.QueryManager;
import me.pljr.playtime.utils.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final QueryManager query = PlayTime.getQueryManager();

    @EventHandler
    public void onJoin(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        TimeUtil.update(uuid);
        query.savePlayer(uuid);
    }
}

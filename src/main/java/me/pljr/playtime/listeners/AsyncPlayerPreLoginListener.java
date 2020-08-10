package me.pljr.playtime.listeners;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.managers.QueryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {
    private final QueryManager query = PlayTime.getQueryManager();

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event){
        query.loadPlayerSync(event.getUniqueId());
    }
}

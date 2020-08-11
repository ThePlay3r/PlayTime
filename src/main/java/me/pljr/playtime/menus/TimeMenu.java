package me.pljr.playtime.menus;

import me.pljr.playtime.config.CfgTimeMenu;
import me.pljr.playtime.managers.PlayerManager;
import me.pljr.playtime.objects.CorePlayer;
import me.pljr.playtime.utils.TimeUtil;
import me.pljr.pljrapi.managers.GuiManager;
import me.pljr.pljrapi.utils.FormatUtil;
import me.pljr.pljrapi.utils.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class TimeMenu implements Listener {

    public static void open(Player player, OfflinePlayer requested){
        String requestedName = requested.getName();
        UUID uuid = requested.getUniqueId();
        TimeUtil.update(uuid);

        CorePlayer corePlayer = PlayerManager.getCorePlayer(uuid);
        Inventory inventory = Bukkit.createInventory(player, 3*9, CfgTimeMenu.title);

        for (int i = 0; i<27; i++){
            inventory.setItem(i, CfgTimeMenu.background);
        }

        inventory.setItem(4, ItemStackUtil.replaceLore(GuiManager.createHead(requestedName, CfgTimeMenu.headName.replace("%player", requestedName)), "%player", requestedName));
        inventory.setItem(11, ItemStackUtil.replaceLore(CfgTimeMenu.yesterday, "%time", FormatUtil.formatTime(corePlayer.getYesterday() / 1000)));
        inventory.setItem(12, ItemStackUtil.replaceLore(CfgTimeMenu.daily, "%time", FormatUtil.formatTime(corePlayer.getDaily() / 1000)));
        inventory.setItem(13, ItemStackUtil.replaceLore(CfgTimeMenu.all, "%time", FormatUtil.formatTime(corePlayer.getAll() / 1000)));
        inventory.setItem(14, ItemStackUtil.replaceLore(CfgTimeMenu.weekly, "%time", FormatUtil.formatTime(corePlayer.getWeekly() / 1000)));
        inventory.setItem(15, ItemStackUtil.replaceLore(CfgTimeMenu.monthly, "%time", FormatUtil.formatTime(corePlayer.getMontly() / 1000)));

        player.openInventory(inventory);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if (event.getView().getTitle().equals(CfgTimeMenu.title)) event.setCancelled(true);
    }
}

package me.pljr.playtime.config;

import me.pljr.playtime.PlayTime;
import me.pljr.pljrapi.managers.ConfigManager;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CfgTimeMenu {
    private static final ConfigManager config = PlayTime.getConfigManager();

    public static String title;
    public static ItemStack background;
    public static String headName;
    public static List<String> headLore;
    public static ItemStack yesterday;
    public static ItemStack daily;
    public static ItemStack all;
    public static ItemStack weekly;
    public static ItemStack monthly;

    public static void load(){
        CfgTimeMenu.title = config.getString("time-menu.title");
        CfgTimeMenu.background = config.getSimpleItemStack("time-menu.background");
        CfgTimeMenu.headName = config.getString("time-menu.head.name");
        CfgTimeMenu.headLore = config.getStringList("time-menu.head.lore");
        CfgTimeMenu.yesterday = config.getSimpleItemStack("time-menu.yesterday");
        CfgTimeMenu.daily = config.getSimpleItemStack("time-menu.daily");
        CfgTimeMenu.all = config.getSimpleItemStack("time-menu.all");
        CfgTimeMenu.weekly = config.getSimpleItemStack("time-menu.weekly");
        CfgTimeMenu.monthly = config.getSimpleItemStack("time-menu.monthly");
    }
}

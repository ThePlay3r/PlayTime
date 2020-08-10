package me.pljr.playtime.config;

import me.pljr.playtime.PlayTime;
import me.pljr.pljrapi.managers.ConfigManager;

public class CfgSettings {
    private static final ConfigManager config = PlayTime.getConfigManager();

    public static boolean afkHookCmi;

    public static void load(){
        CfgSettings.afkHookCmi = config.getBoolean("settings.afk-hook-cmi");
    }
}

package me.pljr.playtime.config;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.enums.Lang;
import me.pljr.pljrapi.managers.ConfigManager;

import java.util.HashMap;
import java.util.List;

public class CfgLang {
    private static final ConfigManager config = PlayTime.getConfigManager();

    public static List<String> help;
    public static HashMap<Lang, String> lang = new HashMap<>();

    public static void load(){
        CfgLang.help = config.getStringList("help");
        for (Lang lang : Lang.values()){
            CfgLang.lang.put(lang, config.getString("lang." + lang.toString()));
        }
    }
}

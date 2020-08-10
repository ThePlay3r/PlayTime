package me.pljr.playtime;

import me.pljr.playtime.commands.PlayTimeCommand;
import me.pljr.playtime.config.CfgLang;
import me.pljr.playtime.config.CfgSettings;
import me.pljr.playtime.config.CfgTimeMenu;
import me.pljr.playtime.listeners.AsyncPlayerPreLoginListener;
import me.pljr.playtime.listeners.CMIAfkEnterListener;
import me.pljr.playtime.listeners.CMIAfkLeaveListener;
import me.pljr.playtime.listeners.PlayerQuitListener;
import me.pljr.playtime.managers.QueryManager;
import me.pljr.pljrapi.PLJRApi;
import me.pljr.pljrapi.database.DataSource;
import me.pljr.pljrapi.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayTime extends JavaPlugin {
    private static PlayTime instance;
    private static ConfigManager configManager;
    private static QueryManager queryManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupPLJRApi()) return;
        instance = this;
        setupConfig();
        setupDatabase();
        setupListeners();
        setupCommands();
    }

    private boolean setupPLJRApi(){
        PLJRApi api = (PLJRApi) Bukkit.getServer().getPluginManager().getPlugin("PLJRApi");
        if (api == null){
            Bukkit.getConsoleSender().sendMessage("§cPlayTime: PLJRApi not found, disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }else{
            Bukkit.getConsoleSender().sendMessage("§aPlayTime: Hooked into PLJRApi!");
            return true;
        }
    }

    private void setupConfig(){
        saveDefaultConfig();
        configManager = new ConfigManager(getConfig(), "§cPlayTime:", "config.yml");
        CfgLang.load();
        CfgSettings.load();
        CfgTimeMenu.load();
    }

    private void setupDatabase(){
        DataSource dataSource = DataSource.getFromConfig(configManager);
        queryManager = new QueryManager(dataSource);
        queryManager.setupTables();
    }

    private void setupCommands(){
        getCommand("playtime").setExecutor(new PlayTimeCommand());
    }

    private void setupListeners(){
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        if (CfgSettings.afkHookCmi){
            getServer().getPluginManager().registerEvents(new CMIAfkEnterListener(), this);
            getServer().getPluginManager().registerEvents(new CMIAfkLeaveListener(), this);
        }
    }

    public static PlayTime getInstance() {
        return instance;
    }

    public static QueryManager getQueryManager() {
        return queryManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

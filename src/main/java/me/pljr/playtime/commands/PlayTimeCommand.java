package me.pljr.playtime.commands;

import me.pljr.playtime.config.CfgLang;
import me.pljr.playtime.enums.Lang;
import me.pljr.playtime.menus.TimeMenu;
import me.pljr.pljrapi.utils.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTimeCommand extends CommandUtil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(CfgLang.lang.get(Lang.NO_CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (!checkPerm(player, "playtime.use")) return false;

        // /playtime
        if (args.length == 0){
            TimeMenu.open(player, player);
            return true;
        }


        if (args.length == 1) {
            // /playtime help
            if (args[0].equalsIgnoreCase("help") && checkPerm(player, "playtime.help")){
                sendHelp(player, CfgLang.help);
                return true;
            }
            // /playtime <playerName>
            if (!checkPerm(player, "playtime.use.others")) return false;
            OfflinePlayer requested = Bukkit.getOfflinePlayer(args[0]);
            TimeMenu.open(player, requested);
            return true;
        }

        sendHelp(player, CfgLang.help);
        return false;
    }
}

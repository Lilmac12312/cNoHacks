package me.lilmac.cNoHacks.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

	public static void tell(CommandSender s, String msg){
		s.sendMessage((s instanceof Player ? ChatColor.translateAlternateColorCodes('&', msg) : ChatColor.stripColor(msg)));
	}

	public static String NoPermissions = "§7[§c!§7] You do not have permission!";

	public static String getPrefix(){
		return "§7[§c!§7] ";
	}
}
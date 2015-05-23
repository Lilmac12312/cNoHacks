package me.lilmac.cNoHacks.Commands;

import me.lilmac.cNoHacks.Main;
import me.lilmac.cNoHacks.Config.ConfigManager;
import me.lilmac.cNoHacks.Utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAlerts implements CommandExecutor {

	final Main plugin;
	public CmdAlerts(Main instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		Player p = (Player) sender;
		if(p.hasPermission("cnohacks.alerts") || p.isOp()) { 
			if (cmd.getName().equalsIgnoreCase("alerts")) {
				if(args.length == 0) {
					p.sendMessage(Utils.getPrefix() + "§cUsage:§7 /alerts <on/off> ");
				} else {
					if (args.length == 1) {
						if ((args[0].equalsIgnoreCase("off"))) { 
							p.sendMessage(Utils.getPrefix() + "§cYou have §6disabled §creceiving alerts!");
							ConfigManager.get("alerts.yml").set(p.getName() + ".Alerts", false);
							ConfigManager.save(plugin, "alerts.yml");
						} else {
							if ((args[0].equalsIgnoreCase("on"))) {
								p.sendMessage(Utils.getPrefix() + "§cYou have §6enabled §creceiving alerts!");
								ConfigManager.get("alerts.yml").set(p.getName() + ".Alerts", true);
								ConfigManager.save(plugin, "alerts.yml");
							}
						} 
					}
				}
			}
		} else {
			p.sendMessage(Utils.NoPermissions);
		}
		return false;
	}
}
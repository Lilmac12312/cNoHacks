package me.lilmac.cNoHacks;

import me.lilmac.cNoHacks.Commands.CmdAlerts;
import me.lilmac.cNoHacks.Config.ConfigManager;
import me.lilmac.cNoHacks.ForceField.ForceField;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public Main plugin;

	public void onEnable() {
		System.out.print("§acNoHacks > Enabled!");
		registerCommands();
		RunForceFieldCheck();
        setup();
        ConfigManager.load(this, "alerts.yml");
	}
	
	public void RunForceFieldCheck() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "test"); 
			}
		},0, 20);
	}
	
	public void setup() {
			Bukkit.getPluginManager().registerEvents(this, this);
			getServer().getPluginManager().registerEvents(new ForceField(this), this);

		}
	
	public void registerCommands() {
		getCommand("test").setExecutor(new ForceField(this));
		getCommand("alerts").setExecutor(new CmdAlerts(this));
	}
	
	public void onDisable() {
		 ConfigManager.save(this, "alerts.yml");
	}
}
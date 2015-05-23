package me.lilmac.cNoHacks.ForceField;

import me.lilmac.cNoHacks.Main;
import me.lilmac.cNoHacks.Config.ConfigManager;
import me.lilmac.cNoHacks.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ForceField implements CommandExecutor, Listener {

	public Main plugin;

	public ForceField(Main instance) {
		plugin = instance;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Villager && event.getEntity().hasMetaData("cancel")) {
 			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onVillagerDamage(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Villager) {
			Villager villager = (Villager) event.getEntity();
			Player damager = (Player) event.getDamager();
			if(villager.getCustomName().equals(damager.getName())) {
				for(Player online : Bukkit.getOnlinePlayers()) {
					if(online.hasPermission("cnohacks.view") || online.isOp()) {
						if (ConfigManager.contains("alerts.yml", online.getName())) {
							if (ConfigManager.get("alerts.yml").getBoolean(online.getName() + ".Alerts")) {
								online.sendMessage(Utils.getPrefix() + damager.getDisplayName() + " §7might be using §cForcefield");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onVillagerInteract(PlayerInteractEntityEvent event) { 
		if(event.getRightClicked() instanceof Villager) {
			Villager villager = (Villager) event.getRightClicked();
			if(villager.hasMetadata("cancel")) {
				event.setCancelled(true);
			}
		}
	}	

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!(ConfigManager.contains("alerts.yml", e.getPlayer().getName()))) {
			ConfigManager.set("alerts.yml", e.getPlayer().getName() + ".Alerts", false);
			ConfigManager.save(plugin, "alerts.yml");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		if (tag.equalsIgnoreCase("test")) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					final Villager villager = (Villager) online.getLocation().getWorld().spawnEntity(online.getLocation().subtract(3,0,3), EntityType.VILLAGER);
					villager.setCustomName(online.getName());
					villager.setCustomNameVisible(true);
					villager.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
					villager.setMetadata("cancel", (MetadataValue)new FixedMetadataValue((Plugin)plugin, (Object)true));
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						public void run() {
							vilager.removeMetaData("cancel");
							villager.remove();
						}
					}, 10);
				}
			}
		return false;
	}
}

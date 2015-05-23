package me.lilmac.cNoHacks.Utils;

import java.util.ArrayList;
import java.util.Hashtable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/* 
 * SpamCheck - Antispam  
 * 
 */

public class AntiSpam
{
	public static Hashtable<String, AntiSpam> Players = new Hashtable<String, AntiSpam>();
	public ArrayList<ChatMessage> Messages;
	public String PlayerName;
	public Player Player;
	public int Warnings;
	public int Violations;
	public long SilenceExpires;

	public static void OnPlayerChat(AsyncPlayerChatEvent event)
	{
		String playerName = event.getPlayer().getName();
		AntiSpam check = (AntiSpam)Players.get(playerName);

		if (check == null)
		{
			check = new AntiSpam(playerName);
			Players.put(playerName, check);
		}

		check.AddMessage(event);
	}

	public AntiSpam(String name)
	{
		this.Messages = new ArrayList<ChatMessage>();
		this.PlayerName = name;
		this.Warnings = 10;
		this.Violations = 5;
		this.SilenceExpires = 0L;
	}

	@SuppressWarnings("deprecation")
	public void AddMessage(AsyncPlayerChatEvent event)
	{
		this.Player = event.getPlayer();

		ChatMessage message = new ChatMessage(this.Player, System.currentTimeMillis(), event.getMessage());
		this.Messages.add(0, message);

		if (IsSilenced())
		{
			event.setCancelled(true);

			if (this.Warnings <= 0)
			{
				//String ipAddress = this.Player.getAddress().getAddress().getHostAddress();
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + Player.getName() + " You have been banned for spamming by CONSOLE. You can appeal on our forums");
				Player.setBanned(true);
				Bukkit.broadcastMessage(Utils.getPrefix() + this.Player.getDisplayName() + " has been banned for spamming by CONSOLE.");
				//Bukkit.banIP(ipAddress);
				return;
			}

			this.Player.sendMessage(ChatColor.RED + "YOU ARE CURRENTLY SILENCED FOR SPAM! " + SilenceTimeRemaining() + " REMAINING.");
			this.Player.sendMessage(ChatColor.RED + "IF YOU CONTINUE TO USE PUBLIC CHAT WHILE SILENCED, YOU WILL BE BANNED!");
			this.Player.sendMessage(ChatColor.RED + "YOU HAVE " + this.Warnings + " WARNINGS REMAINING!");

			this.Warnings -= 1;
		}
		else
		{
			this.Warnings = 10;
			CheckSpamming();
		}

		if (this.Messages.size() > 10)
		{
			this.Messages.remove(this.Messages.size() - 1);
		}
	}

	public void CheckSpamming()
	{
		boolean isSpamming = false;
		if (this.Messages.size() >= 3)
		{
			String msg = ((ChatMessage)this.Messages.get(0)).message;
			boolean same = true;
			for (int i = 1; i < 3; i++)
			{
				same = (same) && (msg.equals(((ChatMessage)this.Messages.get(i)).message));
			}

			long time = ((ChatMessage)this.Messages.get(0)).when - ((ChatMessage)this.Messages.get(2)).when;

			isSpamming = (isSpamming) || (time < 1550L);

			isSpamming = (isSpamming) || ((time < 5000L) && (same));
		}

		if (isSpamming)
		{
			Silence(60);
			this.Warnings -= 1;
		}
	}

	public void Silence(int seconds)
	{
		this.Violations -= 1;
		this.SilenceExpires = (System.currentTimeMillis() + seconds * 1000);
		this.Player.sendMessage(ChatColor.RED + "YOU HAVE BEEN SILENCED FOR SPAM FOR " + seconds + " SECONDS");
		this.Player.sendMessage(ChatColor.RED + "IF YOU CONTINUE TO USE PUBLIC CHAT WHILE SILENCED, YOU WILL BE BANNED!");
	}

	public String SilenceTimeRemaining()
	{
		long left = (this.SilenceExpires - System.currentTimeMillis()) / 1000L;

		if (left < 60L) {
			return left + " SECONDS";
		}
		return left + " MINUTES";
	}

	public boolean IsSilenced()
	{
		return this.SilenceExpires > System.currentTimeMillis();
	}

	public void Clear() {
		this.Messages.clear();
	}
	public class ChatMessage {
		public Player player;
		public long when;
		public String message;

		public ChatMessage(Player p, long w, String msg) {
			this.player = p;
			this.when = w;
			this.message = msg;
		}
	}
	public static void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		// TODO Auto-generated method stub

	}
}
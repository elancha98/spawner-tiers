package me.commandcraft.spawnerlevel;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

	public static void process(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "error: you have to be a player to use this command");
		} else {
			process((Player) sender, args);
		}
	}
	
	public static void process(Player player, String[] args) {
		if (!player.hasPermission("spawnertier.levelup")) {
			player.sendMessage(ChatColor.RED + "error: you don't have permission to use this command");
			return;
		}
		int level = Main.levelManager.getLevel(player.getName());
		double d = Main.configuration.getCost(level+1);
		if (d < 0) {
			player.sendMessage(ChatColor.RED + "error: you are already maxed out");
			return;
		}
		if (Main.economy.has(player, d)) {
			Main.levelManager.levelUp(player);
			Main.economy.withdrawPlayer(player, d);
			player.sendMessage(ChatColor.GREEN + "Leveled up!");
		} else {
			player.sendMessage(ChatColor.RED + "error: you need more money to do that");
		}
	}
}

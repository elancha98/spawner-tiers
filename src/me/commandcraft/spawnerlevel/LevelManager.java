package me.commandcraft.spawnerlevel;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LevelManager implements Listener {

	FileConfiguration config;
	
	public LevelManager(File folder) {
		File file = new File(folder, "levels.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save(File folder) {
		try {
			config.save(new File(folder, "levels.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getLevel(String player) {
		if (config.contains(player)) return config.getInt(player);
		config.set(player, 0);
		return 0;
	}
	
	public void levelUp(Player player) {
		int lvl = config.getInt(player.getName());
		lvl += 1;
		config.set(player.getName(), lvl);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType().equals(Material.MOB_SPAWNER)) {
			Player player = event.getPlayer();
			CreatureSpawner data = (CreatureSpawner) block.getState();
			String spawned = data.getSpawnedType().name();
			if (Main.configuration.getSpawnerLevel(spawned) > getLevel(player.getName())) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You need more level to place that spawner");
				player.sendMessage(ChatColor.RED + "Please use /levelup");
			}
		}
	}
}

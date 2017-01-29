package me.commandcraft.spawnerlevel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class Configuration {
	
	FileConfiguration config;

	public Configuration(File folder)  {
		File file = new File(folder, "config.yml");
		if (!file.exists()) makeDefaultConfig(file);
		else config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save(File folder) {
		try {
			config.save(new File(folder, "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void makeDefaultConfig(File f) {
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileConfiguration temp = new YamlConfiguration();
		List<String> list0 = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();
		for (EntityType type : EntityType.values()) {
			if (type.equals(EntityType.BLAZE)) list1.add(type.name());
			else list0.add(type.name());
		}
		temp.set("0.allowed", list0);
		temp.set("0.cost", 0);
		temp.set("1.allowed", list1);
		temp.set("1.cost", 500);
		config = temp;
	}
	
	public int getSpawnerLevel(String entity) {
		int i = 0;
		while (true) {
			List<String> strings = config.getStringList(Integer.toString(i));
			if (strings == null) {
				List<String> zero = config.getStringList("0");
				zero.add(entity);
				config.set("0", zero);
				return 0;
			}
			if (strings.contains(entity)) return i;
			i++;
		}
	}
	
	public double getCost(int level) {
		String path = Integer.toString(level);
		if (config.contains(path)) {
			return config.getDouble(path + ".cost");
		}
		return -1;
	}
}

package me.commandcraft.spawnerlevel;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Logger logger;
	public static Configuration configuration;
	public static LevelManager levelManager;
	public static Economy economy;
	
	public void onEnable() {
		logger = Bukkit.getLogger();
		
		getDataFolder().mkdirs();
		configuration = new Configuration(getDataFolder());
		levelManager = new LevelManager(getDataFolder());
		Bukkit.getPluginManager().registerEvents(levelManager, this);
		
		if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}
	
	public void onDisable() {
		configuration.save(getDataFolder());
		levelManager.save(getDataFolder());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("levelup")) {
			CommandManager.process(sender, args);
		}
		return true;
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}

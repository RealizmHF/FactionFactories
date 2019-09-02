package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		
		createConfig();
		
		createFactoryConfig();
		
		FactoryManager factories = new FactoryManager(this);
		
		factories.getFactoryManager().reloadFactories();
		
		FactoryEvent fEvent = new FactoryEvent(this);
		
		getServer().getPluginManager().registerEvents(new FactoryEvent(this), this);
		
	}

	@Override
	public void onDisable() {
		
		this.saveConfig();
	}

	
	private void createFactoryConfig() {
		
		File file = new File("");
		try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            file = new File(getDataFolder(), "factoryconfig.yml");
            if (!file.exists()) {
                getLogger().info("FactoryConfig.yml not found, creating!");
                this.saveConfig();
            } else {
                getLogger().info("FactoryConfig.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
		
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		
		c.options().header("FactionFactories Config: \n");

		c.createSection("factories");
		
		c.options().copyDefaults(true);
		try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createConfig() {
		
		File file = new File("");
		try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                this.saveConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
		
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		
		c.options().header("FactionFactories Config: \n");

		c.addDefault("factoryBlock", Material.GOLD_BLOCK.toString());
		
		
		c.addDefault("radius", 5);
		
		c.addDefault("space between factories", 3);
		
		c.addDefault("inventory size", 9);
		c.addDefault("repair", 1.1);
		c.addDefault("health timer", 1000);

		c.createSection("tiers");
		c.addDefault("tiers.coal", 1);
		c.addDefault("tiers.iron", 2);
		c.addDefault("tiers.gold", 3);
		c.addDefault("tiers.diamond", 4);
		
		
		c.addDefault("new factory message", "You've created a new Factory!");
		
		c.addDefault("new authorized", "You've been authorized!");
		
		c.addDefault("factory break error", "Sorry, you don't own this factory!");
		
		c.addDefault("not authorized", "Sorry, you are not authorized in this factory!");
		
		c.createSection("items");
		c.addDefault("items.stone", 100);
		c.addDefault("items.coal", 80);
		c.addDefault("items.iron", 60);
		c.addDefault("items.gold", 40);
		c.addDefault("items.diamond", 20);
		c.addDefault("items.emerald", 15);
		
		
		c.options().copyDefaults(true);
		try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
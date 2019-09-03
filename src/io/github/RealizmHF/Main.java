package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private File cFile = null;
	private File cFFile = null;
	private YamlConfiguration c;
	private YamlConfiguration cf;
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		
		createConfig();
		
		createFactoryConfig();
		
		FactoryManager factories = new FactoryManager(this);
		
		try {
			factories.getFactoryManager().reloadFactories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FactoryEvent fEvent = new FactoryEvent(this);
		
	    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	    	
            public void run() {
                FactoryScheduleManager.handleSchedules();
            }
        }, 1L, 1L);
	}

	@Override
	public void onDisable() {
		
		try {
			this.c.save(cFile);
			this.cf.save(cFFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void createFactoryConfig() {
		
		try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            cFFile = new File(getDataFolder(), "factoryconfig.yml");
            if (!cFFile.exists()) {
                getLogger().info("FactoryConfig.yml not found, creating!");
                this.saveConfig();
            } else {
                getLogger().info("FactoryConfig.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
		
		cf = YamlConfiguration.loadConfiguration(cFFile);
		
		cf.options().header("FactionFactories Config: \n");

		cf.createSection("factories");
		
		cf.options().copyDefaults(true);
		try {
			cf.save(cFFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public YamlConfiguration getC() {
		return this.c;
	}
	public YamlConfiguration getCF() {
		return this.cf;
	}
	public File getCFile() {
		return this.cFile;
	}
	public File getCFFile() {
		return this.cFFile;
	}
	private void createConfig() {
		
		try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            cFile = new File(getDataFolder(), "config.yml");
            if (!cFile.exists()) {
                getLogger().info("Config.yml not found, creating!");
                this.saveConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
		
		c = YamlConfiguration.loadConfiguration(cFile);
		
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
			c.save(cFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
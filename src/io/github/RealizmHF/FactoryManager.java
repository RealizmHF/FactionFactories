package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FactoryManager {

	private ArrayList<Factory> factories = new ArrayList<Factory>();
	private Main plugin;
	public static FactoryManager fManager = new FactoryManager();
	private int k = 0;
	
	/*
	 * Creates a FactoryManager
	 */
	public FactoryManager(Main plugin) {
		this.plugin = plugin;
	}
	public FactoryManager() {}
	/*
	 * 
	 * 
	 * Getters
	 * 
	 * 
	 */
	public ArrayList<Factory> getFactories() {
		return factories;
	}
	public FactoryManager getFactoryManager(){
		return fManager;
	}
	/*
	 * 
	 * 
	 * Functions
	 * 
	 * 
	 */
	public void addFactory(Factory newFactory) {
		factories.add(newFactory);
	}
	public void removeFactory(int id) throws IOException {
		factories.remove(id);
		this.plugin.getCF().set("factories." + id, null);
		this.plugin.getCF().save(this.plugin.getCFFile());
	}

	public void reloadFactories() throws IOException {
		//Recreates all of the factories that have been saved after a server restart
		
		for(int k = 0; k < this.plugin.getCF().getIntegerList("factories.").size(); k++) {
			
			Player player = (Player) this.plugin.getServer().getOfflinePlayer(UUID.fromString(this.plugin.getCF().getString("factories." + k)));
			Location location = new Location(this.plugin.getServer().getWorld("world"), this.plugin.getCF().getDouble("factories." + k + ".x"), this.plugin.getCF().getDouble("factories." + k + ".y"), this.plugin.getCF().getDouble("factories." + k + ".z"));
			int tier = this.plugin.getCF().getInt("factories." + k + ".tier");
			int level = this.plugin.getCF().getInt("factories." + k + ".tier");
			factories.add(new Factory(this.plugin, player, location, k, tier, level));
		}
	}
	public boolean inRadius(Factory newFactory) {
		//If the location is within the radius of a cupboard, return that cupboard's claim
		
		Location loc = newFactory.getFactoryLocation();
		
		if(this.factories.size() > 0) {
			
			for(Factory current : this.factories) {
				
				int radius = current.getFactoryRadius();
				int x = current.getFactoryLocation().getBlockX();
				int z = current.getFactoryLocation().getBlockZ();
				
				//If the block is within the radius of a faction, return true
				if(loc.getBlockX() >= x - radius && loc.getBlockX() <= x + radius && loc.getBlockZ() >= z - radius && loc.getBlockZ() <= z + radius) {
					return true;
				}
			}
		}
		return false;
	}
}

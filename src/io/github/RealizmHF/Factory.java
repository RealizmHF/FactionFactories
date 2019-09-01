package io.github.RealizmHF;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Factory {
	
	private Main plugin;
	private boolean isBroken = false;
	private int health;
	private double repairMultiplier;
	private Location factoryLocation;
	private int inventorySize;
	private int factoryRadius;
	private ArrayList<UUID> authorized;
	private int factoryLevel;
	private int factoryTier;
	private Material tempBluePrint = Material.GOLD_BLOCK;
	private int factoryID;
	
	private YamlConfiguration factoryFile = YamlConfiguration.loadConfiguration(new File("factoryconfig.yml"));
	private YamlConfiguration configFile = YamlConfiguration.loadConfiguration(new File("config.yml"));
	
	
	/*
	 * Creates a Factory of tier 1
	 */
	public Factory(Main plugin, Player p, Location l, int id) {
		this.plugin = plugin;
		
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = configFile.getDouble("repair");
		inventorySize = configFile.getInt("inventory size");
		factoryRadius = configFile.getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryLevel = 0;
		factoryTier = 1;
		
		//Add factory to config
		factoryFile.createSection("factories." + id);
		factoryFile.createSection("factories." + id + ".authorized");
		factoryFile.createSection("factories." + id + ".authorized." + p.getUniqueId());
		factoryFile.addDefault("factories." + id + ".x", l.getX());
		factoryFile.addDefault("factories." + id + ".y", l.getY());
		factoryFile.addDefault("factories." + id + ".z", l.getZ());
		factoryFile.addDefault("factories." + id + ".level", this.getFactoryLevel());
		factoryFile.addDefault("factories." + id + ".tier", this.getFactoryTier());
		
	}
	
	/*
	 * Creates a Factory with a custom tier
	 */
	public Factory(Main plugin, Player p, Location l, int id, int tier) {
		this.plugin = plugin;
		
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = configFile.getDouble("repair");
		inventorySize = configFile.getInt("inventory size");
		factoryRadius = configFile.getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryLevel = 0;
		factoryTier = tier;
		
		//Add factory to config
		factoryFile.createSection("factories." + id);
		factoryFile.createSection("factories." + id + ".authorized");
		factoryFile.createSection("factories." + id + ".authorized." + p.getUniqueId());
		factoryFile.addDefault("factories." + id + ".x", l.getX());
		factoryFile.addDefault("factories." + id + ".y", l.getY());
		factoryFile.addDefault("factories." + id + ".z", l.getZ());
		factoryFile.addDefault("factories." + id + ".level", this.getFactoryLevel());
		factoryFile.addDefault("factories." + id + ".tier", this.getFactoryTier());
				
	}
	/*
	 * 
	 * 
	 * Getters
	 * 
	 * 
	 */
	public boolean isBroke() {
		return isBroken;
	}
	public int getHealth() {
		return health;
	}
	public Location getFactoryLocation() {
		return factoryLocation;
	}
	public ArrayList<UUID> getAuthorized(){
		return authorized;
	}
	public int getFactoryLevel() {
		return factoryLevel;
	}
	public int getFactoryRadius() {
		return factoryRadius;
	}
	public int getFactoryTier() {
		return factoryTier;
	}
	/*
	 * 
	 * 
	 * Setters
	 * 
	 * 
	 */
	public void setBroke() {
		//sets isBroke to the opposite of its current value
		isBroken = !isBroken;
	}
	public void setHeatlh(int newHealth) {
		health = newHealth;
	}
	public void setFactoryLocation(Location newLocation) {
		factoryLocation = newLocation;
	}
	public void setFactoryInventorySize(int newSize) {
		inventorySize = newSize;
	}
	public void setFactoryLevel(int newLevel) {
		factoryLevel = newLevel;
	}
	public void setFactoryRadius(int newRadius) {
		factoryRadius = newRadius;
	}
}

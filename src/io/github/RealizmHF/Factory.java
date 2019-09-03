package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
	
	
	/*
	 * Creates a Factory of tier 1
	 */
	public Factory(Main plugin, Player p, Location l, int id) throws IOException {
		this.plugin = plugin;
		
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = plugin.getC().getDouble("repair");
		inventorySize = plugin.getC().getInt("inventory size");
		factoryRadius = plugin.getC().getInt("radius");
		factoryLocation = l;
		authorized = new ArrayList<UUID>();
		authorized.add(p.getUniqueId());
		factoryLevel = 0;
		factoryTier = 1;
		
		//Add factory to config
		plugin.getCF().createSection("factories." + id);
		plugin.getCF().createSection("factories." + id + ".authorized");
		plugin.getCF().createSection("factories." + id + ".authorized." + p.getUniqueId());
		plugin.getCF().addDefault("factories." + id + ".x", l.getX());
		plugin.getCF().addDefault("factories." + id + ".y", l.getY());
		plugin.getCF().addDefault("factories." + id + ".z", l.getZ());
		plugin.getCF().addDefault("factories." + id + ".level", this.getFactoryLevel());
		plugin.getCF().addDefault("factories." + id + ".tier", this.getFactoryTier());
		
		plugin.getCF().save(plugin.getCFFile());
	}
	
	/*
	 * Creates a Factory with a custom tier
	 */
	public Factory(Main plugin, Player p, Location l, int id, int tier, int level) throws IOException {
		this.plugin = plugin;
		
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = plugin.getC().getDouble("repair");
		inventorySize = plugin.getC().getInt("inventory size");
		factoryRadius = plugin.getC().getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryLevel = level;
		factoryTier = tier;
		
		//Add factory to config
		plugin.getCF().createSection("factories." + id);
		plugin.getCF().createSection("factories." + id + ".authorized");
		plugin.getCF().createSection("factories." + id + ".authorized." + p.getUniqueId());
		plugin.getCF().addDefault("factories." + id + ".x", l.getX());
		plugin.getCF().addDefault("factories." + id + ".y", l.getY());
		plugin.getCF().addDefault("factories." + id + ".z", l.getZ());
		plugin.getCF().addDefault("factories." + id + ".level", this.getFactoryLevel());
		plugin.getCF().addDefault("factories." + id + ".tier", this.getFactoryTier());
		
		plugin.getCF().save(plugin.getCFFile());
	}
	/*
	 * 
	 * 
	 * Getters
	 * 
	 * 
	 */
	public double getRepairMultiplier() {
		return repairMultiplier;
	}
	public int getFactoryID() {
		return factoryID;
	}
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

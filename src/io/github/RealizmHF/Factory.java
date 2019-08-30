package io.github.RealizmHF;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Factory {
	
	private Main plugin;
	private boolean isBroken = false;
	private int health;
	private double repairMultiplier;
	private Location factoryLocation;
	private int inventorySize;
	private int factorySize;
	private ArrayList<UUID> authorized;
	private int factoryLevel;
	private int factoryTier;
	private Material tempBluePrint = Material.GOLD_BLOCK;
	
	
	public Factory(Main plugin, Player p, Location l) {
		this.plugin = plugin;
		
		isBroken = false;
		health = 10;
		repairMultiplier = plugin.getConfig().getDouble("repair");
		inventorySize = plugin.getConfig().getInt("inventory size");
		factorySize = plugin.getConfig().getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryLevel = 0;
		factoryTier = 1;
		
	}
	
	public Factory(Main plugin, Player p, Location l, int tier) {
		this.plugin = plugin;
		
		isBroken = false;
		health = 10;
		repairMultiplier = plugin.getConfig().getDouble("repair");
		inventorySize = plugin.getConfig().getInt("inventory size");
		factorySize = plugin.getConfig().getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryLevel = 0;
		factoryTier = tier;
		
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
	public int getFactoryTier() {
		return factoryTier;
	}
	
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
}
package io.github.RealizmHF;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FactoryInventory {

	private Main plugin;
	private Inventory userFactory;
	private int size;
	/*
	 * 
	 * 
	 * Create like an EnderChest
	 * 
	 * Only create these once a player aquires a Factory in their inventory
	 * Run /factory save when first getting into the players inventory
	 * Can just search through factories for who they are owned by
	 */
	
	public FactoryInventory(Main plugin, Player player) {
		this.plugin = plugin;
		userFactory = Bukkit.createInventory(player, size, "Available Factories");
	}
	/*
	 * 
	 * 
	 * Getters
	 * 
	 * 
	 */
	public Inventory getFactoryInventory() {
		return this.userFactory;
	}
	/*
	 * 
	 * 
	 * Setters
	 * 
	 * 
	 */
	public void setFactoryInventorySize(int size) {
		this.size = size;
	}
}

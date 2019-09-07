package io.github.RealizmHF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FactoryManager {

	private ArrayList<UUID> authorizedPlayers = new ArrayList<UUID>();
	private ArrayList<Factory> factories = new ArrayList<Factory>();
	private ArrayList<ItemStack> bluePrints = new ArrayList<ItemStack>();
	private ArrayList<FactoryInventory> userFactoryInventory = new ArrayList<FactoryInventory>();
	private Main plugin;
	
	public static FactoryManager fManager = new FactoryManager();
	
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
	public ArrayList<FactoryInventory> getUserFactoryInventory() {
		return userFactoryInventory;
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
	public void addUserFactoryInventory(Player player) {
		this.userFactoryInventory.add(new FactoryInventory(this.plugin, player));
	}
	public void addBluePrint(ItemStack item) {
		bluePrints.add(item);
	}
	public Factory isBluePrint(ItemStack item) {
		
		if(this.bluePrints.contains(item)) {
			System.out.println("BluePrint Found");
			ItemStack bluePrint = this.bluePrints.get(this.bluePrints.indexOf(item));
			int id = Integer.parseInt(item.getItemMeta().getLore().get(2).substring(4));
			System.out.println(id);
			
			for(Factory current : this.factories) {
				if(current.getFactoryID() == id)
					return current;
			}
		}
		return null;
	}
	public void addFactory(Factory newFactory) {
		factories.add(newFactory);
	}
	public void removeFactory(int id) throws IOException {
		factories.remove(id);
		if(this.plugin.getCF().get(Integer.toString(id)) != null)
			this.plugin.getCF().set(Integer.toString(id), null);
		else
			this.plugin.getServer().broadcastMessage("Factory not removed from config");
		this.plugin.getCF().save(this.plugin.getCFFile());
	}

	public void reloadFactories() throws IOException {
		//Recreates all of the factories that have been saved after a server restart
		
		//this.plugin.getServer().broadcastMessage((String) this.plugin.getCF().g("factories.").get(0));
		
		Set<String> temp = this.plugin.getCF().getKeys(true);
			System.out.println(temp);
//			Player player = (Player) this.plugin.getServer().getOfflinePlayer(UUID.fromString(temp + "."));
//			Location location = new Location(this.plugin.getServer().getWorld("world"), this.plugin.getCF().getDouble("factories." + k + ".x"), this.plugin.getCF().getDouble("factories." + k + ".y"), this.plugin.getCF().getDouble("factories." + k + ".z"));
//			int tier = this.plugin.getCF().getInt("factories." + k + ".tier");
//			int rank = this.plugin.getCF().getInt("factories." + k + ".rank");
//			String type = this.plugin.getCF().getString("factories." + k + ".type");
//			factories.add(new Factory(this.plugin, player, location, k, tier, rank, type));
	}
	public boolean inRadius(Location loc) {
		//If the location is within the radius of a factory, return true
		
		int factorySpacing = this.plugin.getC().getInt("space between factories");
		
		if(this.factories.size() > 0) {
			
			for(Factory current : this.factories) {
				
				if(current.getFactoryLocation() != null) {
					int radius = current.getFactoryRadius() + factorySpacing;
					int x = current.getFactoryLocation().getBlockX();
					int z = current.getFactoryLocation().getBlockZ();
					
					//If the block is within the radius of a faction, return true
					if(loc.getBlockX() >= x - radius && loc.getBlockX() <= x + radius && loc.getBlockZ() >= z - radius && loc.getBlockZ() <= z + radius) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public Factory inRadius(Player player) {
		//If the player location is within the radius of a factory, return factory
		
		Location loc = player.getLocation();
		int factorySpacing = this.plugin.getC().getInt("space between factories");
		
		
		if(this.factories.size() > 0) {
			
			for(Factory current : this.factories) {
				
				if(current.getFactoryLocation() != null) {
					int radius = current.getFactoryRadius() + factorySpacing;
					int x = current.getFactoryLocation().getBlockX();
					int z = current.getFactoryLocation().getBlockZ();
					
					//If the block is within the radius of a faction, return the factory
					if(loc.getBlockX() >= x - radius && loc.getBlockX() <= x + radius && loc.getBlockZ() >= z - radius && loc.getBlockZ() <= z + radius) {
						return current;
					}
				}
			}
		}
		return null;
	}
	public ArrayList<UUID> getAuthorizedPlayers() {
		return this.authorizedPlayers;
	}
}

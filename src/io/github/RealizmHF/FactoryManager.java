package io.github.RealizmHF;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class FactoryManager {

	private ArrayList<Factory> factories = new ArrayList<Factory>();
	private Main plugin;
	public static FactoryManager fManager = new FactoryManager();
	private YamlConfiguration config = new YamlConfiguration().loadConfiguration(new File("config.yml"));
	private int timer = config.getInt("health timer");
	private int k = 0;
	
	/*
	 * Creates a FactoryManager
	 */
	public FactoryManager(Main plugin) {
		this.plugin = plugin;
		
		//Remove a health point ever 'timer' seconds minus the repairMultiplier times 'timer', times the factories tier
		//Default is 10% less time at rank 1, decreasing by 10% per tier.
		//Do this until health is 0
		if(factories.size() > 0) {
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				
				public void run() {
					
					Bukkit.getServer().broadcastMessage("ID: " + factories.get(k).getFactoryID() + " Health: " + factories.get(k).getHealth());
						
					//If the factories health is greater than 0, lower health by 1
					//Else, the factories health is depleted, and is now broken
					if(factories.get(k).getHealth()-1 > 0)
						factories.get(k).setHeatlh(factories.get(k).getHealth()-1);
					else
						factories.get(k).isBroke();
					
					//If k is less than the number of factories, add to k
					//Else, restart k from the beginning index
					if(k < factories.size()) {
						k++;
					}
					else {
						k = 0;
					}
				}
				
			}, (long)((timer)-((timer*factories.get(k).getRepairMultiplier())*factories.get(k).getFactoryTier())), 10);

		}
	}
	public FactoryManager() {
		
	}
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
	public void removeFactory(int id) {
		
	}

	public void reloadFactories() {
		//Recreates all of the factories that have been saved after a server restart
		
	}
	public boolean inRadius(Factory newFactory) {
		//Checks if this newFactory is going to overlap with already existing factories
		
		/*
		 * Location (X, Z) positions of the newFactory corners
		 */
		int xPlus = (int)newFactory.getFactoryLocation().getBlockX() + newFactory.getFactoryRadius();
		int xMinus = (int)newFactory.getFactoryLocation().getBlockX() - newFactory.getFactoryRadius();
		int zPlus = (int)newFactory.getFactoryLocation().getBlockZ() + newFactory.getFactoryRadius();
		int zMinus = (int)newFactory.getFactoryLocation().getBlockZ() - newFactory.getFactoryRadius();

		boolean test = false;
		//Iterate through the current factories, comparing locations
		for(Factory tempFactory : factories) {
			
			/*
			 * Location (X, Z) positions of current factory corners
			 */
			int tempXPlus = (int)tempFactory.getFactoryLocation().getBlockX() + tempFactory.getFactoryRadius();
			int tempXMinus = (int)tempFactory.getFactoryLocation().getBlockX() - tempFactory.getFactoryRadius();
			int tempZPlus = (int)tempFactory.getFactoryLocation().getBlockZ() + tempFactory.getFactoryRadius();
			int tempZMinus = (int)tempFactory.getFactoryLocation().getBlockZ() - tempFactory.getFactoryRadius();
			
			//If the newFactory isn't within a factory
			if(tempXPlus < xMinus && tempXMinus > xPlus && tempZPlus < zMinus && tempZMinus > zPlus) {
				test = true;
			}
			else {
				return false;
			}
		}
		return test;
	}
}

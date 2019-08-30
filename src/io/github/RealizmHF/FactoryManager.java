package io.github.RealizmHF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class FactoryManager {

	private ArrayList<Factory> factories = new ArrayList<Factory>();
	private Main plugin;
	public static FactoryManager fManager = new FactoryManager();
	
	/*
	 * Creates a FactoryManager
	 */
	public FactoryManager(Main plugin) {
		this.plugin = plugin;
		
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
		int xPlus = (int)newFactory.getFactoryLocation().getX() + newFactory.getFactoryRadius();
		int xMinus = (int)newFactory.getFactoryLocation().getX() - newFactory.getFactoryRadius();
		int zPlus = (int)newFactory.getFactoryLocation().getZ() + newFactory.getFactoryRadius();
		int zMinus = (int)newFactory.getFactoryLocation().getZ() - newFactory.getFactoryRadius();
		
		//Iterate through the current factories, comparing locations
		for(Factory tempFactory : factories) {
			
			/*
			 * Location (X, Z) positions of current factory corners
			 */
			int tempXPlus = (int)tempFactory.getFactoryLocation().getX() + tempFactory.getFactoryRadius();
			int tempXMinus = (int)tempFactory.getFactoryLocation().getX() - tempFactory.getFactoryRadius();
			int tempZPlus = (int)tempFactory.getFactoryLocation().getZ() + tempFactory.getFactoryRadius();
			int tempZMinus = (int)tempFactory.getFactoryLocation().getZ() - tempFactory.getFactoryRadius();
			
			//If the newFactory isn't within a factory
			if(tempXPlus < xMinus && tempXMinus > xPlus && tempZPlus < zMinus && tempZMinus > zPlus) {
				
			}
			else {
				return true;
			}
		}
		return false;
	}
}

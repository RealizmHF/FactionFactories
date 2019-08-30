package io.github.RealizmHF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class FactoryManager {

	private ArrayList<Factory> factories = new ArrayList<Factory>();
	private Main plugin;
	
	public static FactoryManager fManager = new FactoryManager();
	
	public FactoryManager(Main plugin) {
		this.plugin = plugin;
		
	}
	
	public FactoryManager() {
		
	}

	public FactoryManager getFactoryManager(){
		return fManager;
	}

	public void reloadFactories() {
		//Recreates all of the factories that have been saved after a server restart
		
	}
}

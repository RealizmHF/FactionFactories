package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

public class FactoryEvent implements Listener {

	private Main plugin;
	private FactoryManager factories = new FactoryManager(plugin);
	private int count = 0;
	
	public FactoryEvent(Main plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onBluePrintPlaced(BlockPlaceEvent event) throws IOException {
		
		//Check to make sure the block being placed is a factory block
		if(event.getBlock().getType() == Material.GOLD_BLOCK) {
			
			Faction faction = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));

			//Check if the factory is being placed inside a faction
			//Check if the player is in their own faction territory
			if(faction != null && MPlayer.get(event.getPlayer().getUniqueId()).isInOwnTerritory()) {

				int radius = this.plugin.getC().getInt("radius");
				Faction checkXPlus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getX() + radius, event.getBlock().getLocation().getY(), event.getBlock().getLocation().getZ())));
				Faction checkXMinus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getX() - radius, event.getBlock().getLocation().getY(), event.getBlock().getLocation().getZ())));
				Faction checkZPlus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getX(), event.getBlock().getLocation().getY(), event.getBlock().getLocation().getZ() + radius)));
				Faction checkZMinus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getBlock().getWorld(), event.getBlock().getLocation().getX(), event.getBlock().getLocation().getY(), event.getBlock().getLocation().getZ() - radius)));


				//Check if the factory is completely inside the faction
				if(checkXPlus != null && checkXMinus != null && checkZPlus != null && checkZMinus != null) {

					Factory newFactory = new Factory(plugin, event.getPlayer(), event.getBlock().getLocation(), count);
					
					//Check if the factory is overlapping another factory
					if(!factories.inRadius(newFactory)) {

						//Add the new factory to the current list of factories
						factories.addFactory(newFactory);
						FactoryScheduleManager.add(event.getPlayer(), newFactory, this.plugin.getC().getLong("health timer"), System.currentTimeMillis());
						count++;
						
						event.getPlayer().sendMessage("New Factory Placed!");
						
					}
					else {
						//Else, Factory is overlapping already existing factory(s)
						/*
						 * Plans to format text with bold and coloring
						 */
						event.setCancelled(true);
						event.getPlayer().sendMessage("[FF] There isn't enough faction land for a factory here!");
					}
					
				}
				else {
					//Else, Factory isn't completely inside the faction
					/*
					 * Plans to format text with bold and coloring
					 */
					event.setCancelled(true);
					event.getPlayer().sendMessage("[FF] There isn't enough faction land for a factory here!");
				}
			}
			else {
				//Else, Factory isn't placed inside a Faction
				/*
				 * Plans to format text with bold and coloring
				 */
				event.setCancelled(true);
				event.getPlayer().sendMessage("[FF] You can't place a factory outside of your own faction territory!");
			}
			
			
		}
	}
}

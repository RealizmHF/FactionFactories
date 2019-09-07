package io.github.RealizmHF;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.MaxChangedBlocksException;


public class FactoryEvent implements Listener, TouchHandler {

	private Main plugin;
	private FactoryManager factories = FactoryManager.fManager;
	
	private int count = 0;
	
	public FactoryEvent(Main plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onBluePrintUsed(PlayerInteractEvent event) {
		
		//If the player is clicking a block with a Blue Print in hand
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Factory factory = this.factories.isBluePrint(event.getItem());
			
			if(factory != null) {
				
				//If the  location is null, it hasn't been placed. Stops duplicates
				if(factory.getFactoryLocation() ==  null) {

					Faction faction = BoardColl.get().getFactionAt(PS.valueOf(event.getClickedBlock().getLocation()));
					
					//Check if the factory is being placed inside a faction
					//Check if the player is in their own faction territory
					if(faction != null && MPlayer.get(event.getPlayer().getUniqueId()).isInOwnTerritory()) {
						
						int radius = factory.getFactoryRadius();
						Faction checkXPlus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getX() + radius, event.getClickedBlock().getLocation().getY(), event.getClickedBlock().getLocation().getZ())));
						Faction checkXMinus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getX() - radius, event.getClickedBlock().getLocation().getY(), event.getClickedBlock().getLocation().getZ())));
						Faction checkZPlus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(), event.getClickedBlock().getLocation().getZ() + radius)));
						Faction checkZMinus = BoardColl.get().getFactionAt(PS.valueOf(new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation().getX(), event.getClickedBlock().getLocation().getY(), event.getClickedBlock().getLocation().getZ() - radius)));


						//Check if the factory is completely inside the faction
						if(checkXPlus != null && checkXMinus != null && checkZPlus != null && checkZMinus != null) {

							//Check if the factory is overlapping another factory
							if(!factories.inRadius(event.getClickedBlock().getLocation())) {
								
								factory.setFactoryLocation(event.getClickedBlock().getLocation());
								factory.createFactoryName();
								factory.setLocationConfigs();
								factory.pasteFactorySchematic();
								//Add the new factory to the current list of factories
								FactoryScheduleManager.add(event.getPlayer(), factory, this.plugin.getC().getLong("health timer"), System.currentTimeMillis());
								
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
				else {
					//Else, Factory has a location and therefore cannot be placed again
					/*
					 * Plans to format text with bold and coloring
					 */
					event.getPlayer().sendMessage("[FF] This factory has already been placed!");
				}
			}
		}
	}
	
	@EventHandler
	public void onFactoryInventoryClose(InventoryCloseEvent event) {
		
		//If the inventory is a Factories Inventory
		if(event.getInventory().getName().contains("'s Factory")) {

			this.factories.inRadius((Player) event.getPlayer()).deleteGUI();
			
		}
	}
	
	@EventHandler
	public void onFactoryEntrance(PlayerMoveEvent event) {
		
		Factory current = this.factories.inRadius(event.getPlayer());
		
		//Check if the player is trying to enter a factory
		if(current != null) {

			//Check if player is NOT authorized at this factory
			if(!current.getAuthorized().contains(event.getPlayer().getUniqueId())) {
				
				event.setCancelled(true);
				event.getPlayer().sendMessage("[FF] You are not authorized!");
			}
		}
	}

	public void onTouch(Player player) {
		
		Factory current = this.factories.inRadius(player);
		
		//Check to make sure the player is in a factory
		//Since unauthorized players cannot 
		if(current != null) {
			
			//Check if player is authorized
			if(current.getAuthorized().contains(player.getUniqueId())) {
				
				//Open the GUI if it isn't already open
				if(!current.isGUIOpen()) {

					current.createGUI(player);
				}
				else {
					//Else, GUI is open
					
					current.deleteGUI();
					try {
						current.deleteFactory();
					} catch (MaxChangedBlocksException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					current.setFactoryLocation(null);
				}
			}
		}
	}
}

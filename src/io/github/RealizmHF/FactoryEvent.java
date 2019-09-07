package io.github.RealizmHF;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.MaxChangedBlocksException;


public class FactoryEvent implements Listener, TouchHandler {

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

					//Check if the factory is overlapping another factory
					if(!factories.inRadius(event.getBlock().getLocation())) {
						
						Factory newFactory = new Factory(plugin, event.getPlayer(), event.getBlock().getLocation(), count);
						
						//Add the new factory to the current list of factories
						this.factories.addFactory(newFactory);
						FactoryScheduleManager.add(event.getPlayer(), newFactory, this.plugin.getC().getLong("health timer"), System.currentTimeMillis());
						this.count++;
						
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
					this.factories.getFactories().remove(current);
				}
			}
		}
	}
}

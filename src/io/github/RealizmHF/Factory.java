package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;

public class Factory {
	
	private Main plugin;
	private boolean isBroken = false;
	private boolean openGUI = false;
	private int health;
	private double repairMultiplier;
	private Location factoryLocation;
	private int inventorySize;
	private int factoryRadius;
	private ArrayList<UUID> authorized;
	private int factoryRank;
	private String factoryType;
	private int factoryTier;
	private ItemStack bluePrint;
	private int factoryID;
	private Hologram hologram;
	private Inventory factoryGenerated;
	private Hologram worker;
	private Hologram items;
	private Hologram level;
	private Hologram pickup;
	private Hologram factory;
	private RegionPaster factorySchematic;
	private File fileSchematic;
	
	public Factory(Main plugin, Player p, ItemStack bp, int id) throws IOException {
		this.plugin = plugin;
		
		bluePrint = bp;
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = plugin.getC().getDouble("repair");
		inventorySize = plugin.getC().getInt("inventory size");
		factoryRadius = plugin.getC().getInt("radius");
		factoryLocation = null;
		authorized = new ArrayList<UUID>();
		authorized.add(p.getUniqueId());
		factoryRank = 1;
		factoryTier = 1;
		factoryType = "coal";
		factoryGenerated = Bukkit.createInventory(null, inventorySize, p.getDisplayName() + "'s Factory");
		
		//Add factory to config
		plugin.getCF().createSection(Integer.toString(id));
		plugin.getCF().createSection(Integer.toString(id) + ".authorized");
		plugin.getCF().createSection(Integer.toString(id) + ".authorized." + p.getUniqueId().toString());
		plugin.getCF().addDefault(Integer.toString(id) + ".rank", this.getFactoryRank());
		plugin.getCF().addDefault(Integer.toString(id) + ".tier", this.getFactoryTier());
		plugin.getCF().addDefault(Integer.toString(id) + ".type", this.getFactoryType());
		
		plugin.getCF().save(plugin.getCFFile());
	}
	/*
	 * Creates a Factory of tier 1, rank 1, of type coal
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
		factoryRank = 1;
		factoryTier = 1;
		factoryType = "coal";
		factoryGenerated = Bukkit.createInventory(null, inventorySize, p.getDisplayName() + "'s Factory");
		
		createFactoryName();
		
		//Add factory to config
		plugin.getCF().createSection(Integer.toString(id));
		plugin.getCF().createSection(Integer.toString(id) + ".authorized");
		plugin.getCF().createSection(Integer.toString(id) + ".authorized." + p.getUniqueId().toString());
		plugin.getCF().addDefault(Integer.toString(id) + ".x", l.getX());
		plugin.getCF().addDefault(Integer.toString(id) + ".y", l.getY());
		plugin.getCF().addDefault(Integer.toString(id) + ".z", l.getZ());
		plugin.getCF().addDefault(Integer.toString(id) + ".rank", this.getFactoryRank());
		plugin.getCF().addDefault(Integer.toString(id) + ".tier", this.getFactoryTier());
		plugin.getCF().addDefault(Integer.toString(id) + ".type", this.getFactoryType());
		
		plugin.getCF().save(plugin.getCFFile());
		
		fileSchematic = new File(this.plugin.getDataFolder(), "commonSandDrill.schematic");
		Vector pastePosition = new Vector(this.getFactoryLocation().getBlockX()-2, this.getFactoryLocation().getBlockY()+2, this.getFactoryLocation().getBlockZ()-3);
		World world = this.getFactoryLocation().getWorld();
		EditSessionFactory editSession = WorldEdit.getInstance().getEditSessionFactory();

		factorySchematic = new RegionPaster(editSession);
		
		try {
			factorySchematic.loadArea(world, fileSchematic, pastePosition);
		} catch (MaxChangedBlocksException | DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Creates a Factory with a custom tier & level & type
	 */
	public Factory(Main plugin, Player p, Location l, int id, int tier, int rank, String type) throws IOException {
		this.plugin = plugin;
		
		factoryID = id;
		isBroken = false;
		health = 100;
		repairMultiplier = plugin.getC().getDouble("repair");
		inventorySize = plugin.getC().getInt("inventory size");
		factoryRadius = plugin.getC().getInt("radius");
		factoryLocation = l;
		authorized.add(p.getUniqueId());
		factoryRank = rank;
		factoryTier = tier;
		factoryType = type;
		factoryGenerated = Bukkit.createInventory(null, inventorySize, p.getDisplayName() + "'s Factory");
		factory = HologramsAPI.createHologram(plugin, new Location(l.getWorld(), l.getBlockX(), l.getBlockY()+2, l.getBlockZ()));
		setHologram(factory);
		getHologram().appendTextLine(p.getDisplayName() +  "'s Factory").setTouchHandler(this.plugin.getfEvent());
		
		//Add factory to config
		plugin.getCF().createSection(Integer.toString(id));
		plugin.getCF().createSection(Integer.toString(id) + ".authorized");
		plugin.getCF().createSection(Integer.toString(id) + ".authorized." + p.getUniqueId().toString());
		plugin.getCF().addDefault(Integer.toString(id) + ".x", l.getX());
		plugin.getCF().addDefault(Integer.toString(id) + ".y", l.getY());
		plugin.getCF().addDefault(Integer.toString(id) + ".z", l.getZ());
		plugin.getCF().addDefault(Integer.toString(id) + ".rank", this.getFactoryRank());
		plugin.getCF().addDefault(Integer.toString(id) + ".tier", this.getFactoryTier());
		plugin.getCF().addDefault(Integer.toString(id) + ".type", this.getFactoryType());
		
		plugin.getCF().save(plugin.getCFFile());
		
		fileSchematic = new File(this.plugin.getDataFolder(), "commonSandDrill.schematic");
		Vector pastePosition = new Vector(this.getFactoryLocation().getBlockX(), this.getFactoryLocation().getBlockY(), this.getFactoryLocation().getBlockZ());
		World world = this.getFactoryLocation().getWorld();
		EditSessionFactory editSession = WorldEdit.getInstance().getEditSessionFactory();

		factorySchematic = new RegionPaster(editSession);
		
		try {
			factorySchematic.loadArea(world, fileSchematic, pastePosition);
		} catch (MaxChangedBlocksException | DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 
	 * 
	 * Getters
	 * 
	 * 
	 */
	public String getFactoryType() {
		return this.factoryType;
	}
	public double getRepairMultiplier() {
		return this.repairMultiplier;
	}
	public int getFactoryID() {
		return this.factoryID;
	}
	public boolean isBroke() {
		return this.isBroken;
	}
	public int getHealth() {
		return this.health;
	}
	public Location getFactoryLocation() {
		return this.factoryLocation;
	}
	public ArrayList<UUID> getAuthorized(){
		return this.authorized;
	}
	public int getFactoryRank() {
		return this.factoryRank;
	}
	public int getFactoryRadius() {
		return this.factoryRadius;
	}
	public int getFactoryTier() {
		return this.factoryTier;
	}
	public Hologram getHologram() {
		return hologram;
	}
	public Inventory getFactoryGenerated() {
		return factoryGenerated;
	}
	public boolean isGUIOpen() {
		return this.openGUI;
	}
	public ItemStack getBluePrint() {
		return this.bluePrint;
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
	public void setFactoryRank(int newRank) {
		factoryRank = newRank;
	}
	public void setFactoryRadius(int newRadius) {
		factoryRadius = newRadius;
	}
	public void setHologram(Hologram hologram) {
		this.hologram = hologram;
	}
	public void setFactoryType(String type) {
		this.factoryType = type;
	}
	public void setBluePrint(ItemStack item) {
		this.bluePrint = item;
	}
	/*
	 * 
	 * 
	 * Functions
	 * 
	 * 
	 */
	public void generateItems() {
		
		
		for(int k = 0; k < 1000; k++) {
			
			if(k % 5 == 0) {
				
				Random rand = new Random();
				rand.nextInt(100);
				
				List<String> tempList = this.plugin.getC().getStringList("items." + this.getFactoryTier() + "." + this.getFactoryType());
				
				List<Integer> chances;
				
				for(int j = 0; j < tempList.size(); j++) {
					
					
				}
				//this.factoryGenerated.addItem(new ItemStack(Material.getMaterial(tempItem)));
			}
		}
	}
	/*
	 * 
	 * 
	 */
	public void createGUI(Player player) {
		

		Location loc = this.getFactoryLocation();
		this.openGUI = true;
		
		worker = HologramsAPI.createHologram(plugin, new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY()+2.5, loc.getBlockZ()+2));
		setHologram(worker);
		getHologram().appendItemLine(new ItemStack(Material.DIAMOND_PICKAXE)).setTouchHandler( e -> {
			
			
		});
		
		items = HologramsAPI.createHologram(plugin, new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY()+2.5, loc.getBlockZ()+1));
		setHologram(items);
		getHologram().appendItemLine(new ItemStack(Material.CHEST)).setTouchHandler( e -> {
			
			player.openInventory(this.getFactoryGenerated());
		});

		level = HologramsAPI.createHologram(plugin, new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY()+2.5, loc.getBlockZ()));
		setHologram(level);
		getHologram().appendItemLine(new ItemStack(Material.EXP_BOTTLE)).setTouchHandler( e -> {
			
			
		});

		
		pickup = HologramsAPI.createHologram(plugin, new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY()+2.5, loc.getBlockZ()-1));
		setHologram(pickup);
		getHologram().appendItemLine(new ItemStack(Material.BUCKET)).setTouchHandler(this.plugin.getfEvent());
		
		
		
	}
	public void createFactoryName() {
		//Creates the initial hologram to show the owner's username
		
		factory = HologramsAPI.createHologram(plugin, new Location(this.getFactoryLocation().getWorld(), this.factoryLocation.getBlockX()-1, this.factoryLocation.getBlockY()+3, this.factoryLocation.getBlockZ()+.5));
		setHologram(factory);
		getHologram().appendTextLine(this.plugin.getServer().getOfflinePlayer(this.authorized.get(0)).getPlayer().getDisplayName() +  "'s Factory").setTouchHandler(this.plugin.getfEvent());
	}
	public void setLocationConfigs() {
		//Creates the location configs when placed
		
		plugin.getCF().addDefault(Integer.toString(this.getFactoryID()) + ".x", this.getFactoryLocation().getX());
		plugin.getCF().addDefault(Integer.toString(this.getFactoryID()) + ".y", this.getFactoryLocation().getY());
		plugin.getCF().addDefault(Integer.toString(this.getFactoryID()) + ".z", this.getFactoryLocation().getZ());
	}
	public void deleteGUI() {
		
		openGUI = false;
		items.delete();
		level.delete();
		worker.delete();
		pickup.delete();
		
	}
	public void deleteFactory() throws MaxChangedBlocksException, IOException {
		

		factory.delete();
		Vector pos1 = new Vector(this.getFactoryLocation().getBlockX()-5, this.getFactoryLocation().getBlockY(), this.getFactoryLocation().getBlockZ()-5);
		Vector pos2 = new Vector(this.getFactoryLocation().getBlockX()+5, this.getFactoryLocation().getBlockY()+17, this.getFactoryLocation().getBlockZ()+5);
		CuboidRegion region = new CuboidRegion(pos1, pos2);
		
		World world = this.getFactoryLocation().getWorld();
		
		EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(world), 999999999);
		try {
		    editSession.setBlocks(region, new BaseBlock(BlockID.AIR));
		    
		} catch (MaxChangedBlocksException e) {
		    // As of the blocks are unlimited this should not be called
		}
	}
	public void pasteFactorySchematic() {
		
		fileSchematic = new File(this.plugin.getDataFolder(), "commonSandDrill.schematic");
		Vector pastePosition = new Vector(this.getFactoryLocation().getBlockX(), this.getFactoryLocation().getBlockY()+1, this.getFactoryLocation().getBlockZ());
		World world = this.getFactoryLocation().getWorld();
		EditSessionFactory editSession = WorldEdit.getInstance().getEditSessionFactory();

		factorySchematic = new RegionPaster(editSession);
		
		try {
			factorySchematic.loadArea(world, fileSchematic, pastePosition);
		} catch (MaxChangedBlocksException | DataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

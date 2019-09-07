package io.github.RealizmHF;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class FactoryGenerated implements InventoryProvider {

	
	public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("myInventory")
            .provider(new FactoryGenerated())
            .size(2, 9)
            .type(InventoryType.CHEST)
            .build();
	
	public void init(Player player, InventoryContents factoryItems) {
		
		SmartInventory.builder().title(player.getDisplayName() + "'s Factory");
		
		Pagination pages = factoryItems.pagination();
		
		pages.next().setItems(ClickableItem.empty(new ItemStack(Material.GOLD_AXE)));
		
		pages.addToIterator(factoryItems.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
		
		factoryItems.set(1, 3, ClickableItem.of(new ItemStack(Material.DIAMOND),
	            e -> INVENTORY.open(player, pages.next().getPage())));
		factoryItems.set(1, 5, ClickableItem.of(new ItemStack(Material.EXP_BOTTLE), 
				e -> INVENTORY.open(player, pages.page(2).next().getPage())));
	}

	public void update(Player arg0, InventoryContents arg1) {
		// TODO Auto-generated method stub
		
	}

}

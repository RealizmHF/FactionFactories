package io.github.RealizmHF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FactoryCommands implements CommandExecutor {
	
	private Main plugin;
	private FactoryManager factories = FactoryManager.fManager;
	int count = 0;
	
	public FactoryCommands(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player = (Player) sender;
		
		//Command is a Factory command
		if(label.equalsIgnoreCase("factory")) {

			//Player is OP
			if(sender.isOp()) {
				
				if(args.length == 1) {
					
					if(args[0].equalsIgnoreCase("create")) {
						
						if(player.getItemInHand().getAmount() == 1) {

							ItemStack item = player.getItemInHand();
							ItemMeta itemMeta = item.getItemMeta();
							itemMeta.addEnchant(new makeBluePrint(count), 70, true);
							itemMeta.setDisplayName("Common Coal Factory");
							List<String> list = new ArrayList<String>();
							list.add("Rank: 1");
							list.add("ID: " + count);
							itemMeta.setLore(list);
							item.setItemMeta(itemMeta);
							
							try {
								this.factories.addFactory(new Factory(plugin, player, item, count));
								this.factories.addBluePrint(item);
								count++;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else {
							player.sendMessage("[FF] You can only create 1 blue print at a time!");
						}
					}
					else {
						player.sendMessage("/factory create  - Creates a blue print from whatever item you're holding");
					}
				}
				else if(args.length == 3) {
					//  - /factory authorize <id> <player_name>
					
					if(args[0].equalsIgnoreCase("authorize")) {
						
						int id = Integer.parseInt(args[1]);
						Player authPlayer = this.plugin.getServer().getPlayer(args[2]);
						
						if(authPlayer != null) {

							if(this.factories.getFactories().size() > 0) {
								
								for(Factory current : this.factories.getFactories()) {
									
									if(current.getFactoryID() == id) {
										
										current.getAuthorized().add(authPlayer.getUniqueId());
									}
								}
							}
						}
						else {
							player.sendMessage("[FF] Player " + args[2] + " does not exist or is offline!");
						}
					}
					else {
						player.sendMessage("[FF] Command " + args[0] + " does not exist!");
					}
				}
				else {
					player.sendMessage("/factory create  - Creates a blue print from whatever item you're holding");
				}
			}
		}
		return false;
	}

}

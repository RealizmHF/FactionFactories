package io.github.RealizmHF;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactoryScheduleManager {
 
    public static HashMap<UUID, FactorySchedule> cooldown = new HashMap<UUID, FactorySchedule>();
 
    public static void add(Player player, Factory factory, long seconds, long systime) {
    	
        if(!cooldown.containsKey(player.getUniqueId()))
        	cooldown.put(player.getUniqueId(), new FactorySchedule(player));
        if(isCooling(player, factory))
        	return;
        
        cooldown.get(player.getUniqueId()).healthTimer.put(factory.getFactoryID(), new FactorySchedule(factory, player, seconds, System.currentTimeMillis()));
    }
 
    public static boolean isCooling(Player player, Factory factory) {
        if(!cooldown.containsKey(player.getUniqueId())) return false;
        if(!cooldown.get(player.getUniqueId()).healthTimer.containsKey(factory.getFactoryID())) return false;
        return true;
        }
 
 
    public static void removeCooldown(UUID key, int factoryID) {
        if(!cooldown.containsKey(key)) {
            return;
        }
        if(!cooldown.get(key).healthTimer.containsKey(factoryID)) {
            return;
        }
        Factory temp = cooldown.get(key).healthTimer.remove(factoryID).factory;
        
        Player cPlayer = Bukkit.getPlayer(key);
        if(key != null) {
            cPlayer.sendMessage(ChatColor.GRAY + "Factory ID Removed: " + ChatColor.AQUA + factoryID);
        }
        if(temp.isBroke()) {
        	cPlayer.sendMessage("Factory is Broke");
        }
    }
 
    public static void handleSchedules() {
        if(cooldown.isEmpty()) {
            return;
        }
        for(Iterator<UUID> it = cooldown.keySet().iterator(); it.hasNext();) {
            UUID key = it.next();
            for(Iterator<Integer> iter = cooldown.get(key).healthTimer.keySet().iterator(); iter.hasNext();) {
                int currentID = iter.next();
                
                if(cooldown.get(key).healthTimer.get(currentID).factory.getHealth() > 0) {
                	cooldown.get(key).healthTimer.get(currentID).factory.setHeatlh(cooldown.get(key).healthTimer.get(currentID).factory.getHealth()-1);
                	cooldown.get(key).healthTimer.get(currentID).factory.generateItems();
                }
                else {
                	cooldown.get(key).healthTimer.get(currentID).factory.setBroke();
                	removeCooldown(key, currentID);
                }
            }
        }
    }
}

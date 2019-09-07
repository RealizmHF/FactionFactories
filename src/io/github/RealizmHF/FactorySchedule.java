package io.github.RealizmHF;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class FactorySchedule {

	public HashMap<Integer, FactorySchedule> healthTimer = new HashMap<Integer, FactorySchedule>();
	
	public Factory factory;
	public UUID ownerUUID;
	public long delay;
	public long sysTime;
	
	public FactorySchedule(Factory factory, Player player, long delayTime, long systemTime) {
		this.factory = factory;
		this.ownerUUID = player.getUniqueId();
		this.delay = delayTime;
		this.sysTime = systemTime;
	}
	
	public FactorySchedule(Player player) {
		this.ownerUUID = player.getUniqueId();
	}

}

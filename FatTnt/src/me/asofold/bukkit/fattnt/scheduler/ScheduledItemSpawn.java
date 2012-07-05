package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public final class ScheduledItemSpawn implements ScheduledEntry {
	
	public final long ts;
	public final World world;
	public final double x;
	public final double y;
	public final double z;

	private final int bX;
	private final int bZ;
	
	public final ItemStack stack;
	
	public ScheduledItemSpawn(World world, double x, double y, double z,  ItemStack stack){
		ts = System.currentTimeMillis();
		this.world = world;
		this.x = x;
		this.bX = Utils.floor(x);
		this.y = y;
		this.z = z;
		this.bZ = Utils.floor(z);
		this.stack = stack.clone();
	}

	@Override
	public long getCreationTime() {
		return ts;
	}

	@Override
	public int getBlockX() {
		return bX;
	}

	@Override
	public int getBlockZ() {
		return bZ;
	}

}

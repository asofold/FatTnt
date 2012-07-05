package me.asofold.bukkit.fattnt.scheduler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class ScheduledItemSpawn extends ScheduledLocation {
	
	private final ItemStack stack;
	private final Vector velocity;
	
	/**
	 * Convenience method to set to block middle.
	 * @param block
	 * @param stack
	 */
	public ScheduledItemSpawn(Block block, ItemStack stack){
		this(block.getWorld(), 0.5 + (double)block.getX(), 0.5 + (double)block.getY(), 0.5 + (double)block.getZ(), stack, null);
	}
	
	public ScheduledItemSpawn(Location Location, ItemStack stack, Vector velocity){
		super(Location);
		this.stack = stack.clone();
		this.velocity = velocity;
	}
	
	public ScheduledItemSpawn(World world, double x, double y, double z,  ItemStack stack, Vector velocity){
		super(world, x, y, z);
		this.stack = stack.clone();
		this.velocity = velocity;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public ItemStack getStack() {
		return stack;
	}

}

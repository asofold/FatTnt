package me.asofold.bukkit.fattnt.scheduler;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class ScheduledItemSpawn extends ScheduledLocation {
	
	private final ItemStack stack;
	private final Vector velocity;
	
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

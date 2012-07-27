package me.asofold.bpl.fattnt.scheduler;

import me.asofold.bpl.fattnt.effects.ExplosionManager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class ScheduledItemSpawn extends ScheduledEntitySpawn{
	
	private final ItemStack stack;
	
	/**
	 * Convenience method to set to block middle.
	 * @param block
	 * @param stack
	 */
	public ScheduledItemSpawn(Block block, ItemStack stack){
		this(block.getWorld(), 0.5 + (double)block.getX(), 0.5 + (double)block.getY(), 0.5 + (double)block.getZ(), stack, null);
	}
	
	public ScheduledItemSpawn(Location Location, ItemStack stack, Vector velocity){
		super(Location, velocity);
		this.stack = stack;
	}
	
	public ScheduledItemSpawn(World world, double x, double y, double z,  ItemStack stack, Vector velocity){
		super(world, x, y, z, velocity);
		this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Entity spawn() {
		return ExplosionManager.spawnItem(this);
	}

}

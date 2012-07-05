package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.Location;
import org.bukkit.World;

public class ScheduledLocation implements ScheduledEntry {

	public final long ts;
	
	public final World world;
	public final double x;
	public final double y;
	public final double z;

	protected final int bX;
	protected final int bZ;
	
	public ScheduledLocation(final Location location){
		this(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}
	
	public ScheduledLocation(final World world, final double x, final double y, final double z){
		ts = System.currentTimeMillis();
		this.world = world;
		this.x = x;
		this.bX = Utils.floor(x);
		this.y = y;
		this.z = z;
		this.bZ = Utils.floor(z);
	}
	
	@Override
	public final long getCreationTime() {
		return ts;
	}

	@Override
	public final int getBlockX() {
		return bX;
	}

	@Override
	public final int getBlockZ() {
		return bZ;
	}
	
	public final Location getLocation(){
		return new Location(world, x, y, z);
	}

}

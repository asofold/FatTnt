package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.World;
import org.bukkit.util.Vector;

public class ScheduledTntSpawn implements ScheduledEntry {

	public final long ts;
	public final World world;
	public final double x;
	public final double y;
	public final double z;

	private final int bX;
	private final int bZ;
	private final int fuseTicks;
	private final Vector velocity;
	

	
	public ScheduledTntSpawn(World world, double x, double y, double z, int fuseTicks, Vector velocity){
		ts = System.currentTimeMillis();
		this.world = world;
		this.x = x;
		this.bX = Utils.floor(x);
		this.y = y;
		this.z = z;
		this.bZ = Utils.floor(z);
		this.fuseTicks = fuseTicks;
		this.velocity = velocity;
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

	public Vector getVelocity() {
		return velocity;
	}

	public int getFuseTicks() {
		return fuseTicks;
	}

}

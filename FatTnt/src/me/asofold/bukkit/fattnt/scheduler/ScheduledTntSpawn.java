package me.asofold.bukkit.fattnt.scheduler;

import org.bukkit.World;
import org.bukkit.util.Vector;

public class ScheduledTntSpawn extends ScheduledLocation {

	private final int fuseTicks;
	private final Vector velocity;
	
	public ScheduledTntSpawn(World world, double x, double y, double z, int fuseTicks, Vector velocity){
		super(world, x, y, z);
		this.fuseTicks = fuseTicks;
		this.velocity = velocity;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public int getFuseTicks() {
		return fuseTicks;
	}

}

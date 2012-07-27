package me.asofold.bpl.fattnt.scheduler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public abstract class ScheduledEntitySpawn extends ScheduledLocation {
	
	protected final Vector velocity;
	
	public ScheduledEntitySpawn(World world, double x, double y, double z, Vector velocity) {
		super(world, x, y, z);
		this.velocity = velocity;
	}
	
	public ScheduledEntitySpawn(Location location, Vector velocity) {
		super(location);
		this.velocity = velocity;
	}

	public abstract Entity spawn();

	public Vector getVelocity() {
		return velocity;
	}

}

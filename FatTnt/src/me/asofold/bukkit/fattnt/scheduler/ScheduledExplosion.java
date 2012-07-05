package me.asofold.bukkit.fattnt.scheduler;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Just store settings to let the explosion go off at any point of time.<br>
 * Creates a timestamp on creation, which might lead to expiration.
 * @author mc_dev
 *
 */
public final class ScheduledExplosion {
	public final World world;
	public final double x;
	public final double y;
	public final double z;
	public final float radius;
	public final boolean fire;
	public final Entity explEntity;
	public final EntityType entityType;
	final long ts;
	
	public ScheduledExplosion(World world, double x, double y, double z, float radius, boolean fire, Entity explEntity, EntityType entityType){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius =  radius;
		this.fire = fire;
		this.explEntity = explEntity;
		this.entityType = entityType;
		ts = System.currentTimeMillis();
	}
}

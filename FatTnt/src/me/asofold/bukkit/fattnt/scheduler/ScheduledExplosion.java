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
public final class ScheduledExplosion extends ScheduledEntry{
	public final World world;
	public final double y;
	public final float radius;
	public final boolean fire;
	public final Entity explEntity;
	public final EntityType entityType;
	
	public ScheduledExplosion(World world, double x, double y, double z, float radius, boolean fire, Entity explEntity, EntityType entityType){
		super(x, z);
		this.world = world;
		this.y = y;
		this.radius =  radius;
		this.fire = fire;
		this.explEntity = explEntity;
		this.entityType = entityType;
	}
}

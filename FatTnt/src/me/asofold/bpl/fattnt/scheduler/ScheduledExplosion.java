package me.asofold.bpl.fattnt.scheduler;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Just store settings to let the explosion go off at any point of time.<br>
 * Creates a timestamp on creation, which might lead to expiration.
 * @author mc_dev
 *
 */
public final class ScheduledExplosion extends ScheduledLocation{
	
	private final float radius;
	private final boolean fire;
	private final Entity explEntity;
	private final EntityType entityType;

	
	public ScheduledExplosion(World world, double x, double y, double z, float radius, boolean fire, Entity explEntity, EntityType entityType){
		super(world, x, y, z);
		this.radius =  radius;
		this.fire = fire;
		this.explEntity = explEntity;
		this.entityType = entityType;
	}


	public float getRadius() {
		return radius;
	}


	public boolean isFire() {
		return fire;
	}


	public Entity getExplEntity() {
		return explEntity;
	}


	public EntityType getEntityType() {
		return entityType;
	}

}

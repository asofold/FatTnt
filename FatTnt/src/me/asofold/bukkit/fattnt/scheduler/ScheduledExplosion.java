package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Just store settings to let the explosion go off at any point of time.<br>
 * Creates a timestamp on creation, which might lead to expiration.
 * @author mc_dev
 *
 */
public final class ScheduledExplosion implements ScheduledEntry{
	public final long ts;
	public final World world;
	public final double x;
	public final double y;
	public final double z;
	public final float radius;
	public final boolean fire;
	public final Entity explEntity;
	public final EntityType entityType;
	private final int bX;
	private final int bZ;
	
	public ScheduledExplosion(World world, double x, double y, double z, float radius, boolean fire, Entity explEntity, EntityType entityType){
		ts = System.currentTimeMillis();
		this.world = world;
		this.x = x;
		this.bX = Utils.floor(x);
		this.y = y;
		this.z = z;
		this.bZ = Utils.floor(z);
		this.radius =  radius;
		this.fire = fire;
		this.explEntity = explEntity;
		this.entityType = entityType;
	}

	@Override
	public long getExpirationTime() {
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
}

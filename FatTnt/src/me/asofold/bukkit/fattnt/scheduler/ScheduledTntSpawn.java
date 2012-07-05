package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.effects.ExplosionManager;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ScheduledTntSpawn extends ScheduledEntitySpawn {

	private final int fuseTicks;
	
	public ScheduledTntSpawn(World world, double x, double y, double z, int fuseTicks, Vector velocity){
		super(world, x, y, z, velocity);
		this.fuseTicks = fuseTicks;
	}

	public int getFuseTicks() {
		return fuseTicks;
	}

	@Override
	public Entity spawn() {
		return ExplosionManager.addTntPrimed(this);
	}

}

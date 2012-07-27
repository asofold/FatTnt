package me.asofold.bpl.fattnt.scheduler;

import me.asofold.bpl.fattnt.effects.ExplosionManager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ScheduledArrowSpawn extends ScheduledEntitySpawn {

	public ScheduledArrowSpawn(Location location, Vector velocity) {
		super(location, velocity);
	}

	@Override
	public Entity spawn() {
		return ExplosionManager.spawnArrow(world, getLocation(), velocity);
	}

}

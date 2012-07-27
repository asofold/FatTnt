package me.asofold.bpl.fattnt.events;

import java.util.List;

import me.asofold.bpl.fattnt.effects.FatExplosionSpecs;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FatEntityExplodeEvent extends EntityExplodeEvent implements FatExplodeEvent{

	private final FatExplosionSpecs specs;

	public FatEntityExplodeEvent(Entity what, Location location,
			List<Block> blocks, float yield, FatExplosionSpecs specs) {
		super(what, location, blocks, yield);
		this.specs = specs;
	}

	@Override
	public FatExplosionSpecs getExplosionSpecs() {
		return specs;
	}

}

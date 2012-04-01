package me.asofold.bukkit.fattnt.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FatEntityExplodeEvent extends EntityExplodeEvent {

	public FatEntityExplodeEvent(Entity what, Location location,
			List<Block> blocks, float yield) {
		super(what, location, blocks, yield);
	}

}

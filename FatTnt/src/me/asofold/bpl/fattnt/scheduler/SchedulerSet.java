package me.asofold.bpl.fattnt.scheduler;

import me.asofold.bpl.fattnt.config.Path;
import me.asofold.bpl.fattnt.config.compatlayer.CompatConfig;

public class SchedulerSet{
	
	/**
	 * Scheduled explosions (including getBlocks, event, applyBlockSettings etc.)
	 */
	public final ChunkWiseScheduler<ScheduledExplosion> explosions = new ChunkWiseScheduler<ScheduledExplosion>();
	
	/**
	 * TODO: Maybe refactor this one to entities ...
	 */
	public final ChunkWiseScheduler<ScheduledEntitySpawn> spawnEntities = new ChunkWiseScheduler<ScheduledEntitySpawn>();
	
	public final ChunkWiseScheduler<ScheduledItemSpawn> spawnItems = new ChunkWiseScheduler<ScheduledItemSpawn>();
	
	public void clear(){
		explosions.clear();
		spawnEntities.clear();
		spawnItems.clear();
	}

	public boolean hasEntries() {
		return explosions.hasEntries() || spawnEntities.hasEntries() || spawnItems.hasEntries();
	}

	public void fromConfig(CompatConfig cfg) {
		explosions.fromConfig(cfg, Path.schedExplosions + Path.sep);
		spawnEntities.fromConfig(cfg, Path.schedEntities + Path.sep);
		spawnItems.fromConfig(cfg, Path.schedItems + Path.sep);
	}
}

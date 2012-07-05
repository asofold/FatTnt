package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.config.Path;
import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;

public class SchedulerSet{
	
	/**
	 * Scheduled explosions (including getBlocks, event, applyBlockSettings etc.)
	 */
	public final ChunkWiseScheduler<ScheduledExplosion> explosions = new ChunkWiseScheduler<ScheduledExplosion>();
	
	/**
	 * TODO: Maybe refactor this one to entities ...
	 */
	public final ChunkWiseScheduler<ScheduledTntSpawn> spawnTnt = new ChunkWiseScheduler<ScheduledTntSpawn>();
	
	public final ChunkWiseScheduler<ScheduledItemSpawn> spawnItems = new ChunkWiseScheduler<ScheduledItemSpawn>();
	
	public void clear(){
		explosions.clear();
		spawnTnt.clear();
		spawnItems.clear();
	}

	public boolean hasEntries() {
		return explosions.hasEntries() || spawnTnt.hasEntries() || spawnItems.hasEntries();
	}

	public void fromConfig(CompatConfig cfg) {
		explosions.fromConfig(cfg, Path.schedExplosions + Path.sep);
		spawnTnt.fromConfig(cfg, Path.schedTnt + Path.sep);
		spawnItems.fromConfig(cfg, Path.schedItems + Path.sep);
	}
}

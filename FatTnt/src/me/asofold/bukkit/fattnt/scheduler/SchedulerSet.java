package me.asofold.bukkit.fattnt.scheduler;

import me.asofold.bukkit.fattnt.config.Path;
import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;

public class SchedulerSet {
	
	public final ChunkWiseScheduler<ScheduledExplosion> explosions = new ChunkWiseScheduler<ScheduledExplosion>();
	
	public final ChunkWiseScheduler<ScheduledTntSpawn> spawnTnt = new ChunkWiseScheduler<ScheduledTntSpawn>();
	
	public void clear(){
		explosions.clear();
		spawnTnt.clear();
	}

	public boolean hasEntries() {
		return explosions.hasEntries() || spawnTnt.hasEntries();
	}

	public void fromConfig(CompatConfig cfg) {
		explosions.fromConfig(cfg, Path.schedExplosions + Path.sep);
		spawnTnt.fromConfig(cfg, Path.schedTnt + Path.sep);
	}
}

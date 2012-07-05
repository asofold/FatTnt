package me.asofold.bukkit.fattnt.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.stats.Stats;

import org.bukkit.entity.EntityType;

/**
 * Settings for FatTnt.
 * 
 * See class Defaults for default settings and application of those.
 * @author mc_dev
 *
 */
public class Settings {
	public final Stats stats;
	
	// World dependent settings:
	
	/**
	 * Default settings for all worlds.
	 */
	WorldSettings defaultWorldSettings = new WorldSettings();
	
	/**
	 * World name (lower-case) to WorldSettings.
	 */
	Map<String, WorldSettings> worldSettings = new HashMap<String, WorldSettings>();
	
	private float maxRadius = 0;

	/**
	 * NOTES:<br>
	 * - Constructor does not initialize arrays !<br>
	 * - Before using applyConfig you need to add defaults to ensure all paths are there. 
	 * @param stats Are passed with settings, currently, to use the same stats object.
	 */
	public Settings(Stats stats){
		this.stats = stats;
	}
	
	public void applyConfig(CompatConfig cfg){
		// world settings:
		defaultWorldSettings = new WorldSettings();
		defaultWorldSettings.fromConfig(cfg, "");
		worldSettings.clear();
		List<String> worlds = cfg.getStringKeys(Path.worldSettings);
		for (String world : worlds){
			WorldSettings ws = new WorldSettings();
			ws.fromConfig(cfg, Path.worldSettings + Path.sep + world + Path.sep);
			if (ws.hasValues()) worldSettings.put(world.trim().toLowerCase(), ws);
		}
		
		setMaxRadius();
	}

	/**
	 * Set handleExplosions for default explosion settings.
	 * @param handle
	 */
	public void setHandleExplosions(boolean handle){
		defaultWorldSettings.setHandleExplosions(handle);
	}
	
	/**
	 * Usually not needed to call after applyConfig was called.
	 */
	public void setMaxRadius() {
		maxRadius = defaultWorldSettings.getMaxRadius();
		for (WorldSettings ws : worldSettings.values()){
			maxRadius = Math.max(maxRadius, ws.getMaxRadius());
		}
	}
	
	/**
	 * Get maximal maxRadius of all possible settings. 
	 * @return
	 */
	public float getMaxRadius(){
		return maxRadius;
	}
	
	public ExplosionSettings getApplicableExplosionSettings(EntityType explodingEntity, String worldName){
		// TODO: check if in cache map
		ExplosionSettings out = new ExplosionSettings(Integer.MIN_VALUE);
		out.applySettings(defaultWorldSettings.getApplicableExplosionSettings(explodingEntity));
		WorldSettings ref = worldSettings.get(worldName.trim().toLowerCase());
		if (ref != null){
			out.applySettings(ref.getApplicableExplosionSettings(explodingEntity));
		}
		// TODO: maybe ensure some defaults here ?
		// TODO: put to cache map !
		return out;
	}

}

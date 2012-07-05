package me.asofold.bukkit.fattnt.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.config.priorityvalues.PrioritySettings;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;

/**
 * World dependent settings.<br>
 * PrioritySettings: Methods will not update sub objects like ConfinementSettings.
 * @author mc_dev
 *
 */
public class WorldSettings extends PrioritySettings {

	public int priority = 0;
	
	public ExplosionSettings explosion = new ExplosionSettings(0);
	
	public Map <EntityType, ExplodingEntitySettings> entities = new HashMap<EntityType, ExplodingEntitySettings>();
	
	public WorldSettings(){
	}
	
	public WorldSettings(int priority){
		this.priority = priority;
		setPriority(priority);
	}
	
	public void fromConfig(CompatConfig cfg, String prefix){
		priority = 0;
		if (cfg.contains(prefix + Path.priority)) priority  = cfg.getInt(prefix + Path.priority, (int) 0);
		resetAllValues(priority);
		
		// ExplosionSettings:
		explosion = new ExplosionSettings(priority);
		explosion.applyConfig(cfg, prefix, priority);
		
		// Entity settings:
		for (String key : cfg.getStringKeys(prefix + Path.explodingEntities)){
			EntityType type;
			try{
				type = EntityType.valueOf(key.trim().toUpperCase().replace("-", "_"));
			}
			catch (Throwable t){
				Bukkit.getLogger().warning("[FatTnt] Bad entity type ("+key+") at: " + prefix + Path.explodingEntities);
				continue;
			}
			ExplodingEntitySettings ees = new ExplodingEntitySettings(priority);
			ees.applyConfig(cfg, prefix + Path.explodingEntities + Path.sep + key);
			entities.put(type, ees);
		}
	}
	
	public void toConfig(Configuration cfg, String prefix){
		if (priority != 0) cfg.set(prefix + Path.confine, priority);
		
		explosion.toConfig(cfg, prefix);
		
		for (Entry<EntityType, ExplodingEntitySettings> entry : entities.entrySet()){
			entry.getValue().toConfig(cfg, prefix + Path.explodingEntities + Path.sep + entry.getKey().toString() + Path.sep);
		}
		
	}

	public void setHandleExplosions(boolean handle) {
		// TODO Auto-generated method stub
		
	}

	public float getMaxRadius(){
		float maxRadius = explosion.maxRadius;
		for (ExplodingEntitySettings ees : entities.values()){
			maxRadius = Math.max(maxRadius, ees.explosionSettings.maxRadius);
		}
		return maxRadius;
	}

	public ExplosionSettings getApplicableExplosionSettings(EntityType explodingEntity) {
		// TODO Auto-generated method stub
		return explosion;
	}
	
}

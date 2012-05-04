package me.asofold.bukkit.fattnt.config;

import me.asofold.bukkit.fattnt.config.priorityvalues.PrioritySettings;

import org.bukkit.configuration.Configuration;

/**
 * World dependent settings.<br>
 * PrioritySettings: Methods will not update sub objects like ConfinementSettings.
 * @author mc_dev
 *
 */
public class WorldSettings extends PrioritySettings {

	int priority = 0;
	ConfinementSettings confine = new ConfinementSettings();
	
	public WorldSettings(){
	}
	
	public WorldSettings(int priority){
		this.priority = priority;
		setPriority(priority);
	}
	
	public void fromConfig(Configuration cfg, String prefix){
		priority = 0;
		if (cfg.contains(prefix + Path.priority)) priority  = cfg.getInt(prefix + Path.priority, (int) 0);
		resetAllValues(priority);
		
		// confinement settings:
		confine = new ConfinementSettings(priority);
		confine.fromConfig(cfg, prefix);
	}
	
	public void toConfig(Configuration cfg, String prefix){
		if (priority != 0) cfg.set(prefix + Path.confine, priority);
		confine.toConfig(cfg, prefix);
	}
	
}

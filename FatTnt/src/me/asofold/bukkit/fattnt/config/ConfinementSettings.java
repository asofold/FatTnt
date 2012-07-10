package me.asofold.bukkit.fattnt.config;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.config.priorityvalues.OverridePolicy;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityBoolean;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityNumber;
import me.asofold.bukkit.fattnt.config.priorityvalues.PrioritySettings;

import org.bukkit.configuration.Configuration;

/**
 * Confine explosions to a part of the map (basically y-dependent).
 * @author mc_dev
 *
 */
public class ConfinementSettings extends PrioritySettings{
	public int priority = 0;
	public final PriorityBoolean enabled = (PriorityBoolean) addValue("enabled", new PriorityBoolean(null, priority, OverridePolicy.OVERRIDE));
	public final PriorityNumber yMax = (PriorityNumber) addValue("yMax", new PriorityNumber(null, priority, OverridePolicy.OVERRIDE));
	public final PriorityNumber yMin = (PriorityNumber) addValue("yMin", new PriorityNumber(null, priority, OverridePolicy.OVERRIDE));
	
	public ConfinementSettings(int priority){
		this.priority = priority;
		setPriority(priority);
	}
	
	public void applyConfig(CompatConfig cfg, String prefix){
		String p = prefix + Path.confinePriority;
		priority = 0;
		if (cfg.contains(p)){
			Integer temp = cfg.getInt(p, (Integer) null);
			if (temp != null) priority = temp;
		}
		resetAllValues(priority);
		p = prefix + Path.confineEnabled;
		if (cfg.contains(p)){
			Boolean temp = cfg.getBoolean(p);
			if (temp != null) enabled.value = temp;
		}
		p = prefix + Path.confineYMin;
		if (cfg.contains(p)){
			Integer temp = cfg.getInt(p, (Integer) null);
			if (temp != null) yMin.value = temp;
		}
		p = prefix + Path.confineYMax;
		if (cfg.contains(p)){
			Integer temp = cfg.getInt(p, (Integer) null);
			if (temp != null) yMax.value = temp;
		}
	}
	
	/**
	 * This will not overwrite values, if value is null.
	 * @param cfg
	 * @param prefix
	 */
	public void toConfig(Configuration cfg, String prefix){
		if (priority != 0) cfg.set(prefix + Path.confinePriority, priority);
		if (enabled.value != null) cfg.set(prefix + Path.confineEnabled, enabled.value);
		if (yMin.value != null) cfg.set(prefix + Path.confineYMin, yMin.value);
		if (yMax.value != null) cfg.set(prefix + Path.confineYMax, yMax.value);
	}
}

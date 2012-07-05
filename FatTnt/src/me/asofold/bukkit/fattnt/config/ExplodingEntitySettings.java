package me.asofold.bukkit.fattnt.config;

import org.bukkit.configuration.Configuration;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;

/**
 * Settings that apply for certain entities that are exploding, can be part of WorldSettings or default.
 * @author mc_dev
 *
 */
public class ExplodingEntitySettings {
	/**
	 * These contain handleExplosions.
	 */
	public ExplosionSettings explosionSettings = new ExplosionSettings(0);
	private int priority;
	
	public ExplodingEntitySettings(int priority) {
		this.priority = priority;
	}
	public void applyConfig(CompatConfig cfg, String prefix){
		// TODO: maybe read priority
		explosionSettings.applyConfig(cfg, prefix, priority);
	}
	public void toConfig(Configuration cfg, String prefix) {
		explosionSettings.toConfig(cfg, prefix);
	}
}

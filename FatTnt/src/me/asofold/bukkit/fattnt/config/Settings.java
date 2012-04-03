package me.asofold.bukkit.fattnt.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import me.asofold.bukkit.fattnt.stats.Stats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
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
	/**
	 * Handle and alter explosions
	 */
	public boolean handleExplosions = Defaults.handleExplosions;
	
	/**
	 * Defaults to empty !
	 */
	public final Set<EntityType> handledEntities = new HashSet<EntityType>();
	
	public float maxRadius = Defaults.maxRadius;
	
	public float radiusMultiplier = Defaults.radiusMultiplier;
	
	public float damageMultiplier = Defaults.damageMultiplier;
	
	public float maxPathMultiplier = Defaults.maxPathMultiplier;
	
	public float defaultResistance = Defaults.defaultResistance;
	
	/**
	 * Set automatically according to settings;
	 */
	public float minResistance = 0.0f;
	
	public float fStraight = Defaults.fStraight;
	
	public boolean invertIgnored = Defaults.invertIgnored;
	
	public float randDec = Defaults.randDec;
	
	public boolean itemTnt = Defaults.itemTnt;
	/**
	 * If to not apply damage to primed tnt.
	 */
	public boolean sparePrimed = Defaults.sparePrimed;
	
	public double thresholdTntDirect = Defaults.thresholdTntDirect;
	
	public boolean velUse = Defaults.velUse;
	public float velMin = Defaults.velMin;
	public float velCen = Defaults.velCen;
	public float velRan = Defaults.velRan;
	public boolean velOnPrime = Defaults.velOnPrime;
	public float velCap = Defaults.velCap;
	
	public int maxItems = Defaults.maxItems;

	public  float yield = Defaults.yield;
	public  float entityYield = Defaults.entityYield;
	
	public boolean itemArrows = Defaults.itemArrows;
	public boolean projectiles = Defaults.projectiles;
	
	public boolean[] ignore = new boolean[Defaults.blockArraySize];
	public float[] resistance = new float[Defaults.blockArraySize];
	
	public int minPrime = Defaults.minPrime;
	public int maxPrime = Defaults.maxPrime;
	public boolean stepPhysics = Defaults.stepPhysics;
	
	public float projectileMultiplier = Defaults.projectileMultiplier;
	
	public Settings(Stats stats){
		this.stats = stats;
	}
	
	public void applyConfig(Configuration cfg){
		minResistance = Float.MAX_VALUE;
		handledEntities.clear();
		for ( String n : cfg.getStringList(Defaults.cfgEntities)){
			try{
				EntityType etp = EntityType.valueOf(n.toUpperCase());
				if ( etp == null) throw new IllegalArgumentException();
				handledEntities.add(etp);
			} catch (Throwable t){
				Bukkit.getServer().getLogger().warning(Defaults.msgPrefix+"Bad entity: "+n);
			}
		}
		radiusMultiplier = (float) cfg.getDouble(Defaults.cfgMultRadius);
		damageMultiplier = (float) cfg.getDouble(Defaults.cfgMultDamage);
		maxPathMultiplier = (float) cfg.getDouble(Defaults.cfgMultMaxPath);
		invertIgnored = cfg.getBoolean(Defaults.cfgInvertIgnored);
		defaultResistance = (float) cfg.getDouble(Defaults.cfgDefaultResistence);
		minResistance = Math.min(minResistance, defaultResistance);
		maxRadius = (float) cfg.getDouble(Defaults.cfgMaxRadius);
		randDec = (float) cfg.getDouble(Defaults.cfgRandRadius);
		yield = (float) cfg.getDouble(Defaults.cfgYield);
		velUse = cfg.getBoolean(Defaults.cfgVelUse);
		velMin = (float) cfg.getDouble(Defaults.cfgVelMin);
		velCen = (float) cfg.getDouble(Defaults.cfgVelCen);
		velRan = (float) cfg.getDouble(Defaults.cfgVelRan);
		fStraight = (float) cfg.getDouble(Defaults.cfgFStraight);
		velOnPrime = cfg.getBoolean(Defaults.cfgVelOnPrime);
		thresholdTntDirect = cfg.getDouble(Defaults.cfgThresholdTntDirect);
		velCap = (float) cfg.getDouble(Defaults.cfgVelCap);
		itemTnt = cfg.getBoolean(Defaults.cfgItemTnt);
		entityYield = (float) cfg.getDouble(Defaults.cfgEntityYield);
		maxItems = cfg.getInt(Defaults.cfgMaxItems);
		itemArrows = cfg.getBoolean(Defaults.cfgItemArrows);
		projectiles = cfg.getBoolean(Defaults.cfgProjectiles);
		minPrime = cfg.getInt(Defaults.cfgMinPrime);
		maxPrime = cfg.getInt(Defaults.cfgMaxPrime);
		stepPhysics = cfg.getBoolean(Defaults.cfgStepPhysics);
		projectileMultiplier = (float) cfg.getDouble(Defaults.cfgMultProjectiles);
		
		if ( maxRadius > Defaults.radiusLock) maxRadius = Defaults.radiusLock; // safety check
		
		initBlockIds();
		for (Integer i : Defaults.getIdList(cfg, Defaults.cfgIgnore)){
			ignore[i] = !invertIgnored;
		}
		ConfigurationSection sec = cfg.getConfigurationSection(Defaults.cfgResistence);
		Collection<String> keys = sec.getKeys(false);
		if ( keys != null){
			for (String key : keys){
				if ( "default".equalsIgnoreCase(key)) continue;
				float val = (float) cfg.getDouble(Defaults.cfgResistence+"."+key+".value", 1.0);
				minResistance = Math.min(minResistance, val);
				for ( Integer i : Defaults.getIdList(cfg, Defaults.cfgResistence+"."+key+".ids")){
					resistance[i] = val;
				}
			}
		}
	}
	
	private void initBlockIds() {
		for (int i = 0;i<ignore.length;i++){
			ignore[i] = invertIgnored;
			resistance[i] = defaultResistance;
		}
	}
	
	public void setHandleExplosions(boolean handle){
		handleExplosions = handle;
		// TODO: maybe save to some configuration file ?
	}
	
}

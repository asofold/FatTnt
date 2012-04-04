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
	 * Defaults to empty !
	 */
	public final Set<EntityType> handledEntities = new HashSet<EntityType>();
	
	/**
	 * Handle and alter explosions
	 * TODO: also put to config.
	 */
	public boolean handleExplosions = true;
	
	/**
	 * Explosion strength is cut off there.
	 */
	public float maxRadius = 20.0f;
	
	/**
	 * Multiplier for strength (radius)
	 */
	public float radiusMultiplier = 2.0f;
	
	/**
	 * Multiplier for entity damage.
	 */
	public float damageMultiplier = 3.0f;
	
	
	public float maxPathMultiplier = 1.7f;

	/**
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	public float defaultResistance = 2.0f;
	
	/**
	 * Strength changes with this factor, for explosion paths advancing in the same direction again.
	 */
	public float fStraight = 0.85f;
	
	/**
	 * Use ignored settings inverted, i.e. blacklist (not-ignored).
	 */
	public boolean invertIgnored = false;

	
	/**
	 * UNUSED (was: random resistance added to blocks)
	 */
	public float randDec = 0.2f;
	/**
	 * If to not apply damage to primed tnt.
	 */
	public boolean sparePrimed = true;
	
	public  double thresholdTntDirect = 2.0;
	
	public boolean itemTnt = false;
	
	public boolean velUse = true;
	public float velMin = 0.2f;
	public float velCen = 1.0f;
	public float velRan = 0.5f;
	public boolean velOnPrime = false;
	public float velCap = 3.0f;
	
	public int maxItems = 15;
	
	public boolean itemArrows = false;
	
	public boolean projectiles = false;
	
	public int minPrime = 30;
	public int maxPrime = 80;
	
	/**
	 * Drop chance.
	 */
	public float yield = 0.2f;
	/**
	 * Survival chance.
	 */
	public float entityYield = 0.2f;
	
	public boolean stepPhysics = false;
	
	public float projectileMultiplier = 3.0f;
	
	public float minResistance = 0.0f;
	
	/**
	 * Ignore flags for blocks:
	 * Explosion propagates beyond but does not destroy the block.
	 */
	public boolean[] ignore = new boolean[Defaults.blockArraySize];
	public float[] resistance = new float[Defaults.blockArraySize];
	
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

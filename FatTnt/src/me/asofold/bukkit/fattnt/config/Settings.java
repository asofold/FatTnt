package me.asofold.bukkit.fattnt.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
	public float radiusMultiplier = 2.125f;
	
	/**
	 * Multiplier for entity damage.
	 */
	public float damageMultiplier =  5.0f; // TODO: add some ray damage !
	
	/**
	 * Radius multiplier to modify range for collecting affected entities.
	 * 
	 */
	public float entityRadiusMultiplier = 2.0f;
	
	
	
	/**
	 * Restrict maximal path length for propagation multiplied by explosion strength.
	 */
	public float maxPathMultiplier = 1.7f;

	/**
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	public float defaultResistance = 2.0f;
	
	/**
	 * Default pass-through resistance.
	 */
	public float defaultPassthrough = Float.MAX_VALUE; 
	
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
	public float randRadius = 0.2f;
	/**
	 * If to not apply damage to primed tnt.
	 */
	public boolean sparePrimed = true;
	
	/**
	 * Currently unused [aimed at fast explosions]
	 */
	public  double thresholdTntDirect = 2.0;
	
	/**
	 * Allow tnt items to change to primed tnt if combusted or hit by explosions.
	 */
	public boolean itemTnt = false;
	
	// velocity settings
	public boolean velUse = true;
	public float velMin = 0.2f;
	public float velCen = 1.0f;
	public float velRan = 0.5f;
	public boolean velOnPrime = false;
	public float velCap = 3.0f;
	
	/**
	 * Maximal number of Item entities created from an ItemStack.
	 */
	public int maxItems = 15;
	
	/**
	 * Transform arrow items to real arrows (explosions).
	 */
	public boolean itemArrows = false;
	
	/**
	 * Affect projectiles velocity.
	 */
	public boolean projectiles = false;
	
	/**
	 * Minimum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 */
	public int minPrime = 30;
	/**
	 * Maximum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 * If set to a value greater than minPrime, the fuse ticks will be set randomly using that interval.
	 */
	public int maxPrime = 80;
	
	/**
	 * Drop chance from destroyed blocks.
	 */
	public float yield = 0.2f;
	/**
	 * Survival chance for items/entities hit by an explosion.
	 */
	public float entityYield = 0.2f;
	
	/**
	 * Use extra distance based damage.
	 */
	public boolean useDistanceDamage = true;
	
	/**
	 * Use a simple distance damage model.
	 */
	public boolean simpleDistanceDamage = false;
	
	/**
	 * Experimental:Currently does explosions without applying physics (not good),
	 * intended: apply physics after setting blocks to air.
	 */
	public boolean stepPhysics = false;
	
	/**
	 * Multiply projectiles velocity by this, if affected.
	 */
	public float projectileMultiplier = 3.0f;
	
	/**
	 * The minimal present resistance value.
	 * Set automatically from configuration input.
	 */
	public float minResistance = 0.0f;
	
	/**
	 * If a block can not be destroyed this will be checked for further propagation.
	 * created in applyConfig
	 */
	public float[] passthrough = null;
	
	/**
	 * Explosion resistance values for blocks.
	 * created in applyConfig
	 */
	public float[] resistance = null;
	
	public boolean[] propagateDamage = null;
	
	/**
	 * Multiplier for the distance based damage to entities.
	 */
	public float entityDistanceMultiplier = 0.4f; // TODO: adjust
	
	/**
	 * If to damage the armor on base of damage amount.
	 */
	public boolean armorUseDamage = false;
	public float armorMultDamage = 0.5f;
	public int armorBaseDepletion = 3;
	
	/**
	 * NOTES:<br>
	 * - Constructor does not initialize arrays !<br>
	 * - Before using applyConfig you need to add defaults to ensure all paths are there. 
	 * @param stats Are passed with settings, currently, to use the same stats object.
	 */
	public Settings(Stats stats){
		this.stats = stats;
	}
	
	public void applyConfig(Configuration cfg){
		passthrough = new float[Defaults.blockArraySize];
		resistance = new float[Defaults.blockArraySize];
		propagateDamage = new boolean[Defaults.blockArraySize];
		minResistance = Float.MAX_VALUE;
		handledEntities.clear();
		for ( String n : cfg.getStringList(Path.entities)){
			try{
				EntityType etp = EntityType.valueOf(n.toUpperCase());
				if ( etp == null) throw new IllegalArgumentException();
				handledEntities.add(etp);
			} catch (Throwable t){
				Bukkit.getServer().getLogger().warning(Defaults.msgPrefix+"Bad entity: "+n);
			}
		}
		radiusMultiplier = (float) cfg.getDouble(Path.multRadius);
		damageMultiplier = (float) cfg.getDouble(Path.multDamage);
		entityRadiusMultiplier = (float) cfg.getDouble(Path.multEntityRadius);
		entityDistanceMultiplier = (float) cfg.getDouble(Path.multEntityDistance);
		maxPathMultiplier = (float) cfg.getDouble(Path.multMaxPath);
		defaultPassthrough = (float) cfg.getDouble(Path.defaultPassthrough);
		defaultResistance = (float) cfg.getDouble(Path.defaultResistence);
		minResistance = Math.min(Math.min(minResistance, defaultResistance), defaultPassthrough);
		maxRadius = (float) cfg.getDouble(Path.maxRadius);
		randRadius = (float) cfg.getDouble(Path.randRadius);
		yield = (float) cfg.getDouble(Path.yield);
		velUse = cfg.getBoolean(Path.velUse);
		velMin = (float) cfg.getDouble(Path.velMin);
		velCen = (float) cfg.getDouble(Path.velCen);
		velRan = (float) cfg.getDouble(Path.velRan);
		fStraight = (float) cfg.getDouble(Path.fStraight);
		velOnPrime = cfg.getBoolean(Path.velOnPrime);
		thresholdTntDirect = cfg.getDouble(Path.cthresholdTntDirect);
		velCap = (float) cfg.getDouble(Path.velCap);
		itemTnt = cfg.getBoolean(Path.itemTnt);
		entityYield = (float) cfg.getDouble(Path.entityYield);
		useDistanceDamage = cfg.getBoolean(Path.useDistanceDamage);
		simpleDistanceDamage = cfg.getBoolean(Path.simpleDistanceDamage);
		maxItems = cfg.getInt(Path.maxItems);
		itemArrows = cfg.getBoolean(Path.itemArrows);
		projectiles = cfg.getBoolean(Path.projectiles);
		minPrime = cfg.getInt(Path.minPrime);
		maxPrime = cfg.getInt(Path.maxPrime);
		stepPhysics = cfg.getBoolean(Path.stepPhysics);
		projectileMultiplier = (float) cfg.getDouble(Path.multProjectiles);
		armorUseDamage = cfg.getBoolean(Path.armorUseDamage);
		armorMultDamage = (float) cfg.getDouble(Path.armorMultDamage);
		armorBaseDepletion = cfg.getInt(Path.armorBaseDepletion);
		
		if ( maxRadius > Defaults.radiusLock) maxRadius = Defaults.radiusLock; // safety check
		
		initBlockIds();
		readResistance(cfg, Path.resistence, resistance, defaultResistance);
		readResistance(cfg, Path.passthrough, passthrough, defaultPassthrough);
		List<Integer> ids = Defaults.getIdList(cfg, Path.damagePropagate);
		for ( Integer id : ids){
			propagateDamage[id] = true;
		}
	}
	
	private void readResistance(Configuration cfg, String path, float[] array, float defaultResistance){
		ConfigurationSection sec = cfg.getConfigurationSection(path);
		if (sec == null) return;
		Collection<String> keys = sec.getKeys(false);
		if ( keys != null){
			for (String key : keys){
				if ( "default".equalsIgnoreCase(key)) continue;
				float val = (float) cfg.getDouble(path+"."+key+".value", defaultResistance);
				minResistance = Math.min(minResistance, val);
				for ( Integer i : Defaults.getIdList(cfg, path+"."+key+".ids")){
					array[i] = val;
				}
			}
		}
	}
	
	private void initBlockIds() {
		for (int i = 0;i<passthrough.length;i++){
			passthrough[i] = defaultPassthrough;
			resistance[i] = defaultResistance;
			propagateDamage[i] = false;
		}
	}
	
	public void setHandleExplosions(boolean handle){
		handleExplosions = handle;
		// TODO: maybe save to some configuration file ?
	}
	
}

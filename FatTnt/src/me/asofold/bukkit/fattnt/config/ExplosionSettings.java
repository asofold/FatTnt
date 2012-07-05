package me.asofold.bukkit.fattnt.config;

import java.util.Collection;
import java.util.List;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.config.priorityvalues.PrioritySettings;

import org.bukkit.configuration.Configuration;

/**
 * General explosion settings, may have sub settings, may appear as default, world specific and entity specific.
 */
public class ExplosionSettings extends PrioritySettings{
	
	public ConfinementSettings confine;
	
	/**
	 * Explosion strength is cut off there.
	 */
	public float maxRadius = 20.0f;
	
	/**
	 * Handle and alter explosions at all.
	 * TODO: also put to config.
	 */
	public boolean handleExplosions = true;
	
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
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	public float defaultResistance = 2.0f;
	
	/**
	 * Default pass-through resistance.
	 */
	public float defaultPassthrough = Float.MAX_VALUE; 
	
	/**
	 * Use ignored settings inverted, i.e. blacklist (not-ignored).
	 */
	public boolean invertIgnored = false;
	
	/**
	 * If to not apply damage to primed tnt.
	 */
	public boolean sparePrimed = true;
	
	/**
	 * Allow tnt items to change to primed tnt if combusted or hit by explosions.
	 */
	public boolean itemTnt = false;
	
	/**
	 * Currently unused [aimed at fast explosions]
	 */
	public  double thresholdTntDirect = 2.0;
	
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
	 * Restrict maximal path length for propagation multiplied by explosion strength.
	 */
	public float maxPathMultiplier = 1.7f;
	
	/**
	 * Strength changes with this factor, for explosion paths advancing in the same direction again.
	 */
	public float fStraight = 0.85f;
	
	/**
	 * UNUSED (was: random resistance added to blocks)
	 */
	public float randRadius = 0.2f;
	
	/**
	 * Experimental:Currently does explosions without applying physics (not good),
	 * intended: apply physics after setting blocks to air.
	 */
	public boolean stepPhysics = false;
	
	public ExplosionSettings(int priority) {
		confine = new ConfinementSettings(priority);
	}

	private void initBlockIds() {
		for (int i = 0;i<passthrough.length;i++){
			passthrough[i] = defaultPassthrough;
			resistance[i] = defaultResistance;
			propagateDamage[i] = false;
		}
	}
	
	public void applyConfig(CompatConfig cfg, String prefix, int priority){
		if (cfg.contains(prefix + Path.priority)) priority  = cfg.getInt(prefix + Path.priority, (int) 0);
		resetAllValues(priority);
		
		// Confinement settings:
		confine = new ConfinementSettings(priority);
		confine.applyConfig(cfg, prefix);
		
		ExplosionSettings ref = new ExplosionSettings(0); // default settings.
		if (cfg.contains(prefix + Path.passthrough)) passthrough = new float[Defaults.blockArraySize];
		if (cfg.contains(prefix + Path.resistance)) resistance = new float[Defaults.blockArraySize];
		if (cfg.contains(prefix + Path.damagePropagate)) propagateDamage = new boolean[Defaults.blockArraySize];
		minResistance = Float.MAX_VALUE;		
		radiusMultiplier = cfg.getDouble(prefix + Path.multRadius, (double) ref.radiusMultiplier).floatValue();
		damageMultiplier = cfg.getDouble(prefix + Path.multDamage, (double) ref.damageMultiplier).floatValue();
		entityRadiusMultiplier = cfg.getDouble(prefix + Path.multEntityRadius, (double) ref.entityRadiusMultiplier).floatValue();
		entityDistanceMultiplier = cfg.getDouble(prefix + Path.multEntityDistance, (double) ref.entityDistanceMultiplier).floatValue();
		maxPathMultiplier = cfg.getDouble(prefix + Path.multMaxPath, (double) ref.maxPathMultiplier).floatValue();
		defaultPassthrough = cfg.getDouble(prefix + Path.defaultPassthrough, (double) ref.defaultPassthrough).floatValue();
		defaultResistance = cfg.getDouble(prefix + Path.defaultResistance, (double) ref.defaultResistance).floatValue();
		minResistance = Math.min(Math.min(minResistance, defaultResistance), defaultPassthrough);
		maxRadius = cfg.getDouble(prefix + Path.maxRadius, (double) ref.maxRadius).floatValue();
		randRadius = cfg.getDouble(prefix + Path.randRadius, (double) ref.randRadius).floatValue();
		
		velUse = cfg.getBoolean(prefix + Path.velUse, ref.velUse);
		velMin = cfg.getDouble(prefix + Path.velMin, (double) ref.velMin).floatValue();
		velCen = cfg.getDouble(prefix + Path.velCen, (double) ref.velCen).floatValue();
		velRan = cfg.getDouble(prefix + Path.velRan, (double) ref.velRan).floatValue();
		velOnPrime = cfg.getBoolean(prefix + Path.velOnPrime, ref.velOnPrime);
		velCap = cfg.getDouble(prefix + Path.velCap, (double) ref.velCap).floatValue();
		
		fStraight = cfg.getDouble(prefix + Path.fStraight, (double) ref.fStraight).floatValue();
		
		thresholdTntDirect = cfg.getDouble(prefix + Path.cthresholdTntDirect, ref.thresholdTntDirect);
		
		
		useDistanceDamage = cfg.getBoolean(prefix + Path.useDistanceDamage, ref.useDistanceDamage);
		simpleDistanceDamage = cfg.getBoolean(prefix + Path.simpleDistanceDamage, ref.simpleDistanceDamage);
		maxItems = cfg.getInt(prefix + Path.maxItems, ref.maxItems);
		
		projectiles = cfg.getBoolean(prefix + Path.projectiles, ref.projectiles);
		minPrime = cfg.getInt(prefix + Path.minPrime, ref.minPrime);
		maxPrime = cfg.getInt(prefix + Path.maxPrime, ref.maxPrime);
		stepPhysics = cfg.getBoolean(prefix + Path.stepPhysics, ref.stepPhysics);
		projectileMultiplier = cfg.getDouble(prefix + Path.multProjectiles, (double) ref.projectileMultiplier).floatValue();
		
		armorUseDamage = cfg.getBoolean(prefix + Path.armorUseDamage, ref.armorUseDamage);
		armorMultDamage = cfg.getDouble(prefix + Path.armorMultDamage, (double) ref.armorMultDamage).floatValue();
		armorBaseDepletion = cfg.getInt(prefix + Path.armorBaseDepletion, ref.armorBaseDepletion);
		
		yield = cfg.getDouble(prefix + Path.yield, (double) ref.yield).floatValue();
		entityYield = cfg.getDouble(prefix + Path.entityYield, (double) ref.entityYield).floatValue();
		itemTnt = cfg.getBoolean(prefix + Path.itemTnt, ref.itemTnt);
		itemArrows = cfg.getBoolean(prefix + Path.itemArrows, ref.itemArrows);
		 
		
		if ( maxRadius > Defaults.radiusLock) maxRadius = Defaults.radiusLock; // safety check
		
		// TODO: Lazy treatment of the follwing settings (keep null or set).
		initBlockIds();
		if (resistance != null) readResistance(cfg, prefix + Path.resistance, resistance, defaultResistance);
		if (passthrough != null) readResistance(cfg, prefix + Path.passthrough, passthrough, defaultPassthrough);
		if (propagateDamage != null){
			List<Integer> ids = Defaults.getIdList(cfg, prefix + Path.damagePropagate);
			for ( Integer id : ids){
				propagateDamage[id] = true;
			}
		}
	}

	private void readResistance(CompatConfig cfg, String path, float[] array, float defaultResistance){
		Collection<String> keys = cfg.getStringKeys(path);
		if ( keys != null){
			for (String key : keys){
				if ( "default".equalsIgnoreCase(key)) continue;
				float val = cfg.getDouble(path+Path.sep+key+Path.sep+"value", (double) defaultResistance).floatValue();
				minResistance = Math.min(minResistance, val);
				for ( Integer i : Defaults.getIdList(cfg, path+Path.sep+key+Path.sep+"ids")){
					array[i] = val;
				}
			}
		}
	}

	public void setHandleExplosions(boolean handle) {
		handleExplosions = handle;
	}
	
	public void applySettings(ExplosionSettings other) {
		super.applySettings(other);
		confine.applySettings(other.confine);
	}

	@Override
	public void setPriority(int priority) {
		super.setPriority(priority);
		// TODO: confine.setPriority ?
	}

	@Override
	public boolean hasValues() {
		return super.hasValues() || confine.hasValues();
	}

	@Override
	public void resetAllValues(int priority) {
		confine.resetAllValues(priority);
		super.resetAllValues(priority);
	}

	public void toConfig(Configuration cfg, String prefix) {
		// TODO Auto-generated method stub
		
	}

	
	

}

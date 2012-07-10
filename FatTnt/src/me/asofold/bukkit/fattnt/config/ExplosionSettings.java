package me.asofold.bukkit.fattnt.config;

import java.util.Collection;
import java.util.List;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.config.priorityvalues.OverridePolicy;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityBoolean;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityNumber;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityObject;
import me.asofold.bukkit.fattnt.config.priorityvalues.PrioritySettings;
import me.asofold.bukkit.fattnt.config.priorityvalues.PriorityValue;

/**
 * General explosion settings, may have sub settings, may appear as default, world specific and entity specific.
 */
public class ExplosionSettings extends PrioritySettings{
	
	public ConfinementSettings confine;

	/**
	 * Explosion strength is cut off there.
	 */
//	public float maxRadius = 20.0f;
	public final PriorityNumber maxRadius = addValue("maxRadius", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Handle and alter explosions at all.
	 * TODO: also put to config.
	 */
	public final PriorityBoolean handleExplosions = (PriorityBoolean) addValue("handleExplosions", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Multiplier for strength (radius)
	 */
	public final PriorityNumber radiusMultiplier = (PriorityNumber) addValue("radiusMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Multiplier for entity damage.
	 */
	public final PriorityNumber damageMultiplier = (PriorityNumber) addValue("damageMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE)); // TODO: add some ray damage !
	
	/**
	 * Radius multiplier to modify range for collecting affected entities.
	 * 
	 */
	public final PriorityNumber entityRadiusMultiplier = (PriorityNumber) addValue("entityRadiusMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	public final PriorityNumber defaultResistance = (PriorityNumber) addValue("defaultResistance", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Default pass-through resistance.
	 */
	public final PriorityNumber defaultPassthrough = (PriorityNumber) addValue("defaultPassthrough", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE)); 
	
	/**
	 * Use ignored settings inverted, i.e. blacklist (not-ignored).
	 */
	public final PriorityBoolean invertIgnored = (PriorityBoolean) addValue("invertIgnored", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * If to not apply damage to primed tnt.
	 */
	public final PriorityBoolean sparePrimed = (PriorityBoolean) addValue("sparePrimed", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Allow tnt items to change to primed tnt if combusted or hit by explosions.
	 */
	public final PriorityBoolean itemTnt = (PriorityBoolean) addValue("itemTnt", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Currently unused [aimed at fast explosions]
	 */
	public  final PriorityNumber thresholdTntDirect = (PriorityNumber) addValue("thresholdTntDirect", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	// velocity settings
	public final PriorityBoolean velUse = (PriorityBoolean) addValue("velUse", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));;
	public final PriorityNumber velMin = (PriorityNumber) addValue("velMin", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityNumber velCen = (PriorityNumber) addValue("velCen", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityNumber velRan = (PriorityNumber) addValue("velRan", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityBoolean velOnPrime = (PriorityBoolean) addValue("velOnPrime", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));;
	public final PriorityNumber velCap = (PriorityNumber) addValue("velCap", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Maximal number of Item entities created from an ItemStack.
	 */
	public final PriorityNumber maxItems = (PriorityNumber) addValue("maxItems", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Transform arrow items to real arrows (explosions).
	 */
	public final PriorityBoolean itemArrows = (PriorityBoolean) addValue("itemArrows", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Affect projectiles velocity.
	 */
	public final PriorityBoolean projectiles = (PriorityBoolean) addValue("projectiles", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Minimum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 */
	public final PriorityNumber minPrime = (PriorityNumber) addValue("minPrime", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	/**
	 * Maximum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 * If set to a value greater than minPrime, the fuse ticks will be set randomly using that interval.
	 */
	public final PriorityNumber maxPrime = (PriorityNumber) addValue("maxPrime", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Drop chance from destroyed blocks.
	 */
	public final PriorityNumber yield = (PriorityNumber) addValue("yield", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	/**
	 * Survival chance for items/entities hit by an explosion.
	 */
	public final PriorityNumber entityYield = (PriorityNumber) addValue("entityYield", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Use extra distance based damage.
	 */
	public final PriorityBoolean useDistanceDamage = (PriorityBoolean) addValue("useDistanceDamage", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Use a simple distance damage model.
	 */
	public final PriorityBoolean simpleDistanceDamage = (PriorityBoolean) addValue("simpleDistanceDamage", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Multiply projectiles velocity by this, if affected.
	 */
	public final PriorityNumber projectileMultiplier = (PriorityNumber) addValue("projectileMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * The minimal present resistance value.
	 * Set automatically from configuration input.
	 */
	public float minResistance = Float.MIN_VALUE; // TODO
	
	/**
	 * If a block can not be destroyed this will be checked for further propagation.
	 * created in applyConfig
	 */
	public final PriorityValue<float[]> passthrough = addValue("passthrough", new PriorityObject<float[]>(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Explosion resistance values for blocks.
	 * created in applyConfig
	 */
	public final PriorityValue<float[]> resistance = addValue("resistance", new PriorityObject<float[]>(null, 0, OverridePolicy.OVERRIDE));
	
	public final PriorityValue<boolean[]> propagateDamage = addValue("propagateDamage", new PriorityObject<boolean[]>(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Multiplier for the distance based damage to entities.
	 */
	public final PriorityNumber entityDistanceMultiplier = (PriorityNumber) addValue("entityDistanceMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE)); // TODO: adjust
	
	/**
	 * If to damage the armor on base of damage amount.
	 */
	public final PriorityBoolean armorUseDamage = (PriorityBoolean) addValue("armorUseDamage", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityNumber armorMultDamage = (PriorityNumber) addValue("armorMultDamage", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityNumber armorBaseDepletion = (PriorityNumber) addValue("armorBaseDepletion", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Restrict maximal path length for propagation multiplied by explosion strength.
	 */
	public final PriorityNumber maxPathMultiplier = (PriorityNumber) addValue("maxPathMultiplier", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * Strength changes with this factor, for explosion paths advancing in the same direction again.
	 */
	public final PriorityNumber fStraight = (PriorityNumber) addValue("fStraight", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * UNUSED (was: random resistance added to blocks)
	 */
	public final PriorityNumber randRadius = (PriorityNumber) addValue("randRadius", new PriorityNumber(null, 0, OverridePolicy.OVERRIDE));
	
	
	/**
	 * Experimental:Currently does explosions without applying physics (not good),
	 * intended: apply physics after setting blocks to air.
	 */
	public final PriorityBoolean stepPhysics = (PriorityBoolean) addValue("stepPhysics", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	// TODO: The following need paths !
	public final PriorityBoolean scheduleExplosions = (PriorityBoolean) addValue("scheduleExplosions", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityBoolean scheduleItems = (PriorityBoolean) addValue("scheduleItems", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityBoolean scheduleEntities = (PriorityBoolean) addValue("scheduleEntities", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	public final PriorityBoolean preventOtherExplosions = (PriorityBoolean) addValue("preventOtherExplosions", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	public final PriorityBoolean preventExplosions = (PriorityBoolean) addValue("preventExplosions", new PriorityBoolean(null, 0, OverridePolicy.OVERRIDE));
	
	/**
	 * field names to path.
	 * TODO: consider using path as field name !
	 */
	static final String[][] fields = new String[][]{
		{"radiusMultiplier", Path.multRadius},
		{"damageMultiplier", Path.multDamage},
		{"entityRadiusMultiplier", Path.multEntityRadius},
		{"entityDistanceMultiplier", Path.multEntityDistance},
		{"maxPathMultiplier", Path.multMaxPath},
		{"defaultPassthrough", Path.defaultPassthrough},
		{"defaultResistance", Path.defaultResistance},
		{"maxRadius", Path.maxRadius},
		{"randRadius", Path.randRadius},
		{"velUse", Path.velUse},
		{"velMin", Path.velMin},
		{"velCen", Path.velCen},
		{"velRan", Path.velRan},
		{"velOnPrime", Path.velOnPrime},
		{"velCap", Path.velCap},
		{"fStraight", Path.fStraight},
		{"thresholdTntDirect", Path.cthresholdTntDirect},
		{"useDistanceDamage", Path.useDistanceDamage},
		{"simpleDistanceDamage", Path.simpleDistanceDamage},
		{"maxItems", Path.maxItems},
		{"projectiles", Path.projectiles},
		{"minPrime", Path.minPrime},
		{"maxPrime", Path.maxPrime},
		{"stepPhysics", Path.stepPhysics},
		{"projectileMultiplier", Path.multProjectiles},
		{"armorUseDamage", Path.armorUseDamage},
		{"armorUseDamage", Path.armorMultDamage},
		{"armorBaseDepletion", Path.armorBaseDepletion},
		{"yield", Path.yield},
		{"entityYield", Path.entityYield},
		{"itemTnt", Path.itemTnt},
		{"itemArrows", Path.itemArrows},
	};
	
	public ExplosionSettings(int priority) {
		confine = new ConfinementSettings(priority);
		setPriority(priority);
	}
	
	private void initFloats(float[] a, float def){
		for (int i = 0;i<a.length;i++){
			a[i] = def;
		}
	}
	
	private void initBools(boolean[] a, boolean def){
		for (int i = 0;i<a.length;i++){
			a[i] = def;
		}
	}
	
	public void applyConfig(CompatConfig cfg, String prefix, int priority){
		if (cfg.contains(prefix + Path.priority)) priority  = cfg.getInt(prefix + Path.priority, (int) 0);
		resetAllValues(priority);
		
		// Confinement settings:
		confine = new ConfinementSettings(priority);
		confine.applyConfig(cfg, prefix);
		
	
		for (String[] pair : fields){
			updateFromCfg(pair[0], priority, cfg, pair[1]);
		}
		
//		ExplosionSettings ref = new ExplosionSettings(0); // default settings.
		if (cfg.contains(prefix + Path.defaultPassthrough)) passthrough.value = new float[Defaults.blockArraySize];
		if (cfg.contains(prefix + Path.defaultResistance)) resistance.value = new float[Defaults.blockArraySize];
		if (cfg.contains(prefix + Path.damagePropagate)) propagateDamage.value = new boolean[Defaults.blockArraySize];
		

		
		minResistance = Float.MAX_VALUE;		
		// TODO: minresistance is special (might need to be set in applySettings)!
		
		
		if ( maxRadius.getValue(0).floatValue() > Defaults.radiusLock) maxRadius.value = Defaults.radiusLock; // safety check
		
		// TODO: Lazy treatment of the follwing settings (keep null or set).
		if (resistance.value != null){
			initFloats(resistance.value, defaultResistance.value.floatValue());
			readResistance(cfg, prefix + Path.resistance, resistance.value, defaultResistance.value.floatValue());
		}
		if (passthrough.value != null){
			initFloats(passthrough.value, defaultPassthrough.value.floatValue());
			readResistance(cfg, prefix + Path.passthrough, passthrough.value, defaultPassthrough.value.floatValue());
		}
		if (propagateDamage.value != null){
			initBools(propagateDamage.value, false);
			List<Integer> ids = Defaults.getIdList(cfg, prefix + Path.damagePropagate);
			for ( Integer id : ids){
				propagateDamage.value[id] = true;
			}
		}
		
		if (defaultResistance.value != null) minResistance = Math.min(minResistance, defaultResistance.value.floatValue());
		if (defaultPassthrough.value != null) minResistance = Math.min(minResistance, defaultPassthrough.value.floatValue());
	}

	protected void updateFromCfg(String field, int priority, CompatConfig cfg, String path){
		PriorityValue<?> pv = nameValueMap.get(field); // Assume it to be present.
		if (pv instanceof PriorityNumber) ((PriorityNumber) pv).setValue(cfg.getDouble(path, null), priority);
		else if (pv instanceof PriorityBoolean)	((PriorityBoolean) pv).setValue(cfg.getBoolean(path, null), priority);
		else throw new IllegalArgumentException("Bad PriorityValue type given: " + ((pv==null)?null:pv.getClass().getSimpleName()));
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
		handleExplosions.value = handle;
	}
	
	public void applySettings(ExplosionSettings other) {
		super.applySettings(other);
		confine.applySettings(other.confine);
		minResistance = Math.min(minResistance, other.minResistance); // Always on every priority.
		// TODO: minresistance
	}

	@Override
	public void setPriority(int priority) {
		super.setPriority(priority);
		confine.setPriority(priority);
	}

	@Override
	public boolean hasValues() {
		return super.hasValues() || confine.hasValues();
	}

	@Override
	public void resetAllValues(int priority) {
		confine.resetAllValues(priority);
		super.resetAllValues(priority);
		minResistance = Float.MAX_VALUE;
	}

//	public void toConfig(Configuration cfg, String prefix) {
//		throw new RuntimeException("Not implemented: toConfig");
//	}

	
	

}

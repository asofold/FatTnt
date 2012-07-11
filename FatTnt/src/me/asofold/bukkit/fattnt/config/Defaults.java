package me.asofold.bukkit.fattnt.config;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bukkit.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bukkit.fattnt.config.compatlayer.ConfigUtil;
import me.asofold.bukkit.fattnt.config.compatlayer.NewConfig;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * Default settings and values:
 * TODO: for simply members: move values back to Settings and use newSettings() to get the default value ! [spares double defs] 
 * @author mc_dev
 *
 */
public class Defaults {
	
	/**
	 * To put in front of messages.
	 */
	public static final String msgPrefix = "[FatTnt] ";

	// -------------------------------------------------------------------------------
	
	// Block id presets (resistance) -------------------------------------------------
	
	private static final int[] defaultIgnoreBlocks = new int[]{
	//			7, // bedrock
				8,9, // water
				10,11, // lava
	//			49,90, // obsidian/nether portal
	//			119,120 // end portal / frame
				};
	private static final int[] defaultLowResistance = new int[]{
			0, // air
			8, 18, 30, 31, 32, 37,38, 39, 40, 50, 51, 55,
			59,	63, 75,76, 78, 83, 102, 104, 105, 106, 111,
	};
	private static final int[] defaultHigherResistance = new int[]{
			23, 41,42, 45, 54, 57, 95,
			98, 108, 109
	};
	private static final int[] defaultStrongResistance = new int[]{
			49, 116, 
	};
	private static final int[] defaultMaxResistance = new int[]{
			7, // bedrock
	};
	
	private static final int[] defaultPropagateDamage = new int[]{
		0, 
		6,
		8,9,
		10,11,
		18,
		27, 28,
		30,
		31,
		32,
		34,
		36,
		37,
		38,
		39,
		40,
		50,
		51,
		55,
		59,
		63,
		64,
		65, 66,
		67,
		68,
		69,
		70,72,
		75,76,
		78,
		83,
		85,
		90,
		93,94,
		96,
		101,
		104, 105,
		106,
		107,
		108,109,
		111,
		113,
		114,
		115,
		117,
		119,
		122,
	};
	
	// entitiy presets -------------------------------------------------

	
	// some default settings ------------------------------------
	
	/**
	 * Maximum explosion strength that will be accepted by config.
	 */
	static final float radiusLock = 100.0f;
	
	/**
	 * Center of a block (for addition to a block coordinate).
	 */
	public static final Vector vCenter = new Vector(0.5,0.5,0.5);
	
	/**
	 * Maximum size of entity id arrays.
	 */
	public static final int blockArraySize = 4096;
	
	
	/**
	 * Handle and alter explosions at all.
	 * TODO: also put to config.
	 */
	private static final boolean handleExplosions = true;
	private static final boolean preventOtherExplosions = true;
	private static final boolean preventExplosions = false;
	
	private static final float maxRadius = 20.0f;
	
	/**
	 * Multiplier for strength (radius)
	 */
	private static final float radiusMultiplier = 2.125f;
	
	/**
	 * TODO: experiment
	 */
	private static final float radiusMultiplierFireball = 3.5f;
	
	
	
	/**
	 * Multiplier for entity damage.
	 */
	private static final float damageMultiplier =  5.0f; // TODO: add some ray damage !
	
	/**
	 * Radius multiplier to modify range for collecting affected entities.
	 * 
	 */
	private static final float entityRadiusMultiplier = 2.0f;
	
	/**
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	private static final float defaultResistance = 2.0f;
	
	/**
	 * Default pass-through resistance.
	 */
	private static float defaultPassthrough = Float.MAX_VALUE; 
	
	/**
	 * Use ignored settings inverted, i.e. blacklist (not-ignored).
	 */
	private static final boolean invertIgnored = false;
	
	/**
	 * If to not apply damage to primed tnt.
	 */
	private static final boolean sparePrimed = true;
	
	/**
	 * Allow tnt items to change to primed tnt if combusted or hit by explosions.
	 */
	private static final boolean itemTnt = false;
	
	/**
	 * Currently unused [aimed at fast explosions]
	 */
	private static final  double thresholdTntDirect = 2.0;
	
	// velocity settings
	private static final boolean velUse = true;
	private static final float velMin = 0.2f;
	private static final float velCen = 1.0f;
	private static final float velRan = 0.5f;
	private static final boolean velOnPrime = false;
	private static final float velCap = 3.0f;
	
	/**
	 * Maximal number of Item entities created from an ItemStack.
	 */
	private static final int maxItems = 15;
	
	/**
	 * Transform arrow items to real arrows (explosions).
	 */
	private static final boolean itemArrows = false;
	
	/**
	 * Affect projectiles velocity.
	 */
	private static final boolean projectiles = false;
	
	/**
	 * Minimum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 */
	private static final int minPrime = 30;
	/**
	 * Maximum fuse ticks, if primed tnt is created.
	 * Set  to <=0 to have default fuse ticks.
	 * If set to a value greater than minPrime, the fuse ticks will be set randomly using that interval.
	 */
	private static final int maxPrime = 80;
	
	/**
	 * Drop chance from destroyed blocks.
	 */
	private static final float yield = 0.2f;
	/**
	 * Survival chance for items/entities hit by an explosion.
	 */
	private static final float entityYield = 0.2f;
	
	/**
	 * Use extra distance based damage.
	 */
	private static final boolean useDistanceDamage = true;
	
	/**
	 * Use a simple distance damage model.
	 */
	private static final boolean simpleDistanceDamage = false;
	
	/**
	 * Multiply projectiles velocity by this, if affected.
	 */
	private static final float projectileMultiplier = 3.0f;
	
//	/**
//	 * The minimal present resistance value.
//	 * Set automatically from configuration input.
//	 */
//	public float minResistance = 0.0f;
	
	/**
	 * Multiplier for the distance based damage to entities.
	 */
	private static final float entityDistanceMultiplier = 0.4f; // TODO: adjust
	
	/**
	 * If to damage the armor on base of damage amount.
	 */
	private static final boolean armorUseDamage = false;
	private static final float armorMultDamage = 0.5f;
	private static final int armorBaseDepletion = 3;
	
	/**
	 * Restrict maximal path length for propagation multiplied by explosion strength.
	 */
	private static final float maxPathMultiplier = 1.7f;
	
	/**
	 * Strength changes with this factor, for explosion paths advancing in the same direction again.
	 */
	private static final float fStraight = 0.85f;
	
	/**
	 * UNUSED (was: random resistance added to blocks)
	 */
	private static final float randRadius = 0.2f;
	
	
	/**
	 * Experimental:Currently does explosions without applying physics (not good),
	 * intended: apply physics after setting blocks to air.
	 */
	private static final boolean stepPhysics = false;
	
	// TODO: The following need paths !
	private static final boolean scheduleExplosions = true;
	private static final boolean scheduleItems = true;
	private static final boolean scheduleEntities = true;

	
	/**
	 * Simple default values.
	 */
	static CompatConfig simpleDefaults;
	
	/**
	 * Default config with all necessary values set.
	 */
	static ExplosionSettings defaultExplosionSettings;
	
	
	
	
	static{
		simpleDefaults = getSimpleDefaultConfiguration();
		defaultExplosionSettings = getDefaultExplosionSettings();
	}
	
	/**
	 * Used for all entries that can be checked with if (!Configuration.contains(path)) ... (add it as a whole).
	 * @return
	 */
	private static CompatConfig getSimpleDefaultConfiguration(){
//		ExplosionSettings defaults = new ExplosionSettings(Integer.MIN_VALUE); // read defaults from here.
		CompatConfig cfg = new NewConfig(null);
		
		// General:
		cfg.set(Path.handleExplosions, handleExplosions);
		cfg.set(Path.preventExplosions, preventExplosions);
		cfg.set(Path.preventOtherExplosions, preventOtherExplosions);
		
		// passthrough
		cfg.set(Path.defaultPassthrough, defaultPassthrough);
		
		// resistance
		float[] v = new float[]{1.0f, 4.0f, 20.0f, Float.MAX_VALUE};
		int[][] ids = new int[][]{defaultLowResistance, defaultHigherResistance, defaultStrongResistance, defaultMaxResistance};
		String[] keys = new String[]{"low", "higher", "strongest", "indestructible"};
		for ( int i = 0; i<v.length; i++){
			String base = Path.resistance+"."+keys[i];
			List<Integer> resSet = new LinkedList<Integer>();
			for ( int id: ids[i]) {
				resSet.add(id);
			}
			cfg.set(base+".value", v[i]);
			cfg.set(base+".ids", resSet);
		}
		cfg.set(Path.defaultResistance, defaultResistance);
		
		// damage propagation
		List<Integer> entries = new LinkedList<Integer>();
		for (int i : defaultPropagateDamage){
			entries.add(i);
		}
		cfg.set(Path.damagePropagate, entries);
			
		// explosion basics:
		cfg.set(Path.maxRadius, maxRadius);
		cfg.set(Path.multDamage, damageMultiplier);
		cfg.set(Path.multRadius, radiusMultiplier);
		cfg.set(Path.explodingEntities + Path.sep + "FIREBALL" + Path.sep + Path.multRadius, radiusMultiplierFireball);
		cfg.set(Path.multMaxPath, maxPathMultiplier);
		cfg.set(Path.randRadius, randRadius); // TODO DEPRECATED ?
		cfg.set(Path.yield, yield);
		cfg.set(Path.entityYield, entityYield);
		
		// velocity:
		cfg.set(Path.velUse, velUse);
		cfg.set(Path.velMin, velMin);
		cfg.set(Path.velCen, velCen);			
		cfg.set(Path.velRan, velRan);
		cfg.set(Path.velOnPrime, velOnPrime);	
		cfg.set(Path.velCap, velCap);
		
		// array propagation specific
		cfg.set(Path.fStraight, fStraight);			
			
		// item transformationz
		cfg.set(Path.sparePrimed, sparePrimed);
		cfg.set(Path.itemTnt, itemTnt);
		cfg.set(Path.maxItems, maxItems);
		cfg.set(Path.itemArrows, itemArrows);
		
		// Projectiles:
		cfg.set(Path.multProjectiles, projectileMultiplier);
		cfg.set(Path.projectiles, projectiles);
			
		// tnt specific
		cfg.set(Path.minPrime, minPrime);
		cfg.set(Path.maxPrime, maxPrime);
		cfg.set(Path.cthresholdTntDirect, thresholdTntDirect); // unused ?	
			
		// physics
		cfg.set(Path.stepPhysics, stepPhysics);
			
		// armor
		cfg.set(Path.armorBaseDepletion, armorBaseDepletion);
		cfg.set(Path.armorMultDamage, armorMultDamage);
		cfg.set(Path.armorUseDamage, armorUseDamage);
			
		// entity damage - beyond block damage)
		cfg.set(Path.multEntityDistance, entityDistanceMultiplier);
		cfg.set(Path.multEntityRadius, entityRadiusMultiplier);
		cfg.set(Path.simpleDistanceDamage, simpleDistanceDamage);
		cfg.set(Path.useDistanceDamage, useDistanceDamage);
		
		// TODO: these are a workaround:
		cfg.set(Path.confineEnabled, false);
		cfg.set(Path.confineYMin, 0);
		cfg.set(Path.confineYMax, 255);
		
		cfg.set(Path.schedExplosionsUse, scheduleExplosions);
		cfg.set(Path.schedEntitiesUse, scheduleEntities);
		cfg.set(Path.schedItemsUse, scheduleItems);
		return cfg;
	}
	
	public static ExplosionSettings getDefaultExplosionSettings(){
		ExplosionSettings out = new ExplosionSettings(Integer.MIN_VALUE);
		
		// ??
		out.confine = new ConfinementSettings(Integer.MIN_VALUE);
		
		// ??
		out.passthrough.value = new float[Defaults.blockArraySize];
		out.resistance.value = new float[Defaults.blockArraySize];
		out.propagateDamage.value = new boolean[Defaults.blockArraySize];
		
		
		
		out.applyConfig(simpleDefaults, "", Integer.MIN_VALUE);
		return out;
	}
	
	/**
	 * Add non present default settings.
	 * @param cfg
	 * @return If changes were done.
	 */
	public static boolean addDefaultSettings(CompatConfig cfg) {
		return ConfigUtil.forceDefaults(simpleDefaults, cfg);
	}
	
	/**
	 * Convenience method to allow for integers and block names. [Integers work, blocks?]
	 * @param cfg
	 * @param path
	 * @return
	 */
	public static List<Integer> getIdList(CompatConfig cfg, String path){
		List<Integer> out = new LinkedList<Integer>();
		List<String> ref = cfg.getStringList(path);
		if (ref == null) return out;
		for ( Object x : ref){
			Integer id = null;
			if ( x instanceof Number){
				// just in case
				id = ((Number) x).intValue();
			} else if ( x instanceof String){
				try{
					id = Integer.parseInt((String) x);
				} catch(NumberFormatException exc) {
					Material mat = Material.matchMaterial((String) x);
					if ( mat != null){
						id = mat.getId();
					}
				}
			}
			if (id!=null){
				if ( id>=0 && id<4096) out.add(id);
				continue;
			}
			Bukkit.getServer().getLogger().warning(Defaults.msgPrefix+"Bad item ("+path+"): "+x);
		}
		return out;
	}

}

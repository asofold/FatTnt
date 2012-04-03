package me.asofold.bukkit.fattnt.config;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

/**
 * Default settings and values:
 * TODO: for simply members: move values back to Settings and use newSettings() to get the default value ! [spares double defs] 
 * @author mc_dev
 *
 */
public class Defaults {

	// config
	public static final String cfgMultRadius = "multiplier.radius";
	public static final String cfgMultDamage = "multiplier.damage";
	public static final String cfgMultMaxPath = "multiplier.max-path";
	public static final String cfgMultProjectiles = "multiplier.projectiles";
	public static final String cfgIgnore = "ignore-blocks";
	public static final String cfgInvertIgnored= "invert-ignored";
	public static final String cfgResistence = "resistence";
	public static final String cfgDefaultResistence = "resistence.default";
	public static final String cfgMaxRadius = "radius.max";
	public static final String cfgRandRadius = "radius.random";
	public static final String cfgEntities= "entities";
	public static final String cfgYield = "yield";
	public static final String cfgEntityYield = "entity-yield";
	public static final String cfgMaxItems = "max-items";
	public static final String cfgVelUse = "velocity.use";
	public static final String cfgVelMin = "velocity.min";
	public static final String cfgVelCen= "velocity.center";
	public static final String cfgVelRan = "velocity.random";
	public static final String cfgVelOnPrime = "velocity.tnt-primed";
	public static final String cfgVelCap = "velocity.cap";
	public static final String cfgFStraight = "multiplier.straight";
	public static final String cfgThresholdTntDirect = "tnt.thresholds.direct-explode";
	public static final String cfgItemTnt = "item-tnt";
	public static final String cfgItemArrows = "item-arrows";
	public static final String cfgProjectiles = "projectiles";
	public static final String cfgMinPrime = "min-prime";
	public static final String cfgMaxPrime = "max-prime";
	public static final String cfgStepPhysics= "step-physics";
	
	public static final int[] defaultIgnoreBlocks = new int[]{
	//			7, // bedrock
				8,9, // water
				10,11, // lava
	//			49,90, // obsidian/nether portal
	//			119,120 // end portal / frame
				};
	public static final int[] defaultLowResistance = new int[]{
			0, // air
			8, 18, 30, 31, 32, 37,38, 39, 40, 50, 51, 55,
			59,	63, 75,76, 78, 83, 102, 104, 105, 106, 111,
	};
	public static final int[] defaultHigherResistance = new int[]{
			23, 41,42, 45, 54, 57, 95,
			98, 108, 109
	};
	public static final int[] defaultStrongResistance = new int[]{
			49, 116, 
	};
	public static final int[] defaultMaxResistance = new int[]{
			7, // bedrock
	};
	// other
	/**
	 * To put in front of messages.
	 */
	public static final String msgPrefix = "[FatTnt] ";
	public static final float radiusLock = 100.0f;
	
	public static final Vector vCenter = new Vector(0.5,0.5,0.5);
	
	public static final int blockArraySize = 4096; 
	
	/**
	 * Handle and alter explosions
	 */
	public static final boolean handleExplosions = true;
	
	/**
	 * Explosion strength is cut off there.
	 */
	public static final float maxRadius = 20.0f;
	
	/**
	 * Multiplier for strength (radius)
	 */
	public static final float radiusMultiplier = 2.0f;
	
	/**
	 * Multiplier for entity damage.
	 */
	public static final float damageMultiplier = 3.0f;
	
	
	public static final float maxPathMultiplier = 1.7f;
	
	/**
	 * Default explosion  resistance value for all materials that are not in one of the resistance-lists.
	 */
	public static final float defaultResistance = 2.0f;
	
	/**
	 * Strength changes with this factor, for explosion paths advancing in the same direction again.
	 */
	public static final float fStraight = 0.85f;
	
	/**
	 * Use ignored settings inverted, i.e. blacklist (not-ignored).
	 */
	public static final boolean invertIgnored = false;
	
	public static final String[] handledEntities = new String[]{
		"PRIMED_TNT",
	};
	
	/**
	 * UNUSED (was: random resistance added to blocks)
	 */
	public static final float randDec = 0.2f;
	/**
	 * If to not apply damage to primed tnt.
	 */
	public static final boolean sparePrimed = true;
	
	public static final double thresholdTntDirect = 2.0;
	
	public static final boolean itemTnt = false;
	
	public static final boolean velUse = true;
	public static final float velMin = 0.2f;
	public static final float velCen = 1.0f;
	public static final float velRan = 0.5f;
	public static final boolean velOnPrime = false;
	public static final float velCap = 3.0f;
	
	public static final int maxItems = 15;
	
	public static final boolean itemArrows = false;
	
	public static final boolean projectiles = false;
	
	public static final int minPrime = 30;
	public static final int maxPrime = 80;
	
	/**
	 * Drop chance.
	 */
	public static final  float yield = 0.2f;
	/**
	 * Survival chance.
	 */
	public static final  float entityYield = 0.2f;
	
	public static final boolean stepPhysics = false;
	
	public static final float projectileMultiplier = 3.0f;
	
	/**
	 * 
	 * @param cfg
	 * @return If changes were done.
	 */
	public static boolean addDefaultSettings(FileConfiguration cfg) {
		boolean changed = false;
		if ( !cfg.contains(cfgEntities)){
			List<String> l = new LinkedList<String>();
			for (String et : handledEntities){
				l.add(et);
			}
			cfg.set(cfgEntities, l);
			changed = true;
		}
		if ( !cfg.contains(cfgIgnore)){
			List<Integer> l = new LinkedList<Integer>();
			for (int i : defaultIgnoreBlocks){
				l.add(i);
			}
			cfg.set(cfgIgnore, l);
			changed = true;
		}
		if ( !cfg.contains(cfgInvertIgnored)){
			cfg.set(cfgInvertIgnored, invertIgnored);
			changed = true;
		}
		if ( !cfg.contains(cfgResistence)){
			float[] v = new float[]{1.0f, 4.0f, 20.0f, Float.MAX_VALUE};
			int[][] ids = new int[][]{defaultLowResistance, defaultHigherResistance, defaultStrongResistance, defaultMaxResistance};
			String[] keys = new String[]{"low", "higher", "strongest", "indestructible"};
			for ( int i = 0; i<v.length; i++){
				String base = cfgResistence+"."+keys[i];
				List<Integer> l = new LinkedList<Integer>();
				for ( int id: ids[i]) {
					l.add(id);
				}
				cfg.set(base+".value", v[i]);
				cfg.set(base+".ids", l);
			}
			changed = true;
		}
		if ( !cfg.contains(cfgDefaultResistence)){
			cfg.set(cfgDefaultResistence, defaultResistance);
			changed = true;
		}
		if ( !cfg.contains(cfgMaxRadius)){
			cfg.set(cfgMaxRadius, maxRadius);
			changed = true;
		}
		if ( !cfg.contains(cfgMultDamage)){
			cfg.set(cfgMultDamage, damageMultiplier);
			changed = true;
		}
		if ( !cfg.contains(cfgMultRadius)){
			cfg.set(cfgMultRadius, radiusMultiplier);
			changed = true;
		}
		if ( !cfg.contains(cfgMultMaxPath)){
			cfg.set(cfgMultMaxPath, maxPathMultiplier);
			changed = true;
		}
		if ( !cfg.contains(cfgRandRadius)){
			cfg.set(cfgRandRadius, randDec); // TODO DEPRECATED ?
			changed = true;
		}
		if ( !cfg.contains(cfgYield)){
			cfg.set(cfgYield, yield);
			changed = true;
		}
		if ( !cfg.contains(cfgEntityYield)){
			cfg.set(cfgEntityYield, entityYield);
			changed = true;
		}
		if ( !cfg.contains(cfgVelUse)){
			cfg.set(cfgVelUse, velUse);
			changed = true;
		}
		if ( !cfg.contains(cfgVelMin)){
			cfg.set(cfgVelMin, velMin);
			changed = true;
		}
		if ( !cfg.contains(cfgVelCen)){
			cfg.set(cfgVelCen, velCen);
			changed = true;
		}
		if ( !cfg.contains(cfgVelRan)){
			cfg.set(cfgVelRan, velRan);
			changed = true;
		}
		if ( !cfg.contains(cfgFStraight)){
			cfg.set(cfgFStraight, fStraight);
			changed = true;
		}
		if ( !cfg.contains(cfgVelOnPrime)){
			cfg.set(cfgVelOnPrime, velOnPrime);
			changed = true;
		}
		if ( !cfg.contains(cfgVelCap)){
			cfg.set(cfgVelCap, velCap);
			changed = true;
		}
		if ( !cfg.contains(cfgThresholdTntDirect)){
			cfg.set(cfgThresholdTntDirect, thresholdTntDirect);
			changed = true;
		}
		if ( !cfg.contains(cfgItemTnt)){
			cfg.set(cfgItemTnt, itemTnt);
			changed = true;
		}
		if ( !cfg.contains(cfgMaxItems)){
			cfg.set(cfgMaxItems, maxItems);
			changed = true;
		}
		if ( !cfg.contains(cfgItemArrows)){
			cfg.set(cfgItemArrows, itemArrows);
			changed = true;
		}
		if ( !cfg.contains(cfgProjectiles)){
			cfg.set(cfgProjectiles, projectiles);
			changed = true;
		}
		if ( !cfg.contains(cfgMinPrime)){
			cfg.set(cfgMinPrime, minPrime);
			changed = true;
		}
		if ( !cfg.contains(cfgMaxPrime)){
			cfg.set(cfgMaxPrime, maxPrime);
			changed = true;
		}
		if ( !cfg.contains(cfgStepPhysics)){
			cfg.set(cfgStepPhysics, stepPhysics);
			changed = true;
		}
		if ( !cfg.contains(cfgMultProjectiles)){
			cfg.set(cfgMultProjectiles, projectileMultiplier);
			changed = true;
		}
		return changed;
	}
	
	public static List<Integer> getIdList(Configuration cfg, String path){
		List<Integer> out = new LinkedList<Integer>();
		List<String> ref = cfg.getStringList(path);
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

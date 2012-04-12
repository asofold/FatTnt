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
	
	/**
	 * To put in front of messages.
	 */
	public static final String msgPrefix = "[FatTnt] ";

	// -------------------------------------------------------------------------------
	
	// Block id presets (resistance) -------------------------------------------------
	
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
	
	public static final int[] defaultPropagateDamage = new int[]{
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
	public static final String[] handledEntities = new String[]{
		"PRIMED_TNT",
	};
	
	// some default settings ------------------------------------
	
	/**
	 * Maximum explosion strength that will be accepted by config.
	 */
	public static final float radiusLock = 100.0f;
	
	/**
	 * Center of a block (for addition to a block coordinate).
	 */
	public static final Vector vCenter = new Vector(0.5,0.5,0.5);
	
	/**
	 * Maximum size of entity id arrays.
	 */
	public static final int blockArraySize = 4096;
	
	/**
	 * 
	 * @param cfg
	 * @return If changes were done.
	 */
	public static boolean addDefaultSettings(FileConfiguration cfg) {
		boolean changed = false;
		Settings defaults = new Settings(null); // read defaults from here.
		
		if ( !cfg.contains(Path.cfgEntities)){
			List<String> l = new LinkedList<String>();
			for (String et : handledEntities){
				l.add(et);
			}
			cfg.set(Path.cfgEntities, l);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgDefaultPassthrough)){
			cfg.set(Path.cfgDefaultPassthrough, defaults.defaultPassthrough);
			changed = true;
		}
		// no default ids for passthrough
		if ( !cfg.contains(Path.cfgResistence)){
			float[] v = new float[]{1.0f, 4.0f, 20.0f, Float.MAX_VALUE};
			int[][] ids = new int[][]{defaultLowResistance, defaultHigherResistance, defaultStrongResistance, defaultMaxResistance};
			String[] keys = new String[]{"low", "higher", "strongest", "indestructible"};
			for ( int i = 0; i<v.length; i++){
				String base = Path.cfgResistence+"."+keys[i];
				List<Integer> l = new LinkedList<Integer>();
				for ( int id: ids[i]) {
					l.add(id);
				}
				cfg.set(base+".value", v[i]);
				cfg.set(base+".ids", l);
			}
			changed = true;
		}
		if ( !cfg.contains(Path.cfgDefaultResistence)){
			cfg.set(Path.cfgDefaultResistence, defaults.defaultResistance);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMaxRadius)){
			cfg.set(Path.cfgMaxRadius, defaults.maxRadius);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMultDamage)){
			cfg.set(Path.cfgMultDamage, defaults.damageMultiplier);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMultRadius)){
			cfg.set(Path.cfgMultRadius, defaults.radiusMultiplier);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMultMaxPath)){
			cfg.set(Path.cfgMultMaxPath, defaults.maxPathMultiplier);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgRandRadius)){
			cfg.set(Path.cfgRandRadius, defaults.randDec); // TODO DEPRECATED ?
			changed = true;
		}
		if ( !cfg.contains(Path.cfgYield)){
			cfg.set(Path.cfgYield, defaults.yield);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgEntityYield)){
			cfg.set(Path.cfgEntityYield, defaults.entityYield);
			changed = true;
		}
		if ( !cfg.contains(Path.velUse)){
			cfg.set(Path.velUse, defaults.velUse);
			changed = true;
		}
		if ( !cfg.contains(Path.velMin)){
			cfg.set(Path.velMin, defaults.velMin);
			changed = true;
		}
		if ( !cfg.contains(Path.velCen)){
			cfg.set(Path.velCen, defaults.velCen);
			changed = true;
		}
		if ( !cfg.contains(Path.velRan)){
			cfg.set(Path.velRan, defaults.velRan);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgFStraight)){
			cfg.set(Path.cfgFStraight, defaults.fStraight);
			changed = true;
		}
		if ( !cfg.contains(Path.velOnPrime)){
			cfg.set(Path.velOnPrime, defaults.velOnPrime);
			changed = true;
		}
		if ( !cfg.contains(Path.velCap)){
			cfg.set(Path.velCap, defaults.velCap);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgThresholdTntDirect)){
			cfg.set(Path.cfgThresholdTntDirect, defaults.thresholdTntDirect);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgItemTnt)){
			cfg.set(Path.cfgItemTnt, defaults.itemTnt);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMaxItems)){
			cfg.set(Path.cfgMaxItems, defaults.maxItems);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgItemArrows)){
			cfg.set(Path.cfgItemArrows, defaults.itemArrows);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgProjectiles)){
			cfg.set(Path.cfgProjectiles, defaults.projectiles);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMinPrime)){
			cfg.set(Path.cfgMinPrime, defaults.minPrime);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMaxPrime)){
			cfg.set(Path.cfgMaxPrime, defaults.maxPrime);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgStepPhysics)){
			cfg.set(Path.cfgStepPhysics, defaults.stepPhysics);
			changed = true;
		}
		if ( !cfg.contains(Path.cfgMultProjectiles)){
			cfg.set(Path.cfgMultProjectiles, defaults.projectileMultiplier);
			changed = true;
		}
		if (!cfg.contains(Path.armorBaseDepletion)){
			cfg.set(Path.armorBaseDepletion, defaults.armorBaseDepletion);
			changed = true;
		}
		if (!cfg.contains(Path.armorMultDamage)){
			cfg.set(Path.armorMultDamage, defaults.armorMultDamage);
			changed = true;
		}
		if (!cfg.contains(Path.armorUseDamage)){
			cfg.set(Path.armorUseDamage, defaults.armorUseDamage);
			changed = true;
		}
		if (!cfg.contains(Path.multEntityDistance)){
			cfg.set(Path.multEntityDistance, defaults.entityDistanceMultiplier);
			changed = true;
		}
		if (!cfg.contains(Path.cfgMultEntityRadius)){
			cfg.set(Path.cfgMultEntityRadius, defaults.entityRadiusMultiplier);
			changed = true;
		}
		if (!cfg.contains(Path.simpleDistanceDamage)){
			cfg.set(Path.simpleDistanceDamage, defaults.simpleDistanceDamage);
			changed = true;
		}
		if (!cfg.contains(Path.useDistanceDamage)){
			cfg.set(Path.useDistanceDamage, defaults.useDistanceDamage);
			changed = true;
		}
		if (!cfg.contains(Path.cfgDamagePropagate)){
			List<Integer> entries = new LinkedList<Integer>();
			for (int i : Defaults.defaultPropagateDamage){
				entries.add(i);
			}
			cfg.set(Path.cfgDamagePropagate, entries);
			changed = true;
		}
		return changed;
	}
	
	/**
	 * Convenience method to allow for integers and block names. [Integers work, blocks?]
	 * @param cfg
	 * @param path
	 * @return
	 */
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

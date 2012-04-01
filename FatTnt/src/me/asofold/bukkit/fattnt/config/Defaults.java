package me.asofold.bukkit.fattnt.config;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class Defaults {

	// config
	public static final String cfgMultRadius = "multiplier.radius";
	public static final String cfgMultDamage = "multiplier.damage";
	public static final String cfgIgnore = "ignore-blocks";
	public static final String cfgInvertIgnored= "invert-ignored";
	public static final String cfgResistence = "resistence";
	public static final String cfgDefaultResistence = "resistence.default";
	public static final String cfgMaxRadius = "radius.max";
	public static final String cfgRandRadius = "radius.random";
	public static final String cfgEntities= "entities";
	public static final String cfgYield = "yield";
	public static final String cfgVelUse = "velocity.use";
	public static final String cfgVelMin = "velocity.min";
	public static final String cfgVelCen= "velocity.center";
	public static final String cfgVelRan = "velocity.random";
	public static final String cfgVelOnPrime = "velocity.tnt-primed";
	public static final String cfgFStraight = "multiplier.straight";
	public static final String cfgThresholdTntDirect = "tnt.thresholds.direct-explode";
	
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
			1, 4, 22, 23, 41,42,45, 44, 48, 54, 57, 
			98, 108, 109, 112, 95
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
	
	
//	/**
//	 * opposite direction:
//	 * 0:  no direction
//	 * 1:  reserved: diagonal
//	 * 2:  x+
//	 * 3:  reserved: diagonal
//	 * 4:  x-
//	 * 5:  reserved: diagonal
//	 * 6:  y+
//	 * 7:  reserved: diagonal
//	 * 8:  y-
//	 * 9:  reserved: diagonal
//	 * 10: z+
//	 * 11: reserved: diagonal
//	 * 12: z-
//	 */
//	private final static int[] oDir = new int[]{
//		0,  // 0: no direction maps to no direction
//		0,  // UNUSED
//		4,  // x+ -> x-
//		0,  // UNUSED
//		2,  // x- -> x+
//		0,  // UNUSED
//		8,  // y+ -> y-
//		0,  // UNUSED
//		6,  // y- -> y+
//		0,  // UNUSED
//		12, // z+ -> z-
//		0,  // UNUSED
//		10, // z- -> z+
//	} ;
	
	
	/**
	 * 
	 * @param cfg
	 * @return If changes were done.
	 */
	public static boolean addDefaultSettings(FileConfiguration cfg) {
		boolean changed = false;
		if ( !cfg.contains(cfgEntities)){
			List<String> l = new LinkedList<String>();
			l.add("PRIMED_TNT");
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
			cfg.set(cfgInvertIgnored, false);
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
			cfg.set(cfgDefaultResistence, 2.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMaxRadius)){
			cfg.set(cfgMaxRadius, 20.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMultDamage)){
			cfg.set(cfgMultDamage, 7.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMultRadius)){
			cfg.set(cfgMultRadius, 2.0);
			changed = true;
		}
		if ( !cfg.contains(cfgRandRadius)){
			cfg.set(cfgRandRadius, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgYield)){
			cfg.set(cfgYield, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgVelUse)){
			cfg.set(cfgVelUse, true);
			changed = true;
		}
		if ( !cfg.contains(cfgVelMin)){
			cfg.set(cfgVelMin, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgVelCen)){
			cfg.set(cfgVelCen, 3.0);
			changed = true;
		}
		if ( !cfg.contains(cfgVelRan)){
			cfg.set(cfgVelRan, 1.5);
			changed = true;
		}
		if ( !cfg.contains(cfgFStraight)){
			cfg.set(cfgFStraight, 0.85);
			changed = true;
		}
		if ( !cfg.contains(cfgVelOnPrime)){
			cfg.set(cfgVelOnPrime, false);
			changed = true;
		}
		if ( !cfg.contains(cfgThresholdTntDirect)){
			cfg.set(cfgThresholdTntDirect, 2.0);
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

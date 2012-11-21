package me.asofold.bpl.fattnt.config;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bpl.fattnt.config.compatlayer.CompatConfig;
import me.asofold.bpl.fattnt.config.compatlayer.ConfigUtil;
import me.asofold.bpl.fattnt.config.compatlayer.NewConfig;

import org.bukkit.Bukkit;
import org.bukkit.Material;

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
	
	// some default settings ------------------------------------
	
	/**
	 * Maximum size of entity id arrays.
	 */
	public static final int blockArraySize = 4096;
	
	/**
	 * Maximum explosion strength that will be accepted by config.
	 */
	static final float radiusLock = 100.0f;

	
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
	public static CompatConfig getSimpleDefaultConfiguration(){
//		ExplosionSettings defaults = new ExplosionSettings(Integer.MIN_VALUE); // read defaults from here.
		CompatConfig cfg = new NewConfig(null);
		
		// General:
		cfg.set(Path.handleExplosions, true);
		cfg.set(Path.preventExplosions, false);
		cfg.set(Path.preventOtherExplosions, true);
		
		// passthrough
		cfg.set(Path.defaultPassthrough, Float.MAX_VALUE);
		
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
		cfg.set(Path.defaultResistance, 2.0);
		
		// damage propagation
		cfg.set(Path.damagePropagate, ConfigUtil.asList(defaultPropagateDamage));
		
		// no blockdamage ?
		// TODO: set minecraft defaults here ?
		cfg.set(Path.damagePreventBlocks, new LinkedList<String>());
			
		// explosion basics:
		cfg.set(Path.maxRadius, 20.0);
		cfg.set(Path.multDamage, 5.0);
		cfg.set(Path.multRadius, 2.125);
		cfg.set(Path.multMaxPath, 1.7);
		cfg.set(Path.randRadius, 0.2); // TODO DEPRECATED ?
		cfg.set(Path.yield, 0.2);
		cfg.set(Path.entityYield, 0.2);
		
		// velocity:
		cfg.set(Path.velUse, true);
		cfg.set(Path.velMin, 0.2);
		cfg.set(Path.velCen, 1.0);			
		cfg.set(Path.velRan, 0.5);
		cfg.set(Path.velOnPrime, false);	
		cfg.set(Path.velCap, 3.0);
		
		// array propagation specific
		cfg.set(Path.fStraight, 0.85);			
			
		// item transformationz
		cfg.set(Path.sparePrimed, true);
		cfg.set(Path.itemTnt, false);
		cfg.set(Path.maxItems, 15);
		cfg.set(Path.itemArrows, false);
		
		// Projectiles:
		cfg.set(Path.multProjectiles, 3.0);
		cfg.set(Path.projectiles, false);
			
		// tnt specific
		cfg.set(Path.minPrime, 30);
		cfg.set(Path.maxPrime, 80);
		cfg.set(Path.cthresholdTntDirect, 3.0); // unused ?	
			
		// physics
		cfg.set(Path.stepPhysics, false);
			
		// armor
		cfg.set(Path.armorBaseDepletion, 3);
		cfg.set(Path.armorMultDamage, 0.5);
		cfg.set(Path.armorUseDamage, false);
			
		// entity damage - beyond block damage)
		cfg.set(Path.multEntityDistance, 0.4);
		cfg.set(Path.multEntityRadius, 2.0);
		cfg.set(Path.simpleDistanceDamage, false);
		cfg.set(Path.useDistanceDamage, true);
		
		// TODO: these are a workaround:
		cfg.set(Path.confineEnabled, false);
		cfg.set(Path.confineYMin, 0);
		cfg.set(Path.confineYMax, 255);
		
		cfg.set(Path.schedExplosionsUse, true);
		cfg.set(Path.schedEntitiesUse, true);
		cfg.set(Path.schedItemsUse, true);
		return cfg;
	}
	
	/**
	 * Get the built in default settings.<br>
	 * This will create a new configuration object internally and read the settings from there. 
	 * @return
	 */
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
		if (cfg.getValuesDeep().isEmpty()){
			// only add to new configs
			cfg.set(Path.explodingEntities + Path.sep + "FIREBALL" + Path.sep + Path.multRadius, 3.2);
		}
		boolean changed = ConfigUtil.forceDefaults(simpleDefaults, cfg);
		
		
		return changed;
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

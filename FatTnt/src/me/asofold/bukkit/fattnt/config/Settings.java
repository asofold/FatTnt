package me.asofold.bukkit.fattnt.config;

import java.util.HashSet;
import java.util.Set;

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
	
	/**
	 * Handle and alter explosions
	 */
	public boolean handleExplosions = true;
	
	/**
	 * Defaults to empty !
	 */
	public final Set<EntityType> handledEntities = new HashSet<EntityType>();
	
	public float maxRadius = 20.0f;
	
	public float radiusMultiplier = 4.0f;
	
	public float damageMultiplier = 5.0f;
	
	public float defaultResistance = 2.0f;
	
	public float fStraight = 0.85f;
	
	public boolean invertIgnored = false;
	
	public float randDec = 0.2f;
	/**
	 * If to not apply damage to primed tnt.
	 */
	public boolean sparePrimed = true;
	
	public double thresholdTntDirect = 2.0;
	
	public boolean velUse = true;
	public float velMin = 0.2f;
	public float velCen = 1.0f;
	public float velRan = 0.5f;
	public boolean velOnPrime = false;
	
	public boolean[] ignore = new boolean[4096];
	public float[] resistance = new float[4096];

	public  float defaultYield = 0.2f;
	
	public void applyConfig(Configuration cfg){
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
		invertIgnored = cfg.getBoolean(Defaults.cfgInvertIgnored);
		defaultResistance = (float) cfg.getDouble(Defaults.cfgDefaultResistence);
		maxRadius = (float) cfg.getDouble(Defaults.cfgMaxRadius);
		randDec = (float) cfg.getDouble(Defaults.cfgRandRadius);
		defaultYield = (float) cfg.getDouble(Defaults.cfgYield);
		velUse = cfg.getBoolean(Defaults.cfgVelUse);
		velMin = (float) cfg.getDouble(Defaults.cfgVelMin);
		velCen = (float) cfg.getDouble(Defaults.cfgVelCen);
		velRan = (float) cfg.getDouble(Defaults.cfgVelRan);
		fStraight = (float) cfg.getDouble(Defaults.cfgFStraight);
		velOnPrime = cfg.getBoolean(Defaults.cfgVelOnPrime);
		thresholdTntDirect = cfg.getDouble(Defaults.cfgThresholdTntDirect);
		
		if ( maxRadius > Defaults.radiusLock) maxRadius = Defaults.radiusLock; // safety check
		
		initBlockIds();
		for (Integer i : Defaults.getIdList(cfg, Defaults.cfgIgnore)){
			ignore[i] = !invertIgnored;
		}
		ConfigurationSection sec = cfg.getConfigurationSection(Defaults.cfgResistence);
		for (String key : sec.getKeys(false)){
			if ( "default".equalsIgnoreCase(key)) continue;
			float val = (float) cfg.getDouble(Defaults.cfgResistence+"."+key+".value", 1.0);
			for ( Integer i : Defaults.getIdList(cfg, Defaults.cfgResistence+"."+key+".ids")){
				resistance[i] = val;
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

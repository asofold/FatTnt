package me.asofold.bukkit.fattnt.config;


/**
 * Solely for configuration paths !
 * @author mc_dev
 *
 */
public class Path {

	// multipliers:
	// TODO: maybe split this section.
	public static final String mult = "multiplier";
	public static final String multRadius = mult+".radius";
	public static final String multDamage = mult+".damage";
	public static final String multEntityRadius = mult + ".entity-radius";
	public static final String multMaxPath = mult+".max-path";
	public static final String multProjectiles = mult+".projectiles";
	public static final String fStraight = mult+".straight";
	
	// explosion/propagation settings:
	public static final String passthrough= "passthrough";
	public static final String defaultPassthrough= passthrough+".default";
	public static final String resistence = "resistence";
	public static final String defaultResistence = resistence+".default";
	public static final String radius = "radius";
	public static final String maxRadius = radius+".max";
	public static final String randRadius = radius+".random"; // UNUSED
	public static final String entities= "entities";
	public static final String damagePropagate = "propagate-damage";
	
	// block effects:
	public static final String yield = "yield";
	public static final String stepPhysics= "step-physics";
	
	// tnt specific
	public static final String minPrime = "min-prime";
	public static final String maxPrime = "max-prime";
	public static final String cthresholdTntDirect = "tnt.thresholds.direct-explode"; // UNUSED
	
	// entity effects:
	public static final String entityYield = "entity-yield";
	public static final String itemTnt = "item-tnt";
	public static final String itemArrows = "item-arrows";
	public static final String maxItems = "max-items";
	public static final String projectiles = "projectiles";
	
	// distance effect (damage)
	public static final String distanceDamage = "distance-damage";
	public static final String useDistanceDamage = distanceDamage + ".use";
	public static final String simpleDistanceDamage = distanceDamage + ".simple";
	public static final String multEntityDistance = distanceDamage + ".mult";
	
	// armor:
	public static final String armor = "armor";
	public static final String armorUseDamage = armor+".use-damage";
	public static final String armorMultDamage = armor+".mult-damage";
	public static final String armorBaseDepletion = armor+".base-depletion";
	
	// velocity:
	public static final String vel = "velocity";
	public static final String velUse = vel+".use";
	public static final String velMin = vel+".min";
	public static final String velCen= vel+".center";
	public static final String velRan = vel+".random";
	public static final String velOnPrime = vel+".tnt-primed";
	public static final String velCap = vel+".cap";

}

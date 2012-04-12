package me.asofold.bukkit.fattnt.config;


/**
 * Solely for configuration paths !
 * @author mc_dev
 *
 */
public class Path {

	// multipliers:
	// TODO: maybe split this section.
	public static final String cfgMult = "multiplier";
	public static final String cfgMultRadius = cfgMult+".radius";
	public static final String cfgMultDamage = cfgMult+".damage";
	public static final String cfgMultEntityRadius = cfgMult + ".entity-radius";
	public static final String cfgMultMaxPath = cfgMult+".max-path";
	public static final String cfgMultProjectiles = cfgMult+".projectiles";
	public static final String cfgFStraight = cfgMult+".straight";
	
	// explosion/propagation settings:
	public static final String cfgPassthrough= "passthrough";
	public static final String cfgDefaultPassthrough= cfgPassthrough+".default";
	public static final String cfgResistence = "resistence";
	public static final String cfgDefaultResistence = cfgResistence+".default";
	public static final String cfgRadius = "radius";
	public static final String cfgMaxRadius = cfgRadius+".max";
	public static final String cfgRandRadius = cfgRadius+".random"; // UNUSED
	public static final String cfgEntities= "entities";
	public static final String cfgDamagePropagate = "propagate-damage";
	
	// block effects:
	public static final String cfgYield = "yield";
	public static final String cfgStepPhysics= "step-physics";
	
	// tnt specific
	public static final String cfgMinPrime = "min-prime";
	public static final String cfgMaxPrime = "max-prime";
	public static final String cfgThresholdTntDirect = "tnt.thresholds.direct-explode"; // UNUSED
	
	// entity effects:
	public static final String cfgEntityYield = "entity-yield";
	public static final String cfgItemTnt = "item-tnt";
	public static final String cfgItemArrows = "item-arrows";
	public static final String cfgMaxItems = "max-items";
	public static final String cfgProjectiles = "projectiles";
	
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

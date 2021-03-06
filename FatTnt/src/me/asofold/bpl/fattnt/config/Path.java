package me.asofold.bpl.fattnt.config;


/**
 * Solely for configuration paths !
 * @author mc_dev
 *
 */
public class Path {
	public static final char sep = '.';
	
	
	// Greedy flags:
	public static final String handleExplosions = "handle-explosions";
	public static final String preventExplosions = "prevent-explosions";
	public static final String preventOtherExplosions = "prevent-other-explosions";
	
	// multipliers:
	// TODO: maybe split this section"+ sep + "
	public static final String mult = "multiplier";
	public static final String multRadius = mult +  sep + "radius";
	public static final String multDamage = mult +  sep + "damage";
	public static final String multEntityRadius = mult + ""+ sep + "entity-radius";
	public static final String multMaxPath = mult +  sep + "max-path";
	public static final String multProjectiles = mult +  sep + "projectiles";
	public static final String fStraight = mult +  sep + "straight";
	
	// explosion/propagation settings:
	public static final String passthrough= "passthrough";
	public static final String defaultPassthrough= passthrough +  sep + "default";
	public static final String resistance = "resistance";
	public static final String defaultResistance = resistance +  sep + "default";
	public static final String radius = "radius";
	public static final String maxRadius = radius +  sep + "max";
	public static final String randRadius = radius +  sep + "random"; // UNUSED
	public static final String explodingEntities= "exploding-entities";
	public static final String damagePropagate = "propagate-damage";
	public static final String damagePreventBlocks = "prevent-block-damage";
	
	// confine:
	public static final String confine = "confine";
	public static final String confinePriority = confine +  sep + "priority";
	public static final String confineEnabled = confine +  sep + "enabled";
	public static final String confineYMin = confine +  sep + "y-min";
	public static final String confineYMax = confine +  sep + "y-max";
	
	// block effects:
	public static final String yield = "yield";
	public static final String stepPhysics= "step-physics";
	
	// tnt specific
	public static final String sparePrimed = "spare-primed";
	public static final String minPrime = "min-prime";
	public static final String maxPrime = "max-prime";
	public static final String cthresholdTntDirect = "tnt"+ sep + "thresholds"+ sep + "direct-explode"; // UNUSED
	
	// entity effects:
	public static final String entityYield = "entity-yield";
	public static final String itemTnt = "item-tnt";
	public static final String itemArrows = "item-arrows";
	public static final String maxItems = "max-items";
	public static final String projectiles = "projectiles";
	
	// distance effect (damage)
	public static final String distanceDamage = "distance-damage";
	public static final String useDistanceDamage = distanceDamage + ""+ sep + "use";
	public static final String simpleDistanceDamage = distanceDamage + ""+ sep + "simple";
	public static final String multEntityDistance = distanceDamage + ""+ sep + "mult";
	
	// armor:
	public static final String armor = "armor";
	public static final String armorUseDamage = armor +  sep + "use-damage";
	public static final String armorMultDamage = armor +  sep + "mult-damage";
	public static final String armorBaseDepletion = armor +  sep + "base-depletion";
	
	// velocity:
	public static final String vel = "velocity";
	public static final String velUse = vel +  sep + "use";
	public static final String velMin = vel +  sep + "min";
	public static final String velCen= vel +  sep + "center";
	public static final String velRan = vel +  sep + "random";
	public static final String velOnPrime = vel +  sep + "tnt-primed";
	public static final String velCap = vel +  sep + "cap";
	
	
	// other / general
	public static final String priority = "priority";
	public static final String worldSettings = "world-settings";
	
	// Scheduler settings:
	public static final String sched = "scheduler";
	public static final String schedExplosions = sched + sep + "explosions";
	public static final String schedEntities = sched + sep + "entities";
	public static final String schedItems = sched + sep + "items";
	public static final String process = "process";
	public static final String store = "store";
	public static final String chunkSize = "chunk-size"; 
	public static final String maxTotal = "max-total";
	public static final String maxNanos = "max-nanos";
	public static final String maxChunk = "max-chunk";
	public static final String maxMillis = "max-millis";
	
	// Scheduler use flags (Explosion settings):
	public static final String schedExplosionsUse = "schedule-explosions";
	public static final String schedEntitiesUse = "schedule-entities";
	public static final String schedItemsUse = "schedule-items";
	

}

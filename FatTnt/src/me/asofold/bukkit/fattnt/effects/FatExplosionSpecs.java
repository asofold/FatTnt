package me.asofold.bukkit.fattnt.effects;

import org.bukkit.entity.EntityType;

/**
 * Carry information specific to FetTnt explosions.
 * Intended for use with events.
 * @author mc_dev
 *
 */
public class FatExplosionSpecs {
	
	
	
	private final EntityType entityType;

	public FatExplosionSpecs(EntityType entityType){
		this.entityType = entityType;
	}
	
	@ Override
	public FatExplosionSpecs clone(){
		return new FatExplosionSpecs(entityType);
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
}

package me.asofold.bpl.fattnt.effects;

import org.bukkit.entity.EntityType;

/**
 * Carry information specific to FetTnt explosions.
 * Intended for use with events.
 * @author mc_dev
 *
 */
public class FatExplosionSpecs {
	
	private final String worldName;
	private final EntityType entityType;

	public FatExplosionSpecs(String worldName, EntityType entityType){
		this.worldName = worldName;
		this.entityType = entityType;
	}
	
	@ Override
	public FatExplosionSpecs clone(){
		return new FatExplosionSpecs(worldName, entityType);
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	public String getWorldName(){
		return worldName;
	}
}

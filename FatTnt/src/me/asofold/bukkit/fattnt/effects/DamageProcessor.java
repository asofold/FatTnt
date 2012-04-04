package me.asofold.bukkit.fattnt.effects;

import me.asofold.bukkit.fattnt.config.Settings;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Auxiliary class for processing damage according to settings.<br>
 * This class is not meant for throwing events, but to actually deal out the damage.<br>
 * (This might change with time, if Bukkit API changes to have other events for armor, for instance.)
 * @author mc_dev
 *
 */
public class DamageProcessor {
	Settings settings;
	
	public DamageProcessor(Settings settings){
		this.settings = settings;
	}
	
	/**
	 * Deal the damage to the entity.
	 * (might be aiming at explosions currently)
	 * @param event
	 * @return damage dealt, 0 if none or if not applicable.
	 */
	public int damageEntity(EntityDamageEvent event) {
		int damage = event.getDamage();
		if ( damage == 0) return 0;
		Entity entity = event.getEntity();
		if (entity == null) return 0; // impossible ?
		if (entity.isDead()) return 0;
		EntityType type = entity.getType();
		entity.setLastDamageCause(event); 
		if ( type.isAlive()){
			// TODO: armor !
			// TODO: set damager if possible. [Needs EntityDamageByEntityEvent]
			LivingEntity living = (LivingEntity) entity;
			living.damage(damage);
		} 
		else{
			// TODO: some stuff with different entity types (vehicles, items, paintings).
			// TODO: maybe some destruction chance !
			damage = 0;
		}
		
		return damage;
	}

}

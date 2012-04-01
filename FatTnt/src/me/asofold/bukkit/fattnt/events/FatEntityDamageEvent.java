package me.asofold.bukkit.fattnt.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class FatEntityDamageEvent extends EntityDamageEvent {

	public FatEntityDamageEvent(Entity damagee, DamageCause cause, int damage) {
		super(damagee, cause, damage);
	}

}

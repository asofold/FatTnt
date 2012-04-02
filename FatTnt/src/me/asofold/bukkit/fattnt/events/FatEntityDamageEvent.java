package me.asofold.bukkit.fattnt.events;

import me.asofold.bukkit.fattnt.effects.FatExplosionSpecs;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class FatEntityDamageEvent extends EntityDamageEvent implements FatDamageEvent {

	private final FatExplosionSpecs specs;

	public FatEntityDamageEvent(Entity damagee, DamageCause cause, int damage, FatExplosionSpecs specs) {
		super(damagee, cause, damage);
		this.specs = specs;
	}

	@Override
	public FatExplosionSpecs getExplosionSpecs() {
		return specs;
	}

}

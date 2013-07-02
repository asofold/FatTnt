package me.asofold.bpl.fattnt.events;

import me.asofold.bpl.fattnt.effects.FatExplosionSpecs;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class FatEntityDamageEvent extends EntityDamageEvent implements FatDamageEvent {

	private final FatExplosionSpecs specs;

	@SuppressWarnings("deprecation")
	public FatEntityDamageEvent(Entity damagee, DamageCause cause, double damage, FatExplosionSpecs specs) {
		super(damagee, cause, (int) Math.round(damage));
		this.specs = specs;
	}

	@Override
	public FatExplosionSpecs getExplosionSpecs() {
		return specs;
	}

}

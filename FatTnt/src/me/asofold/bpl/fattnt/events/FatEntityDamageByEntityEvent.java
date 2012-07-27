package me.asofold.bpl.fattnt.events;

import me.asofold.bpl.fattnt.effects.FatExplosionSpecs;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FatEntityDamageByEntityEvent extends EntityDamageByEntityEvent implements FatDamageEvent{

	private final FatExplosionSpecs specs;

	public FatEntityDamageByEntityEvent(Entity damager, Entity damagee,
			DamageCause cause, int damage, FatExplosionSpecs specs) {
		super(damager, damagee, cause, damage);
		this.specs = specs;
	}

	@Override
	public FatExplosionSpecs getExplosionSpecs() {
		return specs;
	}

}

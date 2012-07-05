package me.asofold.bukkit.fattnt.effects;

import java.util.HashMap;
import java.util.Map;

import me.asofold.bukkit.fattnt.config.ExplosionSettings;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Auxiliary class for processing damage according to settings.<br>
 * This class is not meant for throwing events, but to actually deal out the damage.<br>
 * (This might change with time, if Bukkit API changes to have other events for armor, for instance.)
 * @author mc_dev
 *
 */
public class DamageProcessor {
	
	Map<Material, Integer> shield = new HashMap<Material, Integer>(); 
	
	public DamageProcessor(){

		initShield();
	}
	
	/**

	 */
	private void initShield() {
		shield.clear();
		
		// leather:
		shield.put(Material.LEATHER_HELMET, 1);
		shield.put(Material.LEATHER_CHESTPLATE, 3);
	    shield.put(Material.LEATHER_LEGGINGS, 2);
		shield.put(Material.LEATHER_BOOTS, 1);
		
		// gold:
		shield.put(Material.GOLD_HELMET, 2);
		shield.put(Material.GOLD_CHESTPLATE, 5);
	    shield.put(Material.GOLD_LEGGINGS, 3);
		shield.put(Material.GOLD_BOOTS, 1);
		
		// chainmail:
		shield.put(Material.CHAINMAIL_HELMET, 2);
		shield.put(Material.CHAINMAIL_CHESTPLATE, 5);
	    shield.put(Material.CHAINMAIL_LEGGINGS, 4);
		shield.put(Material.CHAINMAIL_BOOTS, 1);
		
		// iron:
		shield.put(Material.IRON_HELMET, 2);
		shield.put(Material.IRON_CHESTPLATE, 6);
	    shield.put(Material.IRON_LEGGINGS, 5);
		shield.put(Material.IRON_BOOTS, 2);
		
		// diamond:
		shield.put(Material.DIAMOND_HELMET, 3);
		shield.put(Material.DIAMOND_CHESTPLATE, 8);
	    shield.put(Material.DIAMOND_LEGGINGS, 6);
		shield.put(Material.DIAMOND_BOOTS, 3);
	}


	/**
	 * Deal the damage to the entity.
	 * (might be aiming at explosions only, currently)
	 * @param event
	 * @return damage dealt, 0 if none or if not applicable.
	 */
	public int damageEntity(EntityDamageEvent event, ExplosionSettings settings) {
		int damage = event.getDamage();
		if ( damage == 0) return 0; // TODO: ret-hink this
		Entity entity = event.getEntity();
		if (entity == null) return 0; // impossible ?
		if (entity.isDead()) return 0;
		EntityType type = entity.getType();
		DamageCause cause = event.getCause();
		// TODO: check if in boat / minecart !
		if ( type.isAlive()){
			LivingEntity living = (LivingEntity) entity;
			int[] armorDamage = getArmorDamage(living, type, cause, damage, settings);
			if (armorDamage != null ) damage = armorDamage[0];
			
			final int noDamageTicks = living.getNoDamageTicks() ;
			if (noDamageTicks>0){ 
				// TODO: allow settings: ignore-no-damage-ticks
				// TODO: allow settings: still apply armor damage
				EntityDamageEvent oldDamage = living.getLastDamageCause();
				if ( oldDamage == null){
					// TODO: check settings ?
				} else{
					if (oldDamage.getDamage() >= damage) return 0;
					// TODO: damage should probably be reduced !
				}
			}
			if ( armorDamage != null) applyArmorDamage(living, armorDamage);
			// TODO: set damager if possible. [Needs EntityDamageByEntityEvent]
			living.setLastDamageCause(event);
			living.damage(damage);
			living.setNoDamageTicks(living.getMaximumNoDamageTicks()); // TODO: Is this correct?
		} 
		else{
			// TODO: some stuff with different entity types (vehicles, items, paintings).
			// TODO: maybe some destruction chance !
			damage = 0;
		}
		
		return damage;
	}

	/**
	 * Just find out how much to damage each piece of armor.
	 * According to settings + check enchantments etc.
	 * @param cause
	 * @param damage
	 * @return null or an Array with to be dealt damage, then durability losses for: helmet, chestplate, leggings, boots.
	 */
	public int[] getArmorDamage(LivingEntity living, EntityType damager, DamageCause cause, int damage, ExplosionSettings settings) {
		if ( cause != DamageCause.ENTITY_EXPLOSION) return null; // current limit.
		if ( living instanceof HumanEntity){
			int base = settings.armorBaseDepletion; // TODO: entity specific ?
			if (settings.armorUseDamage){
				base += (int) (settings.armorMultDamage * (float) damage);
			}
			int[] out = new int[]{damage, base, base, base, base};
			// TODO: reduce the damage for enchanted parts.
			HumanEntity human = (HumanEntity) living;
			ItemStack[] stacks = human.getInventory().getArmorContents();
			int shield = 0;
			int enchShield = 0;
			for ( int i=0; i<stacks.length; i++){
				ItemStack stack = stacks[i];
				if ( stack == null) continue;
				shield += getShieldValue(i, stack, cause);
				enchShield = getEnchantmentShieldValue(i, stack , cause);
			}
			// shield effect (armor standard):
			if (shield >0) damage = damage*(100-4*shield)/100;
			// enchantment effect: 
			if (enchShield>0) damage = damage*(100-4*Math.max(enchShield, 20))/100;
			// TODO: minimum damage 
			out[0] = damage;
			return out;
		}
		else return null;
		
	}
	
	private int getEnchantmentShieldValue(int place, ItemStack stack, DamageCause cause) {
		if (stack == null) return 0;
		Map <Enchantment, Integer> ench = stack.getEnchantments();
		if (ench!=null && cause == DamageCause.ENTITY_EXPLOSION){
			Integer level = ench.get(Enchantment.PROTECTION_EXPLOSIONS);
			if ( level != null){
				int f = (6*level*level)/2*(50 +ExplosionManager.random.nextInt(51)) /100;
				// TODO: varies with i ?
				return f;
			}
		}
		return 0;
	}

	public int getShieldValue(int place, ItemStack stack, DamageCause cause){
		if (stack == null) return 0;
		Material mat = stack.getType();
		Integer s = shield.get(mat);
		if (s == null) return 0;
		int max = mat.getMaxDurability();
		s = s*(max-stack.getDurability())/max;
		return s;
	}
	
	/**
	 * Just damage the armor with the result of getArmorDamage.
	 * @param living
	 */
	public void applyArmorDamage( LivingEntity living, int[] armorDamage){
		if (armorDamage == null) return;
		if (living instanceof HumanEntity){
			HumanEntity human = (HumanEntity) living;
			PlayerInventory inv =  human.getInventory();
			ItemStack[] stacks = inv.getArmorContents();
			boolean changed= false;
			for ( int i = 0; i<stacks.length; i++){
				ItemStack stack = stacks[i];
				if ( stack == null || stack.getType() == Material.AIR) continue;
				int max  = stack.getType().getMaxDurability();
				int dur = stack.getDurability(); 
				if (dur + armorDamage[i+1] >max){
					// TODO: destroy it
					stacks[i] = null;
				} else{
					stack.setDurability((short) (dur+armorDamage[i+1]));
					changed = true;
				}
			}
			if (changed) inv.setArmorContents(stacks);
		}
	}

}

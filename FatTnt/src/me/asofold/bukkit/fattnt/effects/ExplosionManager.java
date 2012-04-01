package me.asofold.bukkit.fattnt.effects;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import me.asofold.bukkit.fattnt.config.Defaults;
import me.asofold.bukkit.fattnt.config.Settings;
import me.asofold.bukkit.fattnt.events.FatEntityDamageEvent;
import me.asofold.bukkit.fattnt.events.FatEntityExplodeEvent;
import me.asofold.bukkit.fattnt.propagation.Propagation;
import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

/**
 * Static method utility ( currently).
 * @author mc_dev
 *
 */
public class ExplosionManager {
	
	public static final Random random = new Random(System.currentTimeMillis()-1256875);
	
	/**
	 * This does not create the explosion effect !
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius
	 * @param fire
	 * @param explEntity
	 * @param entityType
	 * @param nearbyEntities
	 * @param settings
	 * @param propagation
	 */
	public static void applyExplosionEffects(World world, double x, double y, double z, float realRadius, boolean fire, Entity explEntity, EntityType entityType,
			List<Entity> nearbyEntities, Settings settings, Propagation propagation) {
		if ( realRadius > settings.maxRadius){
			// TODO: setttings ?
			realRadius = settings.maxRadius;
		} else if (realRadius == 0.0f) return;
		Server server = Bukkit.getServer();
		PluginManager pm = server.getPluginManager();
		
		// blocks:
		List<Block> affected = propagation.getExplodingBlocks(world , x, y, z, realRadius);
		EntityExplodeEvent exE = new FatEntityExplodeEvent(explEntity, new Location(world,x,y,z), affected, settings.defaultYield );
		pm.callEvent(exE);
		if (exE.isCancelled()) return;
		float yield = exE.getYield();
//		final List<block> directExplode = new LinkedList<block>(); // if set in config. - maybe later (split method to avoid recursion !)
		for ( Block block : exE.blockList()){
			if (block.getType() == Material.TNT){
				block.setTypeId(0, true);
				Location loc = block.getLocation().add(Defaults.vCenter);
				final float effRad = propagation.getStrength(loc); // effective strength/radius
//				if ( effRad > thresholdTntDirect){
//					directExplode.add(block);
//					continue;
//				}
				// do spawn tnt-primed
				try{
					Entity entity = world.spawn(loc, CraftTNTPrimed.class);
					if (entity == null) continue;
					if ( !(entity instanceof TNTPrimed)) continue;
					if ( effRad == 0.0f) continue; // not affected
					if (settings.velOnPrime) addRandomVelocity(entity, loc, x,y,z, effRad, realRadius, settings);
				} catch( Throwable t){
					// maybe later log
				}
				continue;
			}
			// All other blocks:
			Collection<ItemStack> drops = block.getDrops();
			for (ItemStack drop : drops){
				if ( random.nextFloat()<=yield){
					Location loc = block.getLocation().add(Defaults.vCenter);
					Item item = world.dropItemNaturally(loc, drop.clone());
					if (item==null) continue;
					//addRandomVelocity(item, loc, x,y,z, realRadius);
				}
			}
			block.setTypeId(0, true); // TODO: evaluate if still spazzing appears (if so: use false, and then iterate again for applying physics after block changes).
		}
		
		// entities:
		for ( Entity entity : nearbyEntities){
			// test damage:
			final Location loc = entity.getLocation();
			final float effRad = propagation.getStrength(loc); // effective strength/radius
			if ( effRad == 0.0f) continue; // not affected
			addRandomVelocity(entity, loc, x,y,z, effRad, realRadius, settings);
			if (settings.sparePrimed && (entity instanceof TNTPrimed)) continue;
			// TODO: damage entities according to type
			int damage = 1 + (int) (effRad*settings.damageMultiplier) ;
			// TODO: take into account armor, enchantments and such?
			EntityDamageEvent event = new FatEntityDamageEvent(entity, DamageCause.ENTITY_EXPLOSION, damage);
			pm.callEvent(event);
			if (!event.isCancelled()){
				Utils.damageEntity(event);
			}
		}
		
		
	}
	
	/**
	 * 
	 * @param entity
	 * @param loc
	 * @param x
	 * @param y
	 * @param z
	 * @param part part of radius (effective)
	 * @param max max radius
	 */
	private static void addRandomVelocity(Entity entity, Location loc, double x, double y,
			double z, float part, float max, Settings settings) {
		// TODO: make some things configurable, possible entity dependent and !
		if (!settings.velUse) return;
		Vector v = entity.getVelocity();
		Vector fromCenter = new Vector(loc.getX()-x,loc.getY()-y,loc.getZ()-z).normalize();
		Vector rv = v.add((fromCenter.multiply(settings.velMin+random.nextFloat()*settings.velCen)).add(Vector.getRandom().multiply(settings.velRan)).multiply(part/max));
		if (entity instanceof LivingEntity) ((LivingEntity) entity).setVelocity(rv); 
		else if (entity instanceof TNTPrimed) ((TNTPrimed) entity).setVelocity(rv);
		else entity.setVelocity(rv);
	}
}

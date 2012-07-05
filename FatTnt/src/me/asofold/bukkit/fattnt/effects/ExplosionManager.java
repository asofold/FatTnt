package me.asofold.bukkit.fattnt.effects;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import me.asofold.bukkit.fattnt.FatTnt;
import me.asofold.bukkit.fattnt.config.ExplosionSettings;
import me.asofold.bukkit.fattnt.events.FatEntityDamageEvent;
import me.asofold.bukkit.fattnt.events.FatEntityExplodeEvent;
import me.asofold.bukkit.fattnt.propagation.Propagation;
import me.asofold.bukkit.fattnt.scheduler.ScheduledArrowSpawn;
import me.asofold.bukkit.fattnt.scheduler.ScheduledItemSpawn;
import me.asofold.bukkit.fattnt.scheduler.ScheduledTntSpawn;
import me.asofold.bukkit.fattnt.scheduler.SchedulerSet;
import me.asofold.bukkit.fattnt.stats.Stats;
import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Vehicle;
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
	
	private static Stats stats = null;
	
	
	/**
	 * This does not create the explosion effect (FX) but calculates and applies the world changes !
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius
	 * @param fire
	 * @param explEntity 
	 * @param entityType
	 * @param nearbyEntities can be null
	 * @param settings
	 * @param propagation
	 */
	public static boolean applyExplosionEffects(World world, double x, double y, double z, float realRadius, boolean fire, Entity explEntity, EntityType entityType,
			List<Entity> nearbyEntities, float damageMultiplier, ExplosionSettings settings, Propagation propagation, DamageProcessor damageProcessor, SchedulerSet schedulers) {
		if ( realRadius > settings.maxRadius){
			// TODO: settings ?
			realRadius = settings.maxRadius;
		} else if (realRadius == 0.0f) return false;
		PluginManager pm = Bukkit.getPluginManager();
		
		// blocks:
		long ns = System.nanoTime();
		List<Block> affected = propagation.getExplodingBlocks(world , x, y, z, realRadius, settings);
		stats.addStats(FatTnt.statsGetBlocks, System.nanoTime()-ns); // Time measurement
		stats.addStats(FatTnt.statsBlocksCollected, affected.size()); // counting average number of collected blocks.
		stats.addStats(FatTnt.statsStrength, (long) realRadius); // just counting average explosion strength !
		ns = System.nanoTime();
		FatExplosionSpecs specs = new FatExplosionSpecs(world.getName(), Utils.usedEntityType(explEntity, entityType));
		EntityExplodeEvent exE = new FatEntityExplodeEvent(explEntity, new Location(world,x,y,z), affected, settings.yield , specs);
		pm.callEvent(exE);
		stats.addStats(FatTnt.statsExplodeEvent, System.nanoTime()-ns);
		if (exE.isCancelled()) return false;
		// block effects:
		ns = System.nanoTime();
		applyBlockEffects(world, x, y, z, realRadius, exE.blockList(), exE.getYield(), settings, propagation, specs, schedulers);
		stats.addStats(FatTnt.statsApplyBlocks, System.nanoTime()-ns);
		// entities:
		if ( nearbyEntities != null){
			ns = System.nanoTime();
			applyEntityEffects(world, x, y, z, realRadius, nearbyEntities, damageMultiplier, settings, propagation, specs, damageProcessor, schedulers);
			stats.addStats(FatTnt.statsApplyEntities, System.nanoTime()-ns);
		}
		return true;
	}
	
	/**
	 * Block manipulations for explosions.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius
	 * @param blocks
	 * @param defaultYield
	 * @param settings
	 * @param propagation
	 * @param specs 
	 * @param schedulers 
	 */
	public static void applyBlockEffects(World world, double x, double y, double z, float realRadius, List<Block> blocks, float defaultYield, ExplosionSettings settings, Propagation propagation, FatExplosionSpecs specs, SchedulerSet schedulers){
//		final List<block> directExplode = new LinkedList<block>(); // if set in config. - maybe later (split method to avoid recursion !)
		final int tntId = Material.TNT.getId();
		for (final Block block : blocks){
			int id = propagation.getTypeId(block.getX(), block.getY(), block.getZ());
			if (id == -1) id = block.getTypeId();
			if (settings.stepPhysics) block.setTypeId(0, false);
			else block.setTypeId(0, true);
			if (id == tntId){
				Location loc = blockCenter(world, block);
				if (settings.scheduleEntities) schedulers.spawnEntities.addEntry(getScheduledTnt(world, x, defaultYield, z, loc, realRadius, settings, propagation));
				else addTNTPrimed(world, x, y, z, loc, realRadius, settings, propagation);
			}
			else{
				// All other blocks:
				Collection<ItemStack> drops = block.getDrops();
				for (ItemStack drop : drops){
					// TODO: another way than yield to control number ?
					if (random.nextFloat()<=defaultYield){
						// TODO: settings !
						//getSomeRandomVelocity(item, loc, x,y,z, realRadius);
						if (settings.scheduleItems){
							schedulers.spawnItems.addEntry(new ScheduledItemSpawn(block, drop));
						}
						else{
							Location loc = blockCenter(world, block);
							world.dropItemNaturally(loc, drop); // .clone());
						}
						
					}
				}
			}
		}
		if (settings.stepPhysics){
			for ( Block block : blocks){
				block.getState().update();
			}
		}
	}
	
	public static final Location blockCenter(final World world, final Block block){
		return new Location(world, 0.5 + (double) block.getX(), 0.5 + (double) block.getY(), 0.5 + (double) block.getZ());
	}
	
	public static TNTPrimed addTNTPrimed(World world, double x, double y, double z,
			Location loc, float realRadius, ExplosionSettings settings, Propagation propagation) {
		final float effRad = propagation.getStrength(loc); // effective strength/radius
		int fuseTicks = 80; 
		if (settings.minPrime > 0  && settings.maxPrime > 0){
			
			if ( settings.minPrime <settings.maxPrime) fuseTicks = settings.minPrime + random.nextInt((settings.maxPrime-settings.minPrime+1));
			else fuseTicks = Math.max(settings.minPrime, settings.maxPrime);
		}
		Vector v = null; // not affected
		if (settings.velOnPrime) v = getRandomVelocityToAdd(EntityType.PRIMED_TNT, loc, x,y,z, effRad, realRadius, settings);
		return spawnTNTPrimed(world, loc, fuseTicks, v);
	}
	
	public static ScheduledTntSpawn getScheduledTnt(World world, double x, double y, double z,
			Location loc, float realRadius, ExplosionSettings settings, Propagation propagation) {
		final float effRad = propagation.getStrength(loc); // effective strength/radius
		int fuseTicks = 80; 
		if (settings.minPrime > 0  && settings.maxPrime > 0){
			
			if ( settings.minPrime <settings.maxPrime) fuseTicks = settings.minPrime + random.nextInt((settings.maxPrime-settings.minPrime+1));
			else fuseTicks = Math.max(settings.minPrime, settings.maxPrime);
		}
		Vector v = null; // not affected
		if (settings.velOnPrime) v = getRandomVelocityToAdd(EntityType.PRIMED_TNT, loc, x,y,z, effRad, realRadius, settings);
		return new ScheduledTntSpawn(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), fuseTicks, v);
	}
	
	/**
	 * Just spawn it.
	 * @param world
	 * @param loc
	 * @return
	 */
	public static TNTPrimed spawnTNTPrimed(World world, Location loc, int fuseTicks, Vector velocity){
		try{
			TNTPrimed tnt = world.spawn(loc, TNTPrimed.class);
			if (tnt == null) return null;
			tnt.setVelocity(velocity);
			try{
				tnt.setFuseTicks(fuseTicks);
			}
			catch(Throwable t){};
			return tnt;
		} catch( Throwable t){
			// maybe later log
			return null;
		}
	}
	
	public static Arrow addArrow(World world, double x, double y,
			double z, Location loc, float realRadius, ExplosionSettings settings,
			Propagation propagation, SchedulerSet schedulers) {
		final float effRad = propagation.getStrength(loc); // effective strength/radius
//		if ( effRad > thresholdTntDirect){
//			directExplode.add(block);
//			continue;
//		}
		// do spawn tnt-primed
		if ( effRad == 0.0f) return null; // not affected
		Vector fromCenter = new Vector(loc.getX()-x,loc.getY()-y,loc.getZ()-z).normalize().multiply(0.5);
		if ( loc.getBlock().getRelative(BlockFace.DOWN).getType()!=Material.AIR) fromCenter.setY(Math.abs(fromCenter.getY())+0.7);
		
		if (settings.velOnPrime) fromCenter = addRandomVelocity(EntityType.ARROW, fromCenter, loc, x,y,z, effRad, realRadius, settings);
		if (settings.scheduleEntities){
			schedulers.spawnEntities.addEntry(new ScheduledArrowSpawn(loc, fromCenter));
			return null; // hmm
		}
		else{
			Arrow arrow = spawnArrow(world, loc);
			if ( arrow == null) return null;
			arrow.setVelocity(fromCenter);
			return arrow;
		}
	}

	public  static Arrow spawnArrow(World world, Location loc) {
		Arrow arrow = world.spawn(loc, CraftArrow.class);
		if (arrow == null) return null;
		return arrow;
	}
	
	public static Arrow spawnArrow(World world, Location location, Vector velocity) {
		Arrow arrow = spawnArrow(world, location);
		if (arrow == null) return null;
		if (velocity != null) arrow.setVelocity(velocity);
		return arrow;
	}

	/**
	 * Entity manipulations for explosions.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius
	 * @param nearbyEntities
	 * @param damageMultiplier
	 * @param settings
	 * @param propagation
	 * @param specs 
	 * @param damageProcessor 
	 * @param schedulers 
	 */
	public static void applyEntityEffects(World world, double x, double y, double z, float realRadius, List<Entity> nearbyEntities, float damageMultiplier, ExplosionSettings settings, Propagation propagation, FatExplosionSpecs specs, DamageProcessor damageProcessor, SchedulerSet schedulers) {
		if ( realRadius > settings.maxRadius){
			// TODO: settings ?
			realRadius = settings.maxRadius;
		} else if (realRadius == 0.0f) return;
		PluginManager pm = Bukkit.getPluginManager();
		Location expCenter = new Location(world, x, y, z);
		
		float maxD = realRadius * settings.entityRadiusMultiplier;
		
		// entities:
		for ( Entity entity : nearbyEntities){
			// test damage:
			final Location loc = entity.getLocation();
			float effStr = propagation.getStrength(loc); // effective strength/radius
			
			final EntityType entityType = entity.getType();
			boolean isAlive = entityType.isAlive();
			boolean addVelocity = false;
			boolean useDamage = true;
			boolean applyEntityYield = false;
			
			
			
			// ADD DISTANCE DAMAGE ASPECT (currently to effStr)
			if ( settings.simpleDistanceDamage && effStr > 0.0); // ignore distance damage
			else if (isAlive){
				// TODO: maybe also allow for all 
				// TODO: add distance aspect
				if (settings.useDistanceDamage){
					// currently only the simple method:
					if (FatTnt.DEBUG_LOTS) System.out.println("[FatTnt] Checking distance damage...");
					final float d = (float) loc.distance(expCenter);
					// TODO: more precise / efficient (using block coordinates right away, diagonal transition checks, ... )
					if (d < maxD){
						final Vector dir = expCenter.toVector().subtract(loc.toVector()).normalize().multiply(0.3);
						final int max = (int) (maxD*3.0);
						double h = 0.5;
						if ( entity instanceof LivingEntity){
							h = ((LivingEntity) entity).getEyeHeight();
						}
						Location current = loc.clone().add(new Vector(0.0, h ,0.0)); 
						for ( int i = 0 ; i< max; i++){
							int id = world.getBlockTypeIdAt(current);
							if (FatTnt.DEBUG_LOTS) System.out.println("[FatTnt] dist-damage at id: "+id);
							if (!settings.propagateDamage.value[id]) break;
							float str = propagation.getStrength(current);
							if (str > 0.0f){
								// modify effStr according to settings.
								final float ed = (float) loc.distance(current);
								if ( ed <= 0.0f) break;
								effStr += str * settings.entityDistanceMultiplier*(maxD - ed) / maxD;
								if ( FatTnt.DEBUG_LOTS) System.out.println("[FatTnt] Modified effStr at "+ed+ ": "+settings.entityDistanceMultiplier*(maxD - ed) / maxD);
								break;
							}
							current.add(dir);
						}
					}
				}
			}
			
			// normal processing:
			if ( effStr == 0.0f){
				continue; // not affected
			}
			
			
			
			if (settings.sparePrimed && (entity instanceof TNTPrimed)){
				addVelocity = true;
				useDamage = false;
			} 
			else if (isAlive); // just go with settings.
			else if (entity instanceof Item){
				Item item = (Item) entity;
				ItemStack stack = item.getItemStack();
				final Material mat = stack.getType();
				if ( mat == Material.TNT && settings.itemTnt){
					// create primed tnt according to settings
					item.remove();
					for ( int i = 0; i< Math.min(settings.maxItems, stack.getAmount()); i++){
						// TODO: FUSE TICKS !
						if (settings.scheduleExplosions) schedulers.spawnEntities.addEntry(getScheduledTnt(world, maxD, y, z, loc, realRadius, settings, propagation));
						else addTNTPrimed(world, x, y, z, loc.add(new Vector(0.0,0.5,0.0)), realRadius, settings, propagation);
					}
					continue;
				} 
				else if (mat == Material.ARROW && settings.itemArrows){
					item.remove();
					for ( int i = 0; i< Math.min(settings.maxItems, stack.getAmount()); i++){
						addArrow(world, x, y, z, loc, realRadius, settings, propagation, schedulers);
					}
				} 
				// TODO: maybe splash potions !
				else{
					applyEntityYield = true;
				}
				// TODO: either use yield here or let damageEntity decide bout removal !
			} else if (entity instanceof Vehicle){
				// TODO: StorageMinecart ?
				applyEntityYield = true;
			} else if (entity instanceof FallingSand){
				applyEntityYield = true;
			} else if (entity instanceof Projectile){
				if ( settings.projectiles){
					if ( entity instanceof Arrow){
						Arrow arrow = (Arrow) entity;
						if ( arrow.isDead() || arrow.getVelocity().length()<0.25){
							entity.remove();
							addArrow(world, x, y, z, loc, realRadius, settings, propagation, schedulers);
							continue;
						}
					}
					useDamage = false;
					addVelocity = true;
				} else{
					useDamage = false;
					addVelocity = false;
				}
			}
			if (applyEntityYield){
				if ( random.nextFloat()>settings.entityYield) entity.remove();
				continue;
			}
			if (useDamage){
				// TODO: damage entities according to type [currently almost only living entities]
				int damage = 1 + (int) (effStr*settings.damageMultiplier*damageMultiplier); // core damage
				// TODO: add distance damage [maybe above]
				EntityDamageEvent event = new FatEntityDamageEvent(entity, DamageCause.ENTITY_EXPLOSION, damage, specs);
				pm.callEvent(event);
				if (!event.isCancelled()){
					if (damageProcessor.damageEntity(event, settings) > 0){
						// (declined: consider using "effective damage" for stats.)
						// (but:) Only include >0 damage (that might lose some armored players later, but prevents including invalid entities. 
						stats.addStats(FatTnt.statsDamage, damage); 
					}
					addVelocity = true;
				}
			}
			if (addVelocity) entity.setVelocity(addRandomVelocity(entityType, entity.getVelocity(), loc, x,y,z, effStr, realRadius, settings));
		}
	}

	/**
	 * Add velocity according to settings
	 * @param entity
	 * @param loc
	 * @param x
	 * @param y
	 * @param z
	 * @param part part of radius (effective), as with FatTnt.getExplosionStrength
	 * @param max max radius
	 */
	public static Vector addRandomVelocity(EntityType entityType, Vector velocity, Location loc, double x, double y,
			double z, float part, float max, ExplosionSettings settings) {
		return velocity.add(getRandomVelocityToAdd(entityType, loc, x, y, z, part, max, settings));
	}
	
	public static Vector getRandomVelocityToAdd(Entity entity, Location loc, double x, double y,
			double z, float part, float max, ExplosionSettings settings) {
		return getRandomVelocityToAdd((entity==null)?null:entity.getType(), loc, x, y, z, part, max, settings);
	}
	
	public static Vector getRandomVelocityToAdd(EntityType entityType, Location loc, double x, double y,
			double z, float part, float max, ExplosionSettings settings) {
		// TODO: make some things configurable, possible entity dependent and !
		Vector v = new Vector(0,0,0);
		if (!settings.velUse) return v;
		Vector fromCenter = new Vector(loc.getX()-x,loc.getY()-y,loc.getZ()-z).normalize();
		Vector rv = v.add((fromCenter.multiply(settings.velMin+random.nextFloat()*settings.velCen)).add(Vector.getRandom().multiply(settings.velRan)).multiply(part/max));
		if( settings.velCap>0){
			if (rv.lengthSquared()>settings.velCap*settings.velCap) rv = rv.normalize().multiply(settings.velCap);
		}
		if ( entityType == EntityType.ARROW || entityType == EntityType.FIREBALL || entityType == EntityType.SPLASH_POTION || entityType == EntityType.SMALL_FIREBALL || entityType == EntityType.SNOWBALL || entityType == EntityType.THROWN_EXP_BOTTLE) rv = rv.multiply(settings.projectileMultiplier);
		return rv;
	}

	/**
	 * Show the explosion effect in that world, 
	 * currently this simply delegates to create an explosion with radius 0,
	 * later it might add other effects, depending on radius and fire.<br>
	 * This does not explode anything it just creates the effect.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @param fire
	 */
	public static void createExplosionEffect(World world, double x, double y,
			double z, float radius, boolean fire) {
		world.createExplosion(new Location(world,x,y,z), 0.0F); 
	}
	
	public static void setStats(Stats stats){
		ExplosionManager.stats = stats;
	}

	/**
	 * Remove item and put TNTPrimed there, according to settings;
	 * @param item
	 */
	public static TNTPrimed replaceByTNTPrimed(Item item) {
		// TODO: fuse ticks !
		Location loc = item.getLocation().add(new Vector(0.0,0.5,0.0));
		item.remove();
		World world = item.getWorld();
		return spawnTNTPrimed(world, loc, 80, item.getVelocity());
	}
	
	public static ScheduledTntSpawn getScheduledTnt(Item item){
		// TODO: fuse ticks
		Location loc = item.getLocation();
		item.remove();
		return new ScheduledTntSpawn(loc.getWorld(), loc.getX(), loc.getY() + 0.5, loc.getZ(), 80, item.getVelocity().clone());
	}

	public static Entity addTntPrimed(ScheduledTntSpawn spawnTnt) {
		TNTPrimed tnt = spawnTnt.world.spawn(new Location(spawnTnt.world, spawnTnt.x, spawnTnt.y, spawnTnt.z), TNTPrimed.class);
		if (tnt == null) return null;
		if (spawnTnt.getVelocity() != null) tnt.setVelocity(spawnTnt.getVelocity());
		tnt.setFuseTicks(spawnTnt.getFuseTicks());
		return tnt;
	}

	public static Item spawnItem(ScheduledItemSpawn spawnItem) {
		final Vector v = spawnItem.getVelocity();
		if (v == null) return spawnItem.world.dropItemNaturally(spawnItem.getLocation(), spawnItem.getStack());
		Item item = spawnItem.world.dropItem(spawnItem.getLocation(), spawnItem.getStack());
		if (item == null) return null;
		item.setVelocity(v);
		return item;
	}

}

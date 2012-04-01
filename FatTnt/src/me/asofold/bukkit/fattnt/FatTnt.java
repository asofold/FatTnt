package me.asofold.bukkit.fattnt;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import me.asofold.bukkit.fattnt.config.Defaults;
import me.asofold.bukkit.fattnt.config.Settings;
import me.asofold.bukkit.fattnt.propagation.Propagation;
import me.asofold.bukkit.fattnt.propagation.PropagationFactory;
import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Experimental plugin to replace explosions completely.
 * 
 * @author mc_dev
 * @license See project folder, either LICENSE.TXT or fattnt.lists.
 *
 */
public class FatTnt extends JavaPlugin implements Listener {
	

	
//	ExplosionPrimeEvent waitingEP = null;

	
	private final Random random = new Random(System.currentTimeMillis()-1256875);
	
	private final Settings settings = new Settings();
	
	private Propagation propagation = null;
	
	@Override
	public void onEnable() {
		reloadSettings();
		getServer().getPluginManager().registerEvents(this, this);
		System.out.println(Defaults.msgPrefix+getDescription().getFullName()+" is enabled.");
	}
	
	@Override
	public void onDisable() {
		System.out.println(Defaults.msgPrefix+getDescription().getFullName()+" is disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		label = label.toLowerCase();
		if ( !label.equals("fattnt")) return false;
		int len = args.length;
		if (len==1 && args[0].equalsIgnoreCase("reload")){
			if ( !Utils.checkPerm(sender, "fattnt.cmd.reload")) return true;
			reloadSettings();
			Utils.send(sender, "Settings reloaded.");
			return true;
		} 
		else if (len==1 && args[0].equalsIgnoreCase("enable")){
			if ( !Utils.checkPerm(sender, "fattnt.cmd.enable")) return true;
			settings.setHandleExplosions(true);
			Utils.send( sender, "Explosions will be handled by FatTnt."); 
			return true;
		}
		else if (len==1 && args[0].equalsIgnoreCase("disable")){
			if ( !Utils.checkPerm(sender, "fattnt.cmd.disable")) return true;
			settings.setHandleExplosions(false);
			Utils.send( sender, "Explosions are back to default behavior (disregarding other plugins)."); 
			return true;
		}
		return false;
	}

	public void reloadSettings() {
		File file = new File (getDataFolder(), "config.yml");
		boolean exists = file.exists();
		if ( exists) reloadConfig();
		FileConfiguration cfg = getConfig();
		if (Defaults.addDefaultSettings(cfg)) saveConfig();
		else if (!exists) saveConfig();
		settings.applyConfig(cfg);
		// TODO: propagation pbased on config (Factory)
		propagation = PropagationFactory.getPropagation(settings);
	}




	@EventHandler(priority=EventPriority.HIGHEST)
	void onExplosionPrimeLowest(ExplosionPrimeEvent event){
//		waitingEP = null;
		if (!settings.handleExplosions) return;
		else if (event.isCancelled()) return;
		else if (!settings.handledEntities.contains(event.getEntityType())) return;
		// do prepare to handle this explosion:
		event.setCancelled(true);
//		waitingEP = event;
//	}
//	
//	@EventHandler(priority=EventPriority.MONITOR)
//	void onExplosionPrime(ExplosionPrimeEvent event){
//		// check event 
//		if ( waitingEP != event) return;
//		waitingEP = null;
//		// event is to be handled:
//		if ( !event.isCancelled()) event.setCancelled(true); // just in case other plugins mess with this one.
		EntityType type = event.getEntityType();
		Entity entity = event.getEntity();
		Location loc = entity.getLocation().clone();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		if (!entity.isDead()) entity.remove();
		createExplosion(loc.getWorld(), x, y, z, event.getRadius(), event.getFire(), entity, type);
	}
	
	/**
	 * API
	 * @param loc
	 * @param radius As in World.createExplosion
	 * @param fire
	 * @param explEntity
	 * @param entityType May be null, might be unused.
	 */
	public void createExplosion(Location loc, float radius, boolean fire, Entity explEntity, EntityType entityType){
		World world = loc.getWorld();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		createExplosion(world, x, y, z, radius, fire, explEntity, entityType);
	}
	
	/**
	 * API
	 * (called from event handling as well)
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param radius As in World.createExplosion
	 * @param fire
	 * @param explEntity
	 * @param entityType May be null, might be unused.
	 */
	public void createExplosion(World world, double x, double y, double z, float radius, boolean fire, Entity explEntity, EntityType entityType){
		// create a fake explosion
		world.createExplosion(new Location(world,x,y,z), 0.0F); 
		if (radius==0.0f) return;
		// calculate effects
		// WORKAROUND:
		float realRadius = radius*settings.radiusMultiplier;
		List<Entity> nearbyEntities;
		if (explEntity==null) nearbyEntities = Utils.getNearbyEntities(world, x,y,z, realRadius);
		else nearbyEntities = explEntity.getNearbyEntities(realRadius, realRadius, realRadius);
		applyExplosionEffects(world, x, y, z, realRadius, fire, explEntity, entityType, nearbyEntities);
	}

	/**
	 * API
	 * @param loc
	 * @param radius As in World.createExplosion
	 * @param fire
	 */
	public void createExplosion(Location loc, float radius, boolean fire){
		createExplosion(loc, radius, fire, null, null);
	}

	/**
	 * API
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param radius As in World.createExplosion
	 * @param fire
	 */
	public void createExplosion(World world, double x, double y, double z, float radius, boolean fire) {
		createExplosion(world, x, y, z, radius, fire, null, null);
	}
	
	/**
	 * This method only considers the given entities for damage.
	 * API
	 * (used internally)
	 * (this method uses seqMax, such that it should not get manipulated anywhere but inside of getExplodingBlocks)
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius intended to be the block distance rather, so that resistance of 1.0 (AIR) would lead to the full range.
	 * @param fire
	 * @param entityType Causing the explosion.
	 * @param nearbyEntities List of entities that can be affected , should be within radius (x,y,z independently), damage depends on settings.
	 */
	public void applyExplosionEffects(World world, double x, double y, double z, float realRadius, boolean fire, Entity explEntity, EntityType entityType,
			List<Entity> nearbyEntities) {
		if ( realRadius > settings.maxRadius){
			// TODO: setttings ?
			realRadius = settings.maxRadius;
		} else if (realRadius == 0.0f) return;
		Server server = getServer();
		PluginManager pm = server.getPluginManager();
		
		// blocks:
		List<Block> affected = getExplodingBlocks(world , x, y, z, realRadius);
		EntityExplodeEvent exE = new EntityExplodeEvent(explEntity, new Location(world,x,y,z), affected, settings.defaultYield );
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
					if (settings.velOnPrime) addRandomVelocity(entity, loc, x,y,z, effRad, realRadius);
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
			addRandomVelocity(entity, loc, x,y,z, effRad, realRadius);
			if (settings.sparePrimed && (entity instanceof TNTPrimed)) continue;
			// TODO: damage entities according to type
			int damage = 1 + (int) (effRad*settings.damageMultiplier) ;
			// TODO: take into account armor, enchantments and such?
			EntityDamageEvent event = new EntityDamageEvent(entity, DamageCause.ENTITY_EXPLOSION, damage);
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
	private void addRandomVelocity(Entity entity, Location loc, double x, double y,
			double z, float part, float max) {
		// TODO: make some things configurable, possible entity dependent and !
		if (!settings.velUse) return;
		Vector v = entity.getVelocity();
		Vector fromCenter = new Vector(loc.getX()-x,loc.getY()-y,loc.getZ()-z).normalize();
		Vector rv = v.add((fromCenter.multiply(settings.velMin+random.nextFloat()*settings.velCen)).add(Vector.getRandom().multiply(settings.velRan)).multiply(part/max));
		if (entity instanceof LivingEntity) ((LivingEntity) entity).setVelocity(rv); 
		else if (entity instanceof TNTPrimed) ((TNTPrimed) entity).setVelocity(rv);
		else entity.setVelocity(rv);
	}

	/**
	 * Get exploding blocks for a world, explosion center, explosion strength.
	 * This will use the default propagation model.
	 * This does not change the world in any way, it just collects the block. However it changes FatTnt internals.
	 * NEVER CALL THIS ASYNCHRONOUSLY OR DURING EVENT PROCESSING (ExplosionPrime, EntityDamageEvent, EntityExplodeEvent).
	 * Command based access should be ok, for instance.
	 * (API)
	 * (Used internally)
	 * @param world
	 * @param cx Center ...
	 * @param cy
	 * @param cz
	 * @param realRadius
	 * @return
	 */
	public List<Block> getExplodingBlocks(World world, double cx, double cy,
			double cz, float realRadius) {
		return propagation.getExplodingBlocks(world, cx, cy, cz, realRadius);
	}
	

	
}

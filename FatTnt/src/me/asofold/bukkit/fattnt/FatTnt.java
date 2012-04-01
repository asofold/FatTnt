package me.asofold.bukkit.fattnt;

import java.io.File;
import java.util.List;

import me.asofold.bukkit.fattnt.config.Defaults;
import me.asofold.bukkit.fattnt.config.Settings;
import me.asofold.bukkit.fattnt.effects.ExplosionManager;
import me.asofold.bukkit.fattnt.propagation.Propagation;
import me.asofold.bukkit.fattnt.propagation.PropagationFactory;
import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Experimental plugin to replace explosions completely.
 * 
 * @author mc_dev
 * @license See project folder, either LICENSE.TXT or fattnt.lists.
 *
 */
public class FatTnt extends JavaPlugin implements Listener {
	

	
//	ExplosionPrimeEvent waitingEP = null;
	
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
		ExplosionManager.applyExplosionEffects(world, x, y, z, realRadius, fire, explEntity, entityType, nearbyEntities, settings, propagation);
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
	
	/**
	 * This only should be called after an explosion has occurred, 
	 * or after getExplodinBlocks or any of the createExplosion methods has been called.
	 * (API)
	 * @param loc
	 * @return
	 */
	public final float getExplosionStrength(final Location loc){
		return propagation.getStrength(loc);
	}
	
	/**
	 * This only should be called after an explosion has occurred, 
	 * or after getExplodinBlocks or any of the createExplosion methods has been called.
	 * (API)
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public final float getExplosionStrength(double x, double y, double z){
		return propagation.getStrength(x, y, z);
	}
	
	/**
	 * Convenience.
	 * (API)
	 * @return
	 */
	public static FatTnt getInstance(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("FatTnt");
		if ( plugin instanceof FatTnt ) return  (FatTnt) plugin;
		else return null;
	}
	
}

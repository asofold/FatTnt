package me.asofold.bukkit.fattnt;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
 * Features: 
 * - configure: block strength
 * - configure: item strength (?+damage value alteration ?)
 * - select what entities are handled (tnt, creeper?)
 * - modifier for explosion strength in general
 * - Let explosions go through blocks without destruction (configurable, defaults to: lava, water).
 * - Fire custom event (!)
 * - damage entities according to distance or to arriving strength 
 * - configure maximum radius to be handled and if to be aborted otherwise.
 * 
 * ? propagate explosion slowly ?
 * - use array with sequence number where has been checked already, + array with explosion strength
 * - use 2x1d arrays + access methods
 * 
 * - custom damage events ?
 * - schedule damage ?
 * - schedule velocity !
 * 
 * - configurable event type ! (EntityExplodeEvent -> then others like lockette can cancel or modify it ! Problem : no entity)
 * 
 * Some API to create custom explosions.
 * 
 * @author mc_dev
 *
 */
public class FatTnt extends JavaPlugin implements Listener {
	
	// config
	public static final String cfgMultRadius = "multiplier.radius";
	public static final String cfgMultDamage = "multiplier.radius";
	public static final String cfgIgnore = "ignore-blocks";
	public static final String cfgInvertIgnored= "invert-ignored";
	public static final String cfgResistence = "resistence";
	public static final String cfgDefaultResistence = "resistence.default";
	public static final String cfgMaxRadius = "radius.max";
	public static final String cfgRandRadius = "radius.random";
	public static final String cfgEntities= "entities";
	public static final String cfgYield = "yield";
	public static final String cfgVelUse = "velocity.use";
	public static final String cfgVelMin = "velocity.min";
	public static final String cfgVelCen= "velocity.center";
	public static final String cfgVelRan = "velocity.random";
	
	int[] defaultIgnoreBlocks = new int[]{
			7, // bedrock
			8,9, // water
			10,11, // lava
			49,90, // obsidian/nether portal
			119,120 // end portal / frame
			};
	
	int[] defaultLowResistance = new int[]{
			0, // air
			8, 18, 30, 31, 32, 37,38, 39, 40, 50, 51, 55,
			59,	63, 75,76, 78, 83, 102, 104, 105, 106, 111,
	};
	
	int[] defaultHigherResistance = new int[]{
			1, 4, 22, 23, 41,42,45, 44, 48, 54, 57, 
			98, 108, 109, 112, 95
	};
	
	int[] defaultStrongResistance = new int[]{
			7, 49, 116, 
	};
	
	// other
	/**
	 * To put in front of messages.
	 */
	public static final String msgPrefix = "[FatTnt] ";
	
	/**
	 * Handle and alter explosions
	 */
	boolean handleExplosions = true;
	
	final Set<EntityType> handledEntities = new HashSet<EntityType>();
	
	float maxRadius = 20.0f;
	public static final float radiusLock = 100.0f; 
	
	float radiusMultiplier = 2.0f;
	
	float damageMultiplier = 7.0f;
	
	float defaultResistance = 2.0f;
	
	boolean invertIgnored = false;
	
	float randDec = 0.2f;
	boolean sparePrimed = true;
	
	ExplosionPrimeEvent waitingEP = null;
	
	int[] sequence = null;
	float[] strength = null;
	int seqMax = 0;
	
	int center = -1;
	int fY = 0;
	int fZ = 0;
	int izMax = 0;
	
	boolean velUse = true;
	float velMin = 0.2f;
	float velCen = 3.0f;
	float velRan = 1.5f;
	
	boolean[] ignore = new boolean[4096];
	float[] resistance = new float[4096];

	private float defaultYield = 0.2f;
	
	private final Random random = new Random(System.currentTimeMillis()-1256875);
	
	final Vector vCenter = new Vector(0.5,0.5,0.5); 
	
	public FatTnt(){
		handledEntities.add(EntityType.PRIMED_TNT);
	}
	
	@Override
	public void onEnable() {
		reloadSettings();
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("fattnt").setExecutor(this);
		System.out.println(msgPrefix+getDescription().getFullName()+" is enabled.");
	}
	
	@Override
	public void onDisable() {
		System.out.println(msgPrefix+getDescription().getFullName()+" is disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		label = label.toLowerCase();
		if ( !label.equals("fattnt")) return false;
		int len = args.length;
		if (len==1 && args[0].equalsIgnoreCase("reload")){
			if ( !checkPerm(sender, "fattnt.cmd.reload")) return true;
			reloadSettings();
			send(sender, "Settings reloaded.");
			return true;
		} 
		else if (len==1 && args[0].equalsIgnoreCase("enable")){
			if ( !checkPerm(sender, "fattnt.cmd.enable")) return true;
			setHandleExplosions(true);
			send( sender, "Explosions will be handled by FatTnt."); 
			return true;
		}
		else if (len==1 && args[0].equalsIgnoreCase("disable")){
			if ( !checkPerm(sender, "fattnt.cmd.disable")) return true;
			setHandleExplosions(false);
			send( sender, "Explosions are back to default behavior (disregarding other plugins)."); 
			return true;
		}
		return false;
	}

	public static final boolean hasPermission(CommandSender sender, String perm){
		return sender.isOp() || sender.hasPermission(perm);
	}
	
	public final static boolean checkPerm(CommandSender sender, String perm){
		if (hasPermission(sender, perm)) return true;
		send(sender, "You are missing the permission: "+perm);
		return false;
	}
	
	public static void send ( CommandSender sender, String message){
		sender.sendMessage(msgPrefix+message);
	}
	
	public void setHandleExplosions(boolean handle){
		handleExplosions = handle;
		// TODO: maybe save to some configuration file ?
	}
	
	public void reloadSettings() {
		File file = new File (getDataFolder(), "config.yml");
		boolean exists = file.exists();
		if ( exists) reloadConfig();
		FileConfiguration cfg = getConfig();
		if (addDefaultSettings(cfg)) saveConfig();
		else if (!exists) saveConfig();
		applySettings(cfg);
	}
	
	/**
	 * 
	 * @param cfg
	 * @return If changes were done.
	 */
	boolean addDefaultSettings(FileConfiguration cfg) {
		boolean changed = false;
		if ( !cfg.contains(cfgEntities)){
			List<String> l = new LinkedList<String>();
			l.add("PRIMED_TNT");
			cfg.set(cfgEntities, l);
			changed = true;
		}
		if ( !cfg.contains(cfgIgnore)){
			List<Integer> l = new LinkedList<Integer>();
			for (int i : defaultIgnoreBlocks){
				l.add(i);
			}
			cfg.set(cfgIgnore, l);
			changed = true;
		}
		if ( !cfg.contains(cfgInvertIgnored)){
			cfg.set(cfgInvertIgnored, false);
			changed = true;
		}
		if ( !cfg.contains(cfgResistence)){
			float[] v = new float[]{1.0f, 3.0f, 5.0f};
			int[][] ids = new int[][]{defaultLowResistance, defaultHigherResistance, defaultStrongResistance};
			String[] keys = new String[]{"low", "higher", "strongest"};
			for ( int i = 0; i<3; i++){
				String base = cfgResistence+"."+keys[i];
				List<Integer> l = new LinkedList<Integer>();
				for ( int id: ids[i]) {
					l.add(id);
				}
				cfg.set(base+".value", v[i]);
				cfg.set(base+".ids", l);
			}
			changed = true;
		}
		if ( !cfg.contains(cfgDefaultResistence)){
			cfg.set(cfgDefaultResistence, 2.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMaxRadius)){
			cfg.set(cfgMaxRadius, 20.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMultDamage)){
			cfg.set(cfgMultDamage, 7.0);
			changed = true;
		}
		if ( !cfg.contains(cfgMultRadius)){
			cfg.set(cfgMultRadius, 2.0);
			changed = true;
		}
		if ( !cfg.contains(cfgRandRadius)){
			cfg.set(cfgRandRadius, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgYield)){
			cfg.set(cfgYield, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgVelUse)){
			cfg.set(cfgVelUse, true);
			changed = true;
		}
		if ( !cfg.contains(cfgVelMin)){
			cfg.set(cfgVelMin, 0.2);
			changed = true;
		}
		if ( !cfg.contains(cfgVelCen)){
			cfg.set(cfgVelCen, 3.0);
			changed = true;
		}
		if ( !cfg.contains(cfgVelRan)){
			cfg.set(cfgVelRan, 1.5);
			changed = true;
		}
		return changed;
	}

	void applySettings(Configuration cfg){
		initBlockIds();
		handledEntities.clear();
		for ( String n : cfg.getStringList(cfgEntities)){
			try{
				handledEntities.add(EntityType.fromName(n));
			} catch (Throwable t){
				getServer().getLogger().warning(msgPrefix+"Bad entity: "+n);
			}
		}
		radiusMultiplier = (float) cfg.getDouble(cfgMultRadius);
		damageMultiplier = (float) cfg.getDouble(cfgMultDamage);
		invertIgnored = cfg.getBoolean(cfgInvertIgnored);
		defaultResistance = (float) cfg.getDouble(cfgDefaultResistence);
		maxRadius = (float) cfg.getDouble(cfgMaxRadius);
		randDec = (float) cfg.getDouble(cfgRandRadius);
		defaultYield = (float) cfg.getDouble(cfgYield);
		velUse = cfg.getBoolean(cfgVelUse);
		velMin = (float) cfg.getDouble(cfgVelMin);
		velCen = (float) cfg.getDouble(cfgVelCen);
		velRan = (float) cfg.getDouble(cfgVelRan);
		
		if ( maxRadius > radiusLock) maxRadius = radiusLock;
		createArrays();
		for (Integer i : cfg.getIntegerList(cfgIgnore)){
			if ( i>0 && i<4096){
				ignore[i] = !invertIgnored;
			} else{
				getServer().getLogger().warning(msgPrefix+"Bad ignore entry: "+i);
			}
		}
		ConfigurationSection sec = cfg.getConfigurationSection(cfgResistence);
		for (String key : sec.getKeys(false)){ 
			if ( "default".equalsIgnoreCase(key)) continue;
			float val = (float) sec.getDouble("value", 1.0);
			List<Integer> ids = sec.getIntegerList("ids");
			for ( Integer i : ids){
				strength[i] = val;
			}
		}
	}
	
	private void initBlockIds() {
		for (int i = 0;i<ignore.length;i++){
			ignore[i] = invertIgnored;
			resistance[i] = defaultResistance;
		}
	}

	void createArrays() {
		int d = 1 + (int) (maxRadius*2.0);
		center = 1 + (int) maxRadius;
		fY = d;
		fZ = d*d;
		int sz = d*d*d;
		izMax = sz - fZ;
		sequence = new int[sz];
		strength = new float[sz];
		for ( int i = 0; i<sz; i++){
			sequence[i] = 0;
			strength[i] = defaultResistance;
		}
	}
	
	/**
	 * Get Sequence number.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final int getSeq(final int x, final int y, final int z){
		return sequence[x+y*fY+z*fZ];
	}
	
	/**
	 * Set sequence number for the given position, if sequence
	 * @param x
	 * @param y
	 * @param z
	 * @param seq
	 */
	final void setSeq(final int x, final int y, final int z, final int seq){
		sequence[x+y*fY+z*fZ] = seq;
	}
	
	/**
	 * Get strength.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final float getStr(final int x, final int y, final int z){
		return strength[x+y*fY+z*fZ];
	}
	
	/**
	 * Increase strength to val, if val is higher.
	 * @param x
	 * @param y
	 * @param z
	 * @param val
	 * @return If increased
	 */
	final boolean incStr(final int x, final int y, final int z, final float val){
		final int i = x+y*fY+z*fZ;
		final float ref = strength[i];
		if ( ref < val){
			strength[i] = val;
			return true;
		} else{
			return false;
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	void onExplosionPrimeLowest(ExplosionPrimeEvent event){
		waitingEP = null;
		if (!handleExplosions) return;
		else if (event.isCancelled()) return;
		else if (!handledEntities.contains(event.getEntityType())) return;
		// do prepare to handle this explosion:
		event.setCancelled(true);
		waitingEP = event;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	void onExplosionPrime(ExplosionPrimeEvent event){
		// check event 
		if ( waitingEP != event) return;
		waitingEP = null;
		// event is to be handled:
		if ( !event.isCancelled()) event.setCancelled(true); // just in case other plugins mess with this one.
		EntityType type = event.getEntityType();
		Entity entity = event.getEntity();
		Location loc = entity.getLocation().clone();
		// Might have to schedule this for next tick ?
		createExplosion(loc, event.getRadius(), event.getFire(), entity, type);
		if (!entity.isDead()) entity.remove();
	}
	
	/**
	 * API
	 * (called from event handling as well)
	 * @param loc
	 * @param radius As in World.createExplosion
	 * @param fire
	 * @param entityType May be null, might be unused.
	 */
	public void createExplosion(Location loc, float radius, boolean fire, Entity explEntity, EntityType entityType){
		// create a fake explosion
		World world = loc.getWorld();
		world.createExplosion(loc, 0.0F);
		// calculate effects
		// WORKAROUND:
		float realRadius = radius*radiusMultiplier;
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		List<Entity> nearbyEntities;
		if (explEntity==null) nearbyEntities = getNearbyEntities(world, x,y,z, realRadius);
		else nearbyEntities = explEntity.getNearbyEntities(realRadius, realRadius, realRadius);
		applyExplosionEffects(world, x, y, z, realRadius, fire, explEntity, entityType, nearbyEntities);
	}

	public static List<Entity> getNearbyEntities(World world, double x, double y,
			double z, double realRadius) {
		List<Entity> nearbyEntities;
		Entity dummyEntity = world.spawnCreature(new Location(world,x,y,z), EntityType.CHICKEN);
		if ( dummyEntity==null){
			// TODO: maybe warn ?
			System.out.println("CHECK ALL ENTITIES"); // TODO: REMOVE
			nearbyEntities = new LinkedList<Entity>();
			for ( Entity entity : world.getEntities() ){
				Location ref = entity.getLocation();
				if (Math.abs(x-ref.getX())<realRadius && Math.abs(z-ref.getZ())<realRadius && Math.abs(y-ref.getY())<realRadius) nearbyEntities.add(entity);
			}
		} else{
			nearbyEntities = dummyEntity.getNearbyEntities(realRadius, realRadius, realRadius);
			dummyEntity.remove();
		}
		return nearbyEntities;
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
		createExplosion(new Location(world, x,y,z), radius, fire, null, null);
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius intended to be the block distance rather, so that resistance of 1.0 (AIR) would lead to the full range.
	 * @param fire
	 * @param entityType Causing the explosion.
	 * @param nearbyEntities List of entities that can be affected , should be within radius (x,y,z independently), damage depends on settings.
	 */
	public void applyExplosionEffects(World world, double x, double y,
			double z, float realRadius, boolean fire, Entity explEntity, EntityType entityType,
			List<Entity> nearbyEntities) {
		if ( realRadius > maxRadius){
			// TODO: setttings ?
			realRadius = maxRadius;
		}
		Server server = getServer();
		PluginManager pm = server.getPluginManager();
		
		// blocks:
		List<Block> affected = getExplodingBlocks(world , x, y, z, realRadius);
		EntityExplodeEvent exE = new EntityExplodeEvent(explEntity, new Location(world,x,y,z), affected, defaultYield );
		pm.callEvent(exE);
		if (exE.isCancelled()) return;
		float yield = exE.getYield();
		for ( Block block : exE.blockList()){
//			if (block.getType() == Material.TNT){
//				// TODO: spawn primed tnt !
//				block.setTypeId(0);
//				continue;
//			}
			Collection<ItemStack> drops = block.getDrops();
			for (ItemStack drop : drops){
				if ( random.nextFloat()<=yield){
					Location loc = block.getLocation().add(vCenter);
					Item item = world.dropItemNaturally(loc, drop.clone());
					if (item==null) continue;
					addRandomVelocity(item, loc, x,y,z, realRadius);
				}
			}
			block.setTypeId(0, true);
		}
		
		// entities:
		final double damBase = realRadius * damageMultiplier;
		for ( Entity entity : nearbyEntities){
			// TODO: damage entities according to type, + vector ?
			// test damage:
			double d = entity.getLocation().distance(new Location(world,x,y,z)); // TODO efficiency
			// TODO: test the strength map depending on settings !
			if ( d>realRadius) continue;
			addRandomVelocity(entity, entity.getLocation(), x,y,z, realRadius);
			if (sparePrimed && (entity instanceof TNTPrimed)) continue;
			int damage = 1 + (int) (damBase/(d+1.0)) ;
			EntityDamageEvent event = new EntityDamageEvent(entity, DamageCause.ENTITY_EXPLOSION, damage);
			pm.callEvent(event);
			if (!event.isCancelled()){
				if ( entity instanceof LivingEntity){
					((LivingEntity) entity).setLastDamageCause(event);
				}
				else entity.setLastDamageCause(event); // TODO
			}
		}
		
		
	}

	private void addRandomVelocity(Entity entity, Location loc, double x, double y,
			double z, float realRadius) {
		// TODO: make some things configurable !
		if (!velUse) return;
		Vector v = entity.getVelocity();
		Vector fromCenter = new Vector(loc.getX()-x,loc.getY()-y,loc.getZ()-z).normalize();
		Vector rv = v.add(fromCenter.multiply(velMin+random.nextFloat()*velCen)).add(Vector.getRandom().multiply(velRan));
		if (entity instanceof LivingEntity) ((LivingEntity) entity).setVelocity(rv); 
		else if (entity instanceof TNTPrimed) ((TNTPrimed) entity).setVelocity(rv);
		else entity.setVelocity(rv);
	}

	public List<Block> getExplodingBlocks(World world, double cx, double cy,
			double cz, float realRadius) {
		if ( realRadius > maxRadius){
			// TODO: setttings ?
			realRadius = maxRadius;
		}
		List<Block> blocks = new LinkedList<Block>();
		seqMax ++; // new round !
		// starting at center block decrease weight and check neighbor blocks recursively, while weight > durability continue, only check
		propagate(world, (int)cx, (int)cy, (int)cz, center*(1+fY+fZ), realRadius, blocks);
		return blocks;
	}
	
	/**
	 * TEST VERSION / LOW OPTIMIZATION !
	 * Recursively collect blocks that get destroyed.
	 * @param w
	 * @param cx Current real world pos.
	 * @param cy
	 * @param cz
	 * @param i index of  array
	 * @param realRadius
	 * @param seq
	 * @param blocks
	 */
	final void propagate(final World w, final int cx, final int cy, final int cz, 
			final int i, float realRadius, final List<Block> blocks){
		// Matrix position:
		sequence[i] = seqMax;
		strength[i] = realRadius;
		if ( cy > w.getMaxHeight()) return; // TODO: maybe +-1 ?
		// World block position:
		final Block block = w.getBlockAt(cx,cy,cz);
		final int id = block.getTypeId();
		// Resistance check:
		float dur ; // AIR
		final boolean ign;
		if (id>=0 && id<4096){
			dur = resistance[id];
			ign = ignore[id];
		}
		else{
			dur = 1.0f;
			ign = true;
		}
		if ( randDec > 0.0) dur += random.nextFloat()*randDec;
		if ( dur > realRadius) return; // no propagation
		realRadius -= dur;
		// Add block or not:
		if (!ign) blocks.add(block);
		// propagate:
		if (i<fZ || i>izMax) return;
		final int j1 = i - 1;
		if (sequence[j1]!=seqMax || realRadius>strength[j1]) propagate(w, cx-1, cy, cz, j1, realRadius, blocks); 	
		final int j2 = i + 1;
		if (sequence[j2]!=seqMax || realRadius>strength[j2]) propagate(w, cx+1, cy, cz, j2, realRadius, blocks); 
		final int j3 = i - fY;
		if (sequence[j3]!=seqMax || realRadius>strength[j3]) propagate(w, cx, cy-1, cz, j3, realRadius, blocks); 
		final int j4 = i + fY;
		if (sequence[j4]!=seqMax || realRadius>strength[j4]) propagate(w, cx, cy+1, cz, j4, realRadius, blocks); 
		final int j5 = i - fZ;
		if (sequence[j5]!=seqMax || realRadius>strength[j5]) propagate(w, cx, cy, cz-1, j5, realRadius, blocks); 
		final int j6 = i + fZ;
		if (sequence[j6]!=seqMax || realRadius>strength[j6]) propagate(w, cx, cy, cz+1, j6, realRadius, blocks); 
	}
	
}

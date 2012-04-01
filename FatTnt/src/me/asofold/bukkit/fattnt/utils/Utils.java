package me.asofold.bukkit.fattnt.utils;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bukkit.fattnt.config.Defaults;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public class Utils {

	/**
	 * Op or hasPermission
	 * @param sender
	 * @param perm
	 * @return
	 */
	public static final boolean hasPermission(CommandSender sender, String perm){
		return sender.isOp() || sender.hasPermission(perm);
	}

	/**
	 * Check permission, message sender on failure.
	 * @param sender
	 * @param perm
	 * @return
	 */
	public static final boolean checkPerm(CommandSender sender, String perm){
		if (hasPermission(sender, perm)) return true;
		send(sender, "You are missing the permission: "+perm);
		return false;
	}

	/**
	 * Send with or without colors, add message prefix.
	 * @param sender
	 * @param message
	 */
	public static void send( CommandSender sender, String message){
		sender.sendMessage(Defaults.msgPrefix+message);
	}

	/**
	 * Fall-back method, it either tries to spawn a chicken, or iterates over all worlds entities (...).
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param realRadius
	 * @return
	 */
	public static List<Entity> getNearbyEntities(World world, double x, double y,
			double z, double realRadius) {
		List<Entity> nearbyEntities;
		Entity dummyEntity = world.spawnCreature(new Location(world,x,y,z), EntityType.CHICKEN);
		if ( dummyEntity==null){
			// TODO: maybe warn ?
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
	 * Deal the damage to the entity.
	 * (might be aiming at explosions currently)
	 * @param event
	 */
	public static  void damageEntity(EntityDamageEvent event) {
		int damage = event.getDamage();
		if ( damage == 0) return;
		Entity entity = event.getEntity();
		if (entity == null) return; // impossible ?
		if (entity.isDead()) return;
		EntityType type = entity.getType();
		entity.setLastDamageCause(event); 
		if ( type.isAlive()){
			// TODO: armor !
			((LivingEntity) entity).damage(damage);
		} 
		// TODO: some stuff with different entity types (vehicles, items, paintings).
		// TODO: maybe some destruction chance !
	}

	/**
	 * Block coordinate for double, especially important for negative numbers.
	 * (Adapted From Bukkit/NumberConversions.)
	 * @param x
	 * @return
	 */
	public static final int floor(final double x) {
        final int floor = (int) x;
        return (floor == x)? floor : floor - (int) (Double.doubleToRawLongBits(x) >>> 63);
    }

}

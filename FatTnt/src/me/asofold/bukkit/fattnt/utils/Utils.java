package me.asofold.bukkit.fattnt.utils;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bukkit.fattnt.config.Defaults;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
		send(sender, message, true);
	}
	
	public static void send(CommandSender sender, String message, boolean prefix) {
		if (prefix) message = Defaults.msgPrefix + message;
		if ( !(sender instanceof Player)) message = ChatColor.stripColor(message);
		sender.sendMessage(message);
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

package me.asofold.bukkit.fattnt.propagation;

import java.util.List;

import me.asofold.bukkit.fattnt.config.Settings;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Base class for encapsulating the propagation handling.
 * @author mc_dev
 *
 */
public abstract class Propagation {
	
	final float defaultResistance;
	final float[] resistance;
	final boolean[] ignore;

	float maxRadius = 0;
	
	// TODO: add axplosion center into this
	
	public Propagation(Settings settings){
		this.defaultResistance = settings.defaultResistance;
		this.resistance = settings.resistance;
		this.ignore = settings.ignore;
		this.maxRadius = settings.maxRadius;
	}
	
	/**
	 * Get the explosion strength at a coordinate.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public abstract float getStrength(double x, double y, double z);
	
	/**
	 * Get explosion strecngth for a location.
	 * @param loc
	 * @return
	 */
	public float getStrength(Location loc){
		return getStrength(loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * 
	 * @param world
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param realRadius
	 * @return
	 */
	public abstract List<Block> getExplodingBlocks(World world, double cx, double cy,
			double cz, float realRadius);
	
}
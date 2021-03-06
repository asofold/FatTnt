package me.asofold.bpl.fattnt.propagation;

import java.util.List;

import me.asofold.bpl.fattnt.config.ExplosionSettings;
import me.asofold.bpl.fattnt.config.Settings;
import me.asofold.bpl.fattnt.stats.Stats;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Base class for encapsulating the propagation handling.
 * @author mc_dev
 *
 */
public abstract class Propagation {

	float maxRadius = 0;
	
	final Stats stats;

	public Propagation(Settings settings){
		this.maxRadius = settings.getMaxRadius(); // absolute possible maximum.
		this.stats = settings.stats;
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
	 * If ids were cached return therm from here, -1 if unavailable.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int getTypeId(int x, int y, int z){
		return -1;
	}
	
	/**
	 * 
	 * @param world
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param realRadius
	 * @param settings 
	 * @return
	 */
	public abstract List<Block> getExplodingBlocks(World world, double cx, double cy,
			double cz, float realRadius, ExplosionSettings explosionSettings);
	
	/**
	 * To be called periodically, for cleanup action etc., probably.
	 */
	public void onIdle(){
	}
}

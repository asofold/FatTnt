package me.asofold.bukkit.fattnt.propagation;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bukkit.fattnt.FatTnt;
import me.asofold.bukkit.fattnt.config.Settings;
import me.asofold.bukkit.fattnt.utils.Utils;

import org.bukkit.World;
import org.bukkit.block.Block;

public class ArrayPropagation extends Propagation {
	
	int[] sequence = null;
	float[] strength = null;
	int seqMax = 0;
	
	int center = -1;
	int fY = 0;
	int fZ = 0;
	int izMax = 0;
	int iCenter = -1;
	
	/**
	 * Explosion center block coords.
	 */
	int cx = 0;
	int cy = 0;
	int cz = 0;
	
	float fStraight;

	public ArrayPropagation(Settings settings) {
		super(settings);
		fStraight = settings.fStraight;
		createArrays();
		
		// TODO: init on base of settings
		
	}
	
	private void createArrays() {
		int d = 1 + (int) (maxRadius*2.0);
		center = 1 + (int) maxRadius;
		fY = d;
		fZ = d*d;
		int sz = d*d*d;
		izMax = sz - fZ;
		iCenter = center+ center*fY + center*fZ; // TODO: check if such is right
		sequence = new int[sz];
		strength = new float[sz];
		for ( int i = 0; i<sz; i++){
			sequence[i] = 0;
		}
	}

	@Override
	public float getStrength(final double x, final double y, final double z) {
		final int dx = center + Utils.floor(x) - cx;
		final int dy = center + Utils.floor(y) - cy;
		final int dz = center + Utils.floor(z) - cz ;
		final int index = dx+fY*dy+fZ*dz;
		if ( index<0 || index>= strength.length) return 0.0f; // outside of possible bounds.
		if ( sequence[index] != seqMax) return 0.0f; // unaffected // WARNING: this uses seqMax, which has been set in getExplodingBlocks !
		return strength[index]; // effective radius / strength
	}

	@Override
	public List<Block> getExplodingBlocks(World world, double cx, double cy,
			double cz, float realRadius) {
		{
			if ( realRadius > maxRadius){
				// TODO: setttings ?
				realRadius = maxRadius;
			}
			List<Block> blocks = new LinkedList<Block>();
			seqMax ++; // new round !
			// starting at center block decrease weight and check neighbor blocks recursively, while weight > durability continue, only check
			if (FatTnt.DEBUG) System.out.println("start at: "+cx+","+cy+","+cz);
			this.cx = Utils.floor(cx);
			this.cy = Utils.floor(cy);
			this.cz = Utils.floor(cz);
			propagate(world, this.cx, this.cy, this.cz, iCenter, 0, realRadius, blocks);
			return blocks;
		}
	}
	
	/**
	 * TEST VERSION / LOW OPTIMIZATION !
	 * Recursively collect blocks that get destroyed.
	 * @param w
	 * @param x Current real world pos.
	 * @param y
	 * @param z
	 * @param i index of  array
	 * @param expStr Strength of explosion, or radius
	 * @param seq
	 * @param blocks
	 */
	final void propagate(final World w, final int x, final int y, final int z, 
			final int i, final int dir, float expStr, final List<Block> blocks){
		if ( y<0 || y > w.getMaxHeight()) return; // TODO: maybe +-1 ?
		// World block position:
		final Block block = w.getBlockAt(x,y,z);
		final int id = block.getTypeId();
		// Resistance check:
		float dur ; // AIR
		final boolean ign;
		if (id>=0 && id<4096){
			dur = resistance[id];
			ign = ignore[id];
		}
		else{
			dur = defaultResistance;
			ign = true;
		}
		if (FatTnt.DEBUG) System.out.println(x+","+y+","+z+" - "+expStr+" | "+id+":"+dur); // TODO: remove this
		final boolean noAdd;
		if ( sequence[i] == seqMax){
			if ( strength[i] >= dur) noAdd = true;
			else noAdd = false;
		}
		else noAdd = false;
		// Matrix position:
		sequence[i] = seqMax;
		strength[i] = expStr;
//		if ( randDec > 0.0) dur += random.nextFloat()*randDec;
		if ( dur > expStr) return; // no propagation
		expStr -= dur; // decrease after setting the array
		// Add block or not:
		if (id!=0 && !noAdd && !ign) blocks.add(block);
		// propagate:
		if (i<fZ || i>izMax) return;
		// x-
		if (dir != 2){
			final float effStr; // radius to be used.
			if (dir==4) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j1 = i - 1;
			if (sequence[j1]!=seqMax || effStr>strength[j1]) propagate(w, x-1, y, z, j1, 4, effStr, blocks);
		}
		// x+
		if ( dir != 4){
			final float effStr; // radius to be used.
			if (dir==2) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j2 = i + 1;
			if (sequence[j2]!=seqMax || effStr>strength[j2]) propagate(w, x+1, y, z, j2, 2, effStr, blocks);
		}
		// y-
		if (dir != 6){
			final float effStr; // radius to be used.
			if (dir==8) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j3 = i - fY;
			if (sequence[j3]!=seqMax || effStr>strength[j3]) propagate(w, x, y-1, z, j3, 8, effStr, blocks);
		}
		// y+
		if (dir != 8){
			final float effStr; // radius to be used.
			if (dir==6) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j4 = i + fY;
			if (sequence[j4]!=seqMax || effStr>strength[j4]) propagate(w, x, y+1, z, j4, 6, effStr, blocks);
		}
		// z-
		if (dir != 10){
			final float effStr; // radius to be used.
			if (dir==12) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j5 = i - fZ;
			if (sequence[j5]!=seqMax || effStr>strength[j5]) propagate(w, x, y, z-1, j5, 12, effStr, blocks);
		}
		// z+
		if (dir!=12){
			final float effStr; // radius to be used.
			if (dir==10) effStr = expStr * fStraight;
			else effStr = expStr;
			final int j6 = i + fZ;
			if (sequence[j6]!=seqMax || effStr>strength[j6]) propagate(w, x, y, z+1, j6, 10, effStr, blocks); 
		}
	}

}

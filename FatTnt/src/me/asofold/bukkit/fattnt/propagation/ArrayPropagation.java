package me.asofold.bukkit.fattnt.propagation;

import java.util.LinkedList;
import java.util.List;

import me.asofold.bukkit.fattnt.FatTnt;
import me.asofold.bukkit.fattnt.config.Defaults;
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
	 * Stats: number of visited blocks (some count double)
	 */
	int n = 0;
	
	/**
	 * Explosion center block coords.
	 */
	int cx = 0;
	int cy = 0;
	int cz = 0;
	
	float fStraight;
	
	float minRes;
	
	private static final int[] ortDir = new int[]{2,4,6,8,10,12};
	
	/**
	 * opposite direction:
	 * 0:  no direction
	 * 1:  reserved: diagonal
	 * 2:  x+
	 * 3:  reserved: diagonal
	 * 4:  x-
	 * 5:  reserved: diagonal
	 * 6:  y+
	 * 7:  reserved: diagonal
	 * 8:  y-
	 * 9:  reserved: diagonal
	 * 10: z+
	 * 11: reserved: diagonal
	 * 12: z-
	 */
	private static final int[] oDir = new int[]{
		0,  // 0: no direction maps to no direction
		0,  // UNUSED
		4,  // x+ -> x-
		0,  // UNUSED
		2,  // x- -> x+
		0,  // UNUSED
		8,  // y+ -> y-
		0,  // UNUSED
		6,  // y- -> y+
		0,  // UNUSED
		12, // z+ -> z-
		0,  // UNUSED
		10, // z- -> z+
	} ;
	
	/**
	 * x increment by direction.
	 */
	private static final int[] xInc = new int[]{
		0,  // 0: no direction maps to no direction
		0,  // UNUSED
		1,  // x+ 
		0,  // UNUSED
		-1,  // x-
		0,  // UNUSED
		0,  // y+ 
		0,  // UNUSED
		0,  // y- 
		0,  // UNUSED
		0, // z+ 
		0,  // UNUSED
		0, // z-
	};
	
	/**
	 * y increment by direction.
	 */
	private static final int[] yInc = new int[]{
		0,  // 0: no direction maps to no direction
		0,  // UNUSED
		0,  // x+ 
		0,  // UNUSED
		0,  // x- 
		0,  // UNUSED
		1,  // y+ 
		0,  // UNUSED
		-1,  // y- 
		0,  // UNUSED
		0, // z+ 
		0,  // UNUSED
		0, // z- 
	};
	
	/**
	 * z increment by direction.
	 */
	private static final int[] zInc = new int[]{
		0,  // 0: no direction maps to no direction
		0,  // UNUSED
		0,  // x+ 
		0,  // UNUSED
		0,  // x- 
		0,  // UNUSED
		0,  // y+ 
		0,  // UNUSED
		0,  // y- 
		0,  // UNUSED
		1, // z+ 
		0,  // UNUSED
		-1, // z- 
	};
	
	/**
	 * Array increments by direction.
	 */
	private static final int[] aInc =  new int[13];

	public ArrayPropagation(Settings settings) {
		super(settings);
		fStraight = settings.fStraight;
		minRes = settings.minResistance;
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
		for (int i=0; i<aInc.length; i++){
			aInc[i] = xInc[i] + yInc[i]*fY + zInc[i]*fZ;
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
			if (FatTnt.DEBUG) System.out.println(Defaults.msgPrefix+"Explosion at: "+world.getName()+" / "+cx+","+cy+","+cz);
			this.cx = Utils.floor(cx);
			this.cy = Utils.floor(cy);
			this.cz = Utils.floor(cz);
			n = 0;
			propagate(world, this.cx, this.cy, this.cz, iCenter, 0, realRadius, blocks);
			if (FatTnt.DEBUG) System.out.println(Defaults.msgPrefix+"Strength="+realRadius+"("+maxRadius+"/"+minRes+"), visited="+n+", blocks="+blocks.size());
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
		n ++;
		// Block type check (id):
		final int id;
		final Block block;
		if ( y>=0 && y <= w.getMaxHeight()){// TODO: maybe +-1 ?
			block = w.getBlockAt(x,y,z);
			id = block.getTypeId();
		} 
		else{
			id = 0;
			block = null;
		}
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
		if (block == null) noAdd = true;
		else if ( sequence[i] == seqMax){
			if ( strength[i] >= dur) noAdd = true;
			else noAdd = false;
		}
		else noAdd = false;
		// Matrix position:
		sequence[i] = seqMax;
		strength[i] = expStr;
//		if ( randDec > 0.0) dur += random.nextFloat()*randDec;
		if ( dur > expStr) return; // this block stopped this path of propagation.
		expStr -= dur; // decrease after setting the array
		// Add block or not:
		if (id!=0 && !noAdd && !ign) blocks.add(block);
		
		
		// propagate:
		if (i<fZ || i>izMax) return; // no propagation from edge on.
		
		for (int nd : ortDir){ 
			// (iterate over orthogonal directions)
			final float effStr; // strength to be used.
			// Check penalty for propagation in the same direction again:
			if (nd == oDir[dir]) effStr = expStr * fStraight;
			else effStr = expStr;
			if (effStr<minRes) continue; // not strong enough to propagate through any further block.
			final int j = i + aInc[3];
			if (sequence[j]!=seqMax || effStr>strength[j]) propagate(w, x+xInc[0], y+yInc[1], z+zInc[2], j, nd, effStr, blocks);
		}
	}
}

package com.ninja.NinjaEdit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.World;

import com.ninja.NinjaEdit.maths.Vec3;
import com.ninja.NinjaEdit.regions.CuboidRegion;
import com.ninja.NinjaEdit.regions.Region;


public class EditHistory {

	//Stores The changes made
	private Map<Vec3, DataBlock> before = new HashMap<Vec3,DataBlock>();
	private Map<Vec3, DataBlock> after = new HashMap<Vec3, DataBlock>();
	
	private HashMap<Vec3, DataBlock> aSyncBlocks = new HashMap<Vec3, DataBlock>();
	
	private boolean aSync = false;
	
	//List of blocks which will be done last
	private static HashSet<DataBlock> aSyncBlockslist = new HashSet<DataBlock>();
	static { 
		
		aSyncBlockslist.add(new DataBlock(50)); //torch
		aSyncBlockslist.add(new DataBlock(75)); //redstonetorch off
		aSyncBlockslist.add(new DataBlock(76)); //redstonetorch on
		aSyncBlockslist.add(new DataBlock(93)); //repeater off
		aSyncBlockslist.add(new DataBlock(94)); //repeater on
		aSyncBlockslist.add(new DataBlock(55)); //redstone wire
		aSyncBlockslist.add(new DataBlock(149)); //comparator off
		aSyncBlockslist.add(new DataBlock(150)); //comparator on
		aSyncBlockslist.add(new DataBlock(31, 0)); //dead shrub
		aSyncBlockslist.add(new DataBlock(31, 1)); //tallgrass
		aSyncBlockslist.add(new DataBlock(31, 2)); //fern
		aSyncBlockslist.add(new DataBlock(175, 0)); //sunflower
		aSyncBlockslist.add(new DataBlock(175, 1)); //lilac
		aSyncBlockslist.add(new DataBlock(175, 2)); //double tallgrass
		aSyncBlockslist.add(new DataBlock(175, 3)); //large fern
		aSyncBlockslist.add(new DataBlock(175, 4)); //rose bush
		aSyncBlockslist.add(new DataBlock(175, 5)); //Peony
		aSyncBlockslist.add(new DataBlock(37)); //dandelion
		aSyncBlockslist.add(new DataBlock(38)); //poppy
		aSyncBlockslist.add(new DataBlock(38, 1)); //blue orchid
		aSyncBlockslist.add(new DataBlock(38, 2)); //allium
		aSyncBlockslist.add(new DataBlock(38, 3)); //azure bluet
		aSyncBlockslist.add(new DataBlock(38, 4)); //red tulip
		aSyncBlockslist.add(new DataBlock(38, 5)); //orange tulip
		aSyncBlockslist.add(new DataBlock(38, 6)); //White tulip
		aSyncBlockslist.add(new DataBlock(38, 7)); //pink tulip
		aSyncBlockslist.add(new DataBlock(38, 8)); //oxeye daisy
		aSyncBlockslist.add(new DataBlock(6, 0)); //oak sapling
		aSyncBlockslist.add(new DataBlock(6, 1)); //spruce sapling
		aSyncBlockslist.add(new DataBlock(6, 2)); //birch sapling
		aSyncBlockslist.add(new DataBlock(6, 3)); //jungle sapling
		aSyncBlockslist.add(new DataBlock(6, 4)); //acacia sapling
		aSyncBlockslist.add(new DataBlock(6, 5)); //dark oak sapling
		aSyncBlockslist.add(new DataBlock(66)); //Rail
		aSyncBlockslist.add(new DataBlock(27)); //powered rail
		aSyncBlockslist.add(new DataBlock(28)); //detector rail
		aSyncBlockslist.add(new DataBlock(157)); //activator rail
		aSyncBlockslist.add(new DataBlock(39)); //brown mushroom
		aSyncBlockslist.add(new DataBlock(40)); //red mushroom
		aSyncBlockslist.add(new DataBlock(65)); //ladder
		aSyncBlockslist.add(new DataBlock(78)); //snow
		aSyncBlockslist.add(new DataBlock(321)); //painting
		aSyncBlockslist.add(new DataBlock(83)); //sugar cane
	}
	@SuppressWarnings("deprecation")
	public DataBlock getBlock(World world, Vec3 vec) {
		int type = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getTypeId();
		int data = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getData();
		return new DataBlock(type, data);
	}
	
	public DataBlock hGetBlock(World world, Vec3 vec) {
        // In the case of the queue, the block may have not actually been
        // changed yet
        if (aSync) {

            if (after.containsKey(vec)) {
                return after.get(vec);
            }
        }
        
        return getBlock(world, vec);
    }
	
	//No history change
	@SuppressWarnings("deprecation")
	public void setBlock(World world, Vec3 vec, DataBlock block) {
		world.getBlockAt((int)vec.getBlockX(), (int)vec.getBlockY(), (int)vec.getBlockZ()).setTypeIdAndData(block.getTypeId(), (byte) block.getNBTValue(), false);
	}
	
	//History change
	public void hSetBlock(World world, Vec3 vec, DataBlock block) {
        if (!before.containsKey(vec)) {
            before.put(vec, getBlock(world, vec));
        }
        
        after.put(vec, block);
        aSyncSetBlock(world, vec, block);
    }
	private void aSyncSetBlock(World world, Vec3 vec, DataBlock block) {
		if(aSync) {
			if (!block.isAir() && aSyncBlockslist.contains(block) && getBlock(world, vec.tempAdd(0, -1, 0)).isAir()) {
				aSyncBlocks.put(vec, block);
                if(hGetBlock(world, vec) != block) return;
            } else if (block.isAir() && aSyncBlockslist.contains(getBlock(world, vec.tempAdd(0, 1, 0)))) {
                setBlock(world, vec.tempAdd(0, 1, 0), new DataBlock(0)); // Prevent items from being dropped
            }
		}
		setBlock(world, vec, block);
	}
	
	 public int setBlocks(World world, Region region, DataBlock blocktype) {
	 	 
         int affected = 0;
     
         if (region instanceof CuboidRegion) {
             Vec3 min = region.getMinimumPoint();
             Vec3 max = region.getMaximumPoint();
         
             int minX = min.getBlockX();
             int minY = min.getBlockY();
             int minZ = min.getBlockZ();
             int maxX = max.getBlockX();
             int maxY = max.getBlockY();
             int maxZ = max.getBlockZ();

             
             for (int x = minX; x <= maxX; x++) {
                 for (int y = minY; y <= maxY; y++) {
                     for (int z = minZ; z <= maxZ; z++) {
                         Vec3 pt = new Vec3(x, y, z);
                         hSetBlock(world, pt, blocktype);
                         affected++;
                     }
                 }
             }
         } 
        
        return affected;
	 }

	
	 
	 public int replaceBlocks(World world, Region region, int fromBlockType, DataBlock toBlock) {
	 	 
	 	 int affected = 0;
	 	 if (region instanceof CuboidRegion) {
	            
	            Vec3 min = region.getMinimumPoint();
	            Vec3 max = region.getMaximumPoint();
     
	            int minX = min.getBlockX();
	            int minY = min.getBlockY();
	            int minZ = min.getBlockZ();
	            int maxX = max.getBlockX();
	            int maxY = max.getBlockY();
	            int maxZ = max.getBlockZ();
	            
	            for (int x = minX; x <= maxX; x++) {
	                for (int y = minY; y <= maxY; y++) {
	                    for (int z = minZ; z <= maxZ; z++) {
	                        Vec3 vec = new Vec3(x, y, z);
	                        int curBlockType = getBlock(world, vec).getTypeId();
	                        if (fromBlockType == -1 && curBlockType != 0 || curBlockType == fromBlockType) {
	                            	hSetBlock(world, vec, toBlock);
	                                affected++;
	                        }
	                    }
	                }
	            }
	        
	 	 }
	 	 return affected;
	 }
	 
	 public int stackClipboard(World world, Region region, Vec3 dir, int count) {
	        int affected = 0;

	        Vec3 min = region.getMinimumPoint();
	        Vec3 max = region.getMaximumPoint();
	        int xs = region.getWidth();
	        int ys = region.getHeight();
	        int zs = region.getLength();

	        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
	            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
	                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
	                    DataBlock block = getBlock(world, new Vec3(x, y, z));

	                    if (!block.isAir()) {
	                        for (int i = 1; i <= count; i++) {
	                            Vec3 pos = new Vec3(x + xs * dir.getBlockX() * i, y + ys * dir.getBlockY() * i, z + zs * dir.getBlockZ() * i);
	                            hSetBlock(world, pos, block);
	                               affected++;
	                            
	                        }
	                    }
	                }
	            }
	        }

	        return affected;
	    }
	 
	 
	 
	 
	public void undo(World world) {
		for(Map.Entry<Vec3, DataBlock> beforedata : before.entrySet()) {
			Vec3 vec = beforedata.getKey();
			setBlock(world, vec, beforedata.getValue());
		}
		finshAsyncBlocks(world);
	}
	
	
	//undo's what you have undoed
	public void redo(World world) {
        for (Map.Entry<Vec3,DataBlock> afterdata : after.entrySet()) {
            Vec3 vec = afterdata.getKey();
            setBlock(world, vec, afterdata.getValue());
        }
        finshAsyncBlocks(world);
    }
	
	public void disableAsync(World world) {
        if (aSync != false) {
        	finshAsyncBlocks(world);
        }
        aSync = false;
    }
	
	public void finshAsyncBlocks(World world) {
        if (!aSync) { return; }
        
        for (Map.Entry<Vec3,DataBlock> entry : aSyncBlocks.entrySet()) {
            Vec3 vec = (Vec3)entry.getKey();
            setBlock(world, vec, (DataBlock)entry.getValue());
        }
    }
	
	public int size() {
        return before.size();
    }
}

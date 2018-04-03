package com.ninja.NinjaEdit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.ninja.NinjaEdit.blocks.ChestBlock;
import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.blocks.DispenserBlock;
import com.ninja.NinjaEdit.blocks.FurnaceBlock;
import com.ninja.NinjaEdit.blocks.MobSpawnerBlock;
import com.ninja.NinjaEdit.blocks.NoteBlock;
import com.ninja.NinjaEdit.blocks.SignBlock;
import com.ninja.NinjaEdit.blocks.pattern.Pattern;
import com.ninja.NinjaEdit.maths.Vec3;
import com.ninja.NinjaEdit.regions.CuboidRegion;
import com.ninja.NinjaEdit.regions.Region;

public class EditHistory
{
	//Stores The changes made
	private Map<Vec3, DataBlock> before = new HashMap<Vec3, DataBlock>();
	private Map<Vec3, DataBlock> after = new HashMap<Vec3, DataBlock>();

	private HashMap<Vec3, DataBlock> aSyncBlocks = new HashMap<Vec3, DataBlock>();

	private boolean aSync = false;

	//List of blocks which will be done last
	private static final HashSet<Integer> aSyncBlockslist = new HashSet<Integer>();
	
	static
	{
		aSyncBlockslist.add(50); //torch
		aSyncBlockslist.add(75); //redstonetorch off
		aSyncBlockslist.add(76); //redstonetorch on
		aSyncBlockslist.add(93); //repeater off
		aSyncBlockslist.add(94); //repeater on
		aSyncBlockslist.add(55); //redstone wire
		aSyncBlockslist.add(149); //comparator off
		aSyncBlockslist.add(150); //comparator on
		aSyncBlockslist.add(63); //standing sign
		aSyncBlockslist.add(68); //flying sign

		/*aSyncBlockslist.add(new DataBlock(50)); //torch
		aSyncBlockslist.add(new DataBlock(75)); //redstonetorch off
		aSyncBlockslist.add(new DataBlock(76)); //redstonetorch on
		aSyncBlockslist.add(new DataBlock(93)); //repeater off
		aSyncBlockslist.add(new DataBlock(94)); //repeater on
		aSyncBlockslist.add(new DataBlock(55)); //redstone wire
		aSyncBlockslist.add(new DataBlock(149)); //comparator off
		aSyncBlockslist.add(new DataBlock(150)); //comparator on
		aSyncBlockslist.add(new DataBlock(31)); //dead shrub
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
		aSyncBlockslist.add(new DataBlock(6)); //oak sapling
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
		aSyncBlockslist.add(new DataBlock(83)); //sugar cane*/
	}

	@SuppressWarnings("deprecation")
	public DataBlock getBlock(World world, Vec3 vec)
	{
		int type = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getTypeId();
		int data = world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getData();
		//don't store piston heads
		if(type == 34)
		{
			return new DataBlock(0);
		}
		else if(type == 63 || type == 68)
		{ //sign
			String[] text = ((Sign) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getLines();
			return new SignBlock(type, data, text);
		}
		else if(type == 54)
		{ //chest
			ItemStack[] items = ((Chest) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getInventory().getContents();
			return new ChestBlock(data, items);
		}
		else if(type == 61 || type == 62)
		{ //furnace
			ItemStack[] items = ((Furnace) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getInventory().getContents();
			return new FurnaceBlock(type, data, items);
		}
		else if(type == 23)
		{ //dispenser
			ItemStack[] items = ((Dispenser) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getInventory().getContents();
			return new DispenserBlock(type, data, items);
		}
		else if(type == 158)
		{ //dropper
			ItemStack[] items = ((Dropper) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getInventory().getContents();
			return new DispenserBlock(type, data, items);
		}
		else if(type == 25)
		{ //noteblock
			Byte note = ((org.bukkit.block.NoteBlock) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getRawNote();
			return new NoteBlock(data, note);
		}
		else if(type == 52)
		{ //spawner
			String mobtype = ((CreatureSpawner) world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState()).getCreatureTypeName();
			return new MobSpawnerBlock(data, mobtype);
		}

		return new DataBlock(type, data);
	}

	public DataBlock aSyncGetBlock(World world, Vec3 vec)
	{
		if(aSync)
		{
			if(after.containsKey(vec))
			{
				return after.get(vec);
			}
		}

		return getBlock(world, vec);
	}

	//No history change
	@SuppressWarnings("deprecation")
	public void setBlock(World world, Vec3 vec, DataBlock block)
	{
		world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).setTypeIdAndData(block.getTypeId(), (byte) block.getDataValue(), false);
		
		if(block instanceof SignBlock)
		{
			SignBlock signBlock = (SignBlock) block;
			String[] text = signBlock.getText();
			Sign sign = (Sign) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState();
			
			for(int i = 0; i < text.length; i++)
			{
				sign.setLine(i, text[i]);
			}
			
			sign.update();
		}
		else if(block instanceof ChestBlock)
		{
			ChestBlock chestBlock = (ChestBlock) block;
			ItemStack blankItem = new ItemStack(0);
			ItemStack[] items = chestBlock.getItems();
			Chest chest = (Chest) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState();
			
			for(byte i = 0; i <= 26; i++)
			{
				ItemStack item = items[i];
				
				if(item != null)
				{
					chest.getInventory().setItem(i, new ItemStack(Material.getMaterial((int) item.getTypeId()), item.getAmount()));
				}
				else
				{
					chest.getInventory().setItem(i, blankItem);
				}
			}
			
			chest.update();
		}
		else if(block instanceof FurnaceBlock)
		{
			FurnaceBlock furnaceBlock = (FurnaceBlock) block;
			ItemStack blankItem = new ItemStack(0);
			ItemStack[] items = furnaceBlock.getItems();
			Furnace furnace = (Furnace) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState();
			
			for(byte i = 0; i <= 1; i++)
			{
				ItemStack item = items[i];
				
				if(item != null)
				{
					furnace.getInventory().setItem(i, new ItemStack(Material.getMaterial((int) item.getTypeId()), item.getAmount()));
				}
				else
				{
					furnace.getInventory().setItem(i, blankItem);
				}
			}
		}
		else if(block instanceof DispenserBlock)
		{
			DispenserBlock dispenserBlock = (DispenserBlock) block;
			ItemStack blankItem = new ItemStack(0);
			ItemStack[] items = dispenserBlock.getItems();
			
			if(block.getTypeId() == 23)
			{
				Dispenser dispenser = (Dispenser) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState();
				
				for(byte i = 0; i <= 8; i++)
				{
					ItemStack item = items[i];
					
					if(item != null)
					{
						dispenser.getInventory().setItem(i, new ItemStack(Material.getMaterial((int) item.getTypeId()), item.getAmount()));
					}
					else
					{
						dispenser.getInventory().setItem(i, blankItem);
					}
				}
			}
			else
			{
				Dropper dropper = (Dropper) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState();
				
				for(byte i = 0; i <= 8; i++)
				{
					ItemStack item = items[i];
					
					if(item != null)
					{
						dropper.getInventory().setItem(i, new ItemStack(Material.getMaterial((int) item.getTypeId()), item.getAmount()));
					}
					else
					{
						dropper.getInventory().setItem(i, blankItem);
					}
				}
			}

		}
		else if(block instanceof NoteBlock)
		{
			byte note = ((NoteBlock) block).getNote();
			((org.bukkit.block.NoteBlock) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState()).setRawNote(note);
		}
		else if(block instanceof MobSpawnerBlock)
		{
			String mobtype = ((CreatureSpawner) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState()).getCreatureTypeName();
			((CreatureSpawner) world.getBlockAt((int) vec.getBlockX(), (int) vec.getBlockY(), (int) vec.getBlockZ()).getState()).setCreatureTypeByName(mobtype);
		}
	}

	//History change
	public void hSetBlock(World world, Vec3 vec, DataBlock block)
	{
		if(!before.containsKey(vec))
		{
			before.put(vec, getBlock(world, vec));
		}

		after.put(vec, block);
		aSyncSetBlock(world, vec, block);
	}

	private void aSyncSetBlock(World world, Vec3 vec, DataBlock block)
	{
		if(aSync)
		{
			//check if block is in list
			if(!block.isAir() && aSyncBlockslist.contains(block.getTypeId()) && getBlock(world, vec.tempAdd(0, -1, 0)).isAir())
			{
				aSyncBlocks.put(vec, block);
				
				if(aSyncGetBlock(world, vec) != block)
					return;
			}
			else if(block.isAir() && aSyncBlockslist.contains(getBlock(world, vec.tempAdd(0, 1, 0))))
			{
				setBlock(world, vec.tempAdd(0, 1, 0), new DataBlock(0)); // Prevent items from being dropped
				return;
			}
		}
		
		setBlock(world, vec, block);
	}

	public int setBlocks(World world, Region region, DataBlock blocktype)
	{
		int affected = 0;

		if(region instanceof CuboidRegion)
		{
			Vec3 min = region.getMinimumPoint();
			Vec3 max = region.getMaximumPoint();

			int minX = min.getBlockX();
			int minY = min.getBlockY();
			int minZ = min.getBlockZ();
			int maxX = max.getBlockX();
			int maxY = max.getBlockY();
			int maxZ = max.getBlockZ();

			for(int x = minX; x <= maxX; x++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					for(int z = minZ; z <= maxZ; z++)
					{
						Vec3 pos = new Vec3(x, y, z);
						hSetBlock(world, pos, blocktype);
						affected++;
					}
				}
			}
		}
		else
		{
			for(Vec3 pos : region)
			{
				hSetBlock(world, pos, blocktype);
				affected++;
			}
		}

		return affected;
	}

	public int setBlocks(World world, Region region, Pattern pattern)
	{
		int affected = 0;

		if(region instanceof CuboidRegion)
		{
			Vec3 min = region.getMinimumPoint();
			Vec3 max = region.getMaximumPoint();

			int minX = min.getBlockX();
			int minY = min.getBlockY();
			int minZ = min.getBlockZ();
			int maxX = max.getBlockX();
			int maxY = max.getBlockY();
			int maxZ = max.getBlockZ();

			for(int x = minX; x <= maxX; x++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					for(int z = minZ; z <= maxZ; z++)
					{
						Vec3 pos = new Vec3(x, y, z);
						hSetBlock(world, pos, pattern.next(pos));
						affected++;
					}
				}
			}
		}
		else
		{
			for(Vec3 pos : region)
			{
				hSetBlock(world, pos, pattern.next(pos));
				affected++;
			}
		}

		return affected;
	}

	public int replaceBlocks(World world, Region region, int fromBlockType, DataBlock toBlock)
	{
		int affected = 0;
		
		if(region instanceof CuboidRegion)
		{

			Vec3 min = region.getMinimumPoint();
			Vec3 max = region.getMaximumPoint();

			int minX = min.getBlockX();
			int minY = min.getBlockY();
			int minZ = min.getBlockZ();
			int maxX = max.getBlockX();
			int maxY = max.getBlockY();
			int maxZ = max.getBlockZ();

			for(int x = minX; x <= maxX; x++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					for(int z = minZ; z <= maxZ; z++)
					{
						Vec3 pos = new Vec3(x, y, z);
						int curBlockType = getBlock(world, pos).getTypeId();
						
						if(fromBlockType == -1 && curBlockType != 0 || curBlockType == fromBlockType)
						{
							hSetBlock(world, pos, toBlock);
							affected++;
						}
					}
				}
			}

		}
		
		return affected;
	}

	public String getIds(World world, Region region, int maxcount)
	{
		String ids = "";

		if(region.getSize() > maxcount)
		{
			return ids;
		}

		Vec3 min = region.getMinimumPoint();
		Vec3 max = region.getMaximumPoint();

		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		for(int x = minX; x <= maxX; x++)
		{
			for(int z = minZ; z <= maxZ; z++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					DataBlock block = getBlock(world, new Vec3(x, y, z));
					ids += (ChatColor.LIGHT_PURPLE + "" + block.getTypeId() + ":" + block.getDataValue() + ", ");
				}
			}
		}
		
		return ids;
	}

	public int stackClipboard(World world, Region region, Vec3 dir, int count)
	{
		int affected = 0;

		Vec3 min = region.getMinimumPoint();
		Vec3 max = region.getMaximumPoint();

		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		int xs = region.getWidth();
		int ys = region.getHeight();
		int zs = region.getLength();
		
		for(int x = minX; x <= maxX; x++)
		{
			for(int z = minZ; z <= maxZ; z++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					DataBlock block = getBlock(world, new Vec3(x, y, z));

					if(!block.isAir())
					{
						for(int i = 1; i <= count; i++)
						{
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

	public int moveClipboard(World world, Region region, Vec3 dir, int amount)
	{
		int affected = 0;

		DataBlock setblock = new DataBlock(0);

		Vec3 min = region.getMinimumPoint();
		Vec3 max = region.getMaximumPoint();
		Vec3 distance = dir.tempMultiply(amount);
		Vec3 newMin = min.tempAdd(distance);
		Vec3 newMax = max.tempAdd(distance);
		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		Map<Vec3, DataBlock> delayed = new HashMap<Vec3, DataBlock>();

		for(int x = minX; x <= maxX; x++)
		{
			for(int z = minZ; z <= maxZ; z++)
			{
				for(int y = minY; y <= maxY; y++)
				{
					Vec3 pos = new Vec3(x, y, z);
					Vec3 newPos = pos.tempAdd(distance);
					delayed.put(newPos, getBlock(world, pos));
					
					if(!(x >= newMin.getBlockX() && x <= newMax.getBlockX() && y >= newMin.getBlockY() && y <= newMax.getBlockY() && z >= newMin.getBlockZ() && z <= newMax.getBlockZ()))
					{
						hSetBlock(world, pos, setblock);
					}
				}
			}
		}
		
		for(Map.Entry<Vec3, DataBlock> movedBlock : delayed.entrySet())
		{
			hSetBlock(world, movedBlock.getKey(), movedBlock.getValue());
			affected++;
		}

		return affected;
	}

	public void undo(World world)
	{
		for(Map.Entry<Vec3, DataBlock> beforedata : before.entrySet())
		{
			Vec3 vec = beforedata.getKey();
			aSyncSetBlock(world, vec, beforedata.getValue());
		}
		
		finshAsyncBlocks(world);
	}

	//undo's what you have undoed
	public void redo(World world)
	{
		for(Map.Entry<Vec3, DataBlock> afterdata : after.entrySet())
		{
			Vec3 vec = afterdata.getKey();
			aSyncSetBlock(world, vec, afterdata.getValue());
		}
		
		finshAsyncBlocks(world);
	}

	public void enableAsync()
	{
		aSync = true;
	}

	public void disableAsync(World world)
	{
		if(aSync != false)
		{
			finshAsyncBlocks(world);
		}
		
		aSync = false;
	}

	public void finshAsyncBlocks(World world)
	{
		if(!aSync)
		{
			return;
		}

		for(Map.Entry<Vec3, DataBlock> entry : aSyncBlocks.entrySet())
		{
			Vec3 vec = entry.getKey();
			setBlock(world, vec, entry.getValue());
		}
	}

	public int size()
	{
		return before.size();
	}
}

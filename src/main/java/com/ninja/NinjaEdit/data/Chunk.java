package com.ninja.NinjaEdit.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.ListTag;
import org.jnbt.Tag;

import com.ninja.NinjaEdit.blocks.ChestBlock;
import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.blocks.SignBlock;
import com.ninja.NinjaEdit.blocks.TileEntityBlock;
import com.ninja.NinjaEdit.maths.Vec3;

public class Chunk
{
	private CompoundTag rootTag;
	private byte[] blocks;
	private byte[] data;
	private int rootX;
	private int rootZ;
	
	Map<Vec3, Map<String, Tag>> tileEntities;

	public Chunk(CompoundTag tag) throws DataException, InvalidFormatException
	{
		rootTag = tag;

		blocks = ((ByteArrayTag) getChildTag(rootTag.getValue(), "Blocks", ByteArrayTag.class)).getValue();
		data = ((ByteArrayTag) getChildTag(rootTag.getValue(), "Data", ByteArrayTag.class)).getValue();
		rootX = ((IntTag) getChildTag(rootTag.getValue(), "xPos", IntTag.class)).getValue();
		rootZ = ((IntTag) getChildTag(rootTag.getValue(), "zPos", IntTag.class)).getValue();

		if(blocks.length != 32768)
		{
			throw new InvalidFormatException("Chunk blocks byte array expected " + "to be 32,768 bytes; found " + blocks.length);
		}

		if(data.length != 16384)
		{
			throw new InvalidFormatException("Chunk block data byte array " + "expected to be 16,384 bytes; found " + data.length);
		}
	}

	public int getBlockID(Vec3 pos) throws DataException
	{
		int x = pos.getBlockX() - rootX * 16;
		int y = pos.getBlockY();
		int z = pos.getBlockZ() - rootZ * 16;
		int index = y + (z * 128 + (x * 128 * 16));

		try
		{
			return blocks[index];
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new DataException("Chunk does not contain position " + pos);
		}
	}

	/**
	 * Get the block data of a block.
	 *
	 * @param pos
	 * @return
	 * @throws DataException
	 */
	public int getBlockData(Vec3 pos) throws DataException
	{
		int x = pos.getBlockX() - rootX * 16;
		int y = pos.getBlockY();
		int z = pos.getBlockZ() - rootZ * 16;
		int index = y + (z * 128 + (x * 128 * 16));
		boolean shift = index % 2 == 0;
		index /= 2;

		try
		{
			if(!shift)
			{
				return (data[index] & 0xF0) >> 4;
			}
			else
			{
				return data[index] & 0xF;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new DataException("Chunk does not contain position " + pos);
		}
	}

	private void populateTileEntities() throws DataException, InvalidFormatException
	{
		List<Tag> tags = (List<Tag>) ((ListTag) getChildTag(rootTag.getValue(), "TileEntities", ListTag.class)).getValue();

		tileEntities = new HashMap<Vec3, Map<String, Tag>>();

		for(Tag tag : tags)
		{
			if(!(tag instanceof CompoundTag))
			{
				throw new InvalidFormatException("CompoundTag expected in TileEntities");
			}

			CompoundTag t = (CompoundTag) tag;

			int x = 0;
			int y = 0;
			int z = 0;

			Map<String, Tag> values = new HashMap<String, Tag>();

			for(Map.Entry<String, Tag> entry : t.getValue().entrySet())
			{
				if(entry.getKey().equals("x"))
				{
					if(entry.getValue() instanceof IntTag)
					{
						x = ((IntTag) entry.getValue()).getValue();
					}
				}
				else if(entry.getKey().equals("y"))
				{
					if(entry.getValue() instanceof IntTag)
					{
						y = ((IntTag) entry.getValue()).getValue();
					}
				}
				else if(entry.getKey().equals("z"))
				{
					if(entry.getValue() instanceof IntTag)
					{
						z = ((IntTag) entry.getValue()).getValue();
					}
				}

				values.put(entry.getKey(), entry.getValue());
			}

			Vec3 vec = new Vec3(x, y, z);
			tileEntities.put(vec, values);
		}
	}

	private Map<String, Tag> getBlockTileEntity(Vec3 pos) throws DataException, InvalidFormatException
	{
		if(tileEntities == null)
			populateTileEntities();

		return tileEntities.get(new Vec3(pos));
	}

	public DataBlock getBlock(Vec3 pos) throws DataException, InvalidFormatException
	{
		int id = getBlockID(pos);
		int data = getBlockData(pos);

		// Signs
		if(id == 63 || id == 68)
		{
			SignBlock block = new SignBlock(id, data);

			Map<String, Tag> tileEntity = getBlockTileEntity(pos);

			if(tileEntity != null)
			{
				((TileEntityBlock) block).fromTileEntityNBT(tileEntity);
			}

			return block;
			// Chests
		}
		else if(id == 54)
		{
			ChestBlock block = new ChestBlock();

			Map<String, Tag> tileEntity = getBlockTileEntity(pos);

			if(tileEntity != null)
			{
				((TileEntityBlock) block).fromTileEntityNBT(tileEntity);
			}

			return block;
		}
		else
		{
			return new DataBlock(id, data);
		}
	}

	public static Tag getChildTag(Map<String, Tag> items, String key, Class<?> expected) throws InvalidFormatException
	{
		if(!items.containsKey(key))
		{
			throw new InvalidFormatException("Missing a \"" + key + "\" tag");
		}
		
		Tag tag = items.get(key);
		
		if(!expected.isInstance(tag))
		{
			throw new InvalidFormatException(key + " tag is not of tag type " + expected.getName());
		}
		
		return tag;
	}
}

package com.ninja.NinjaEdit.blocks;

import java.util.HashMap;
import java.util.Map;

import org.jnbt.ShortTag;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import com.ninja.NinjaEdit.data.Chunk;
import com.ninja.NinjaEdit.data.DataException;
import com.ninja.NinjaEdit.data.InvalidFormatException;

public class MobSpawnerBlock extends DataBlock implements TileEntityBlock
{

	private String mobType;

	private short delay;

	public MobSpawnerBlock()
	{
		super(52);
		this.mobType = "Pig";
	}

	public MobSpawnerBlock(String mobType)
	{
		super(52);
		this.mobType = mobType;
	}

	public MobSpawnerBlock(int data)
	{
		super(52, data);
	}

	public MobSpawnerBlock(int data, String mobType)
	{
		super(52, data);
		this.mobType = mobType;
	}

	public String getMobType()
	{
		return mobType;
	}

	public void setMobType(String mobType)
	{
		this.mobType = mobType;
	}

	public short getDelay()
	{
		return delay;
	}

	public void setDelay(short delay)
	{
		this.delay = delay;
	}

	public String getTileEntityID()
	{
		return "MobSpawner";
	}

	public Map<String, Tag> toTileEntityNBT() throws DataException
	{
		Map<String, Tag> values = new HashMap<String, Tag>();
		values.put("EntityId", new StringTag("EntityId", mobType));
		values.put("Delay", new ShortTag("Delay", delay));
		return values;
	}

	public void fromTileEntityNBT(Map<String, Tag> values) throws DataException
	{
		if(values == null)
		{
			return;
		}

		Tag t = values.get("id");
		try
		{
			if(!(t instanceof StringTag) || !((StringTag) t).getValue().equals("MobSpawner"))
			{
				throw new DataException("'MobSpawner' tile entity expected");
			}

			StringTag mobTypeTag;
			mobTypeTag = (StringTag) Chunk.getChildTag(values, "EntityId", StringTag.class);
			ShortTag delayTag = (ShortTag) Chunk.getChildTag(values, "Delay", ShortTag.class);

			this.mobType = mobTypeTag.getValue();
			this.delay = delayTag.getValue();
		}
		catch(InvalidFormatException e)
		{
		}
	}
}

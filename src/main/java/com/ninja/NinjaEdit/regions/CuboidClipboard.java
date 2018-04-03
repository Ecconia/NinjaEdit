package com.ninja.NinjaEdit.regions;

import org.bukkit.World;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.maths.Vec3;

public class CuboidClipboard
{

	private DataBlock[][][] data;
	private Vec3 offset;
	private Vec3 origin;
	private Vec3 size;

	public CuboidClipboard(Vec3 size)
	{
		this.size = size;
		data = new DataBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
		origin = new Vec3();
		offset = new Vec3();
	}

	public CuboidClipboard(Vec3 size, Vec3 origin)
	{
		this.size = size;
		data = new DataBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
		this.origin = origin;
		offset = new Vec3();
	}

	public CuboidClipboard(Vec3 size, Vec3 origin, Vec3 offset)
	{
		this.size = size;
		data = new DataBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
		this.origin = origin;
		this.offset = offset;
	}

	public int getWidth()
	{
		return size.getBlockX();
	}

	public int getLength()
	{
		return size.getBlockZ();
	}

	public int getHeight()
	{
		return size.getBlockY();
	}

	public void copy(World world, EditHistory editHistory)
	{
		for(int x = 0; x < size.getBlockX(); x++)
		{
			for(int y = 0; y < size.getBlockY(); y++)
			{
				for(int z = 0; z < size.getBlockZ(); z++)
				{
					data[x][y][z] = editHistory.aSyncGetBlock(world, new Vec3(x, y, z).tempAdd(origin));
				}
			}
		}
	}

	public void paste(World world, EditHistory editHistory, Vec3 newOrigin)
	{
		place(world, editHistory, newOrigin.tempAdd(offset));
	}

	public void place(World world, EditHistory editHistory, Vec3 pos)
	{
		for(int x = 0; x < size.getBlockX(); x++)
		{
			for(int y = 0; y < size.getBlockY(); y++)
			{
				for(int z = 0; z < size.getBlockZ(); z++)
				{
					if(data[x][y][z].isAir())
						continue;
					editHistory.hSetBlock(world, new Vec3(x, y, z).tempAdd(pos), data[x][y][z]);
				}
			}
		}
	}
}

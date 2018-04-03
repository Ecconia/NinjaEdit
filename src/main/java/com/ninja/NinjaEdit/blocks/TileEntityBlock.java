package com.ninja.NinjaEdit.blocks;

import java.util.Map;

import org.jnbt.Tag;

import com.ninja.NinjaEdit.data.DataException;

public interface TileEntityBlock
{
	public String getTileEntityID();

	//store nbt data in the tile entity
	public Map<String, Tag> toTileEntityNBT() throws DataException;

	//get nbt from the title entity data
	public void fromTileEntityNBT(Map<String, Tag> values) throws DataException;
}

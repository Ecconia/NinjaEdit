package com.ninja.NinjaEdit.blocks.pattern;

import com.ninja.NinjaEdit.blocks.DataBlock;

public class BlockChance
{

	private DataBlock block;
	private double chance;

	public BlockChance(DataBlock block, double chance)
	{
		this.block = block;
		this.chance = chance;
	}

	public DataBlock getBlock()
	{
		return block;
	}

	public double getChance()
	{
		return chance;
	}
}

package com.ninja.NinjaEdit.blocks.pattern;

import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.maths.Vec3;

public class SingleBlockPattern implements Pattern
{

	private DataBlock block;

	public SingleBlockPattern(DataBlock block)
	{
		this.block = block;
	}

	/**
	 * Get next block.
	 * 
	 * @param pos
	 * @return
	 */
	public DataBlock next(Vec3 pos)
	{
		return block;
	}

	/**
	 * Get the block.
	 * 
	 * @return
	 */
	public DataBlock getBlock()
	{
		return block;
	}
}

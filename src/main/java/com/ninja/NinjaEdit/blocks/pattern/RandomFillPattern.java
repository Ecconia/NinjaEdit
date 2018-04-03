package com.ninja.NinjaEdit.blocks.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.maths.Vec3;

public class RandomFillPattern implements Pattern
{
	private static final Random random = new Random();

	private List<BlockChance> blocks;

	public RandomFillPattern(List<BlockChance> blocks)
	{
		double max = 0;

		for(BlockChance block : blocks)
		{
			max += block.getChance();
		}

		List<BlockChance> finalBlocks = new ArrayList<BlockChance>();

		double i = 0;

		for(BlockChance block : blocks)
		{
			double v = block.getChance() / max;
			i += v;
			finalBlocks.add(new BlockChance(block.getBlock(), i));
		}

		this.blocks = finalBlocks;
	}

	public DataBlock next(Vec3 pos)
	{
		double r = random.nextDouble();

		for(BlockChance block : blocks)
		{
			if(r <= block.getChance())
			{
				return block.getBlock();
			}
		}

		throw new RuntimeException("ProportionalFillPattern");
	}
}

package com.ninja.NinjaEdit.blocks.pattern;

import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.maths.Vec3;

public interface Pattern
{
	public DataBlock next(Vec3 pos);
}

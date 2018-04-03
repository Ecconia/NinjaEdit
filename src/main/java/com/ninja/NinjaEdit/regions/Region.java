package com.ninja.NinjaEdit.regions;

import com.ninja.NinjaEdit.maths.Vec3;

public interface Region extends Iterable<Vec3>
{
	public Vec3 getMinimumPoint();

	public Vec3 getMaximumPoint();

	public int getSize();

	public int getWidth();

	public int getHeight();

	public int getLength();

	public void expand(Vec3 change);

	public void contract(Vec3 change);
}
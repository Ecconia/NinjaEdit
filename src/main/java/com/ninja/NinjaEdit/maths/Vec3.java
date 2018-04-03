package com.ninja.NinjaEdit.maths;

public class Vec3
{
	private int x, y, z;

	public Vec3(Vec3 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	public Vec3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
//	public double getX() { return x; }
//	public double getY() { return y; }
//	public double getZ() { return z; }

	public int getBlockX()
	{
		return (int) x;
	}

	public int getBlockY()
	{
		return (int) y;
	}

	public int getBlockZ()
	{
		return (int) z;
	}

	public boolean equals(Vec3 other)
	{
		return other.x == x && other.y == y && other.z == z;
	}

	public void add(Vec3 other)
	{
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
	}

	public void add(int otherx, int othery, int otherz)
	{
		this.x += otherx;
		this.y += othery;
		this.z += otherz;
	}

	public void subtract(int otherx, int othery, int otherz)
	{
		this.x -= otherx;
		this.y -= othery;
		this.z -= otherz;
	}

	public void subtract(Vec3 other)
	{
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
	}

	public void multiply(int other)
	{
		this.x *= other;
		this.y *= other;
		this.z *= other;
	}

	public void multiply(Vec3 other)
	{
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
	}

	public void multiply(int otherx, int othery, int otherz)
	{
		this.x *= otherx;
		this.y *= othery;
		this.z *= otherz;
	}

	public Vec3 clampY(int min, int max)
	{
		return new Vec3(x, Math.max(min, Math.min(max, y)), z);
	}

	public Vec3 tempAdd(int x, int y, int z)
	{
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}

	public Vec3 tempAdd(Vec3 other)
	{
		return new Vec3(x + other.x, y + other.y, z + other.z);
	}

	public Vec3 tempSubtract(int x, int y, int z)
	{
		return new Vec3(this.x - x, this.y - y, this.z - z);
	}

	public Vec3 tempSubtract(Vec3 other)
	{
		return new Vec3(x - other.x, y - other.y, z - other.z);
	}

	public Vec3 tempMultiply(int other)
	{
		return new Vec3(x * other, y * other, z * other);
	}

	public Vec3 tempMultiply(Vec3 other)
	{
		return new Vec3(x * other.x, y * other.y, z * other.z);
	}

	public Vec3 tempMultiply(int x, int y, int z)
	{
		return new Vec3(this.x * x, this.y * y, this.z * z);
	}
}

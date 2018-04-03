package com.ninja.NinjaEdit.blocks;

public class Item
{
	private short id;

	private short damage;

	public Item(short id)
	{
		this.id = id;
		this.damage = 0;
	}

	public Item(short id, short damage)
	{
		this.id = id;
		this.damage = damage;
	}

	public short getID()
	{
		return id;
	}

	public void setID(short id)
	{
		this.id = id;
	}

	public short getDamage()
	{
		return damage;
	}

	public void setDamage(short damage)
	{
		this.damage = damage;
	}
}

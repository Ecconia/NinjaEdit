package com.ninja.NinjaEdit;

public class DataBlock
{
	private short id = 0;
	private char datavalue = 0;

	public DataBlock(int id)
	{
		this.id = (short) id;
	}

	public DataBlock(int id, int datavalue)
	{
		this.id = (short) id;
		this.datavalue = (char) datavalue;
	}

	public int getTypeId()
	{
		return id;
	}

	public int getDataValue()
	{
		return datavalue;
	}

	public void setType(int id)
	{
		this.id = (short) id;
	}

	public void setDataValue(int datavalue)
	{
		this.datavalue = (char) datavalue;
	}

	public boolean isAir()
	{
		return (id == 0);
	}
}

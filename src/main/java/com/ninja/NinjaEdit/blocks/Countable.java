package com.ninja.NinjaEdit.blocks;

public class Countable<T>
{
	private T id;
	
	/**
	 * Amount.
	 */
	private int amount;

	/**
	 * Construct the object.
	 * 
	 * @param id
	 * @param amount
	 */
	public Countable(T id, int amount)
	{
		this.id = id;
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public T getID()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setID(T id)
	{
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
}

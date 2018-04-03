package com.ninja.NinjaEdit.Exceptions;

public class NinjaException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected NinjaException()
	{
	}

	protected NinjaException(String msg)
	{
		super(msg);
	}
}
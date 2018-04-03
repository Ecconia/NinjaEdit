package com.ninja.NinjaEdit.blocks;

import java.util.HashMap;
import java.util.Map;

import org.jnbt.StringTag;
import org.jnbt.Tag;

import com.ninja.NinjaEdit.data.DataException;

public class SignBlock extends DataBlock implements TileEntityBlock
{
	private String[] text;

	public SignBlock(int type, int data)
	{
		super(type, data);
		this.text = new String[] { "", "", "", "" };
	}

	public SignBlock(int type, int data, String[] text)
	{
		super(type, data);
		this.text = text;
	}

	public void setText(String[] text)
	{
		this.text = text;
	}

	public String[] getText()
	{
		return text;
	}

	@Override
	public String getTileEntityID()
	{
		return "Sign";
	}

	//store nbt data in the tile entity
	@Override
	public Map<String, Tag> toTileEntityNBT()
	{
		Map<String, Tag> values = new HashMap<String, Tag>();
		values.put("Line1", new StringTag("Line1", text[0]));
		values.put("Line2", new StringTag("Line2", text[1]));
		values.put("Line3", new StringTag("Line3", text[2]));
		values.put("Line4", new StringTag("Line4", text[3]));
		return values;
	}

	//get nbt from the title entity data
	@Override
	public void fromTileEntityNBT(Map<String, Tag> values) throws DataException
	{
		if(values == null)
		{
			return;
		}

		Tag t;

		text = new String[] { "", "", "", "" };

		t = values.get("id");
		
		if(!(t instanceof StringTag) || !((StringTag) t).getValue().equals("Sign"))
		{
			throw new DataException("'Sign' tile entity expected");
		}

		t = values.get("Line1");
		
		if(t instanceof StringTag)
		{
			text[0] = ((StringTag) t).getValue();
		}

		t = values.get("Line2");
		
		if(t instanceof StringTag)
		{
			text[1] = ((StringTag) t).getValue();
		}

		t = values.get("Line3");
		
		if(t instanceof StringTag)
		{
			text[2] = ((StringTag) t).getValue();
		}

		t = values.get("Line4");
		
		if(t instanceof StringTag)
		{
			text[3] = ((StringTag) t).getValue();
		}
	}
}

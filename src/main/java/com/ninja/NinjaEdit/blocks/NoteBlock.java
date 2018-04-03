package com.ninja.NinjaEdit.blocks;

import java.util.HashMap;
import java.util.Map;

import org.jnbt.ByteTag;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import com.ninja.NinjaEdit.data.DataException;

public class NoteBlock extends DataBlock implements TileEntityBlock
{

	private byte note;

	public NoteBlock(int data)
	{
		super(25, data);
		this.note = 0;
	}

	public NoteBlock(int data, byte note)
	{
		super(25, data);
		this.note = note;
	}

	public byte getNote()
	{
		return note;
	}

	public void setNote(byte note)
	{
		this.note = note;
	}

	public String getTileEntityID()
	{
		return "Music";
	}

	public Map<String, Tag> toTileEntityNBT() throws DataException
	{
		Map<String, Tag> values = new HashMap<String, Tag>();
		values.put("note", new ByteTag("note", note));
		return values;
	}

	public void fromTileEntityNBT(Map<String, Tag> values) throws DataException
	{
		if(values == null)
		{
			return;
		}

		Tag t;

		t = values.get("id");
		if(!(t instanceof StringTag) || !((StringTag) t).getValue().equals("Music"))
		{
			throw new DataException("'Music' tile entity expected");
		}

		t = values.get("note");
		if(t instanceof ByteTag)
		{
			note = ((ByteTag) t).getValue();
		}
	}
}

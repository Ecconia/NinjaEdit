package com.ninja.NinjaEdit.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jnbt.ByteTag;
import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.ShortTag;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import com.ninja.NinjaEdit.data.Chunk;
import com.ninja.NinjaEdit.data.DataException;
import com.ninja.NinjaEdit.data.InvalidFormatException;

public class ChestBlock extends DataBlock implements TileEntityBlock, ContainerBlock
{
	//the items
	private ItemStack[] items;

	public ChestBlock()
	{
		super(54);
		items = new ItemStack[27];
	}

	public ChestBlock(int data)
	{
		super(54, data);
		items = new ItemStack[27];
	}

	public ChestBlock(int data, ItemStack[] items)
	{
		super(54, data);
		this.items = items;
	}

	public ItemStack[] getItems()
	{
		return items;
	}

	public void setItems(ItemStack[] items)
	{
		this.items = items;
	}

	public String getTileEntityID()
	{
		return "Chest";
	}

	@SuppressWarnings("deprecation")
	public Map<String, Tag> toTileEntityNBT() throws DataException
	{
		List<Tag> itemsList = new ArrayList<Tag>();
		
		for(int i = 0; i < items.length; i++)
		{
			ItemStack item = items[i];
			
			if(item != null)
			{
				Map<String, Tag> data = new HashMap<String, Tag>();
				CompoundTag itemTag = new CompoundTag("Items", data);
				data.put("id", new ShortTag("id", (short) item.getTypeId()));
				data.put("Damage", new ShortTag("Damage", item.getDurability()));
				data.put("Count", new ByteTag("Count", (byte) item.getAmount()));
				data.put("Slot", new ByteTag("Slot", (byte) i));
				itemsList.add(itemTag);
			}
		}
		
		Map<String, Tag> values = new HashMap<String, Tag>();
		values.put("Items", new ListTag("Items", CompoundTag.class, itemsList));
		return values;
	}

	@SuppressWarnings("deprecation")
	public void fromTileEntityNBT(Map<String, Tag> values) throws DataException
	{
		if(values == null)
		{
			return;
		}

		ItemStack[] newItems = new ItemStack[27];

		Tag t = values.get("id");
		
		if(!(t instanceof StringTag) || !((StringTag) t).getValue().equals("Chest"))
		{
			throw new DataException("'Chest' tile entity expected");
		}

		ListTag items;
		
		try
		{
			items = (ListTag) Chunk.getChildTag(values, "Items", ListTag.class);

			for(Tag tag : items.getValue())
			{
				if(!(tag instanceof CompoundTag))
				{
					throw new DataException("CompoundTag expected as child tag of Chest's Items");
				}

				CompoundTag item = (CompoundTag) tag;
				Map<String, Tag> itemValues = item.getValue();

				short id = (Short) ((ShortTag) Chunk.getChildTag(itemValues, "id", ShortTag.class)).getValue();
				short damage = (Short) ((ShortTag) Chunk.getChildTag(itemValues, "Damage", ShortTag.class)).getValue();
				byte count = (Byte) ((ByteTag) Chunk.getChildTag(itemValues, "Count", ByteTag.class)).getValue();
				byte slot = (Byte) ((ByteTag) Chunk.getChildTag(itemValues, "Slot", ByteTag.class)).getValue();

				newItems[slot] = new ItemStack(Material.getMaterial(id), count);
				newItems[slot].setDurability(damage);
			}
		}
		catch(InvalidFormatException e)
		{
		}

		this.items = newItems;
	}
}

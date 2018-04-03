package com.ninja.NinjaEdit.blocks;

import org.bukkit.inventory.ItemStack;

public interface ContainerBlock
{
	public ItemStack[] getItems();

	public void setItems(ItemStack[] items);
}

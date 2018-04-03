package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandWand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.wand"))
			{
				if(p.getInventory().firstEmpty() != -1)
				{
					p.getInventory().addItem(new ItemStack(Material.WOOD_AXE, 1));
					p.sendMessage(ChatColor.LIGHT_PURPLE + "Wand given.");
				}
				else
				{
					p.sendMessage(ChatColor.LIGHT_PURPLE + "Inventory full.");
				}
				
				return true;
			}
			else
			{
				p.sendMessage(ChatColor.RED + "You do not have the permission to this command.");
			}
		}
		
		return false;
	}
}

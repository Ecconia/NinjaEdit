package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.NinjaEdit;

public class CommandUp implements CommandExecutor
{

	NinjaEdit inst;

	public CommandUp(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(p.hasPermission("NinjaEdit.up"))
			{
				Location loc = p.getLocation();
				int value = -1;
				if(args.length != 0)
				{
					try
					{
						value = Integer.parseInt(args[0]);
					}
					catch(NumberFormatException e)
					{
						p.sendMessage(ChatColor.DARK_RED + "You have to put in a number!");
						return true;
					}
				}

				if(((loc.getBlockY() + value) - 1) > 256)
				{
					loc.setY(255);
				}
				loc.add(0, value, 0);
				//set glass
				loc.getWorld().getBlockAt(loc).setTypeId(20);
				loc.add(0, 1, 0);
				p.teleport(loc);
				p.sendMessage(ChatColor.LIGHT_PURPLE + "Whoosh!");
				return true;
			}
			else
			{
				p.sendMessage(ChatColor.DARK_RED + "I'm sorry but you don't have the permission to use this command!");
				return true;
			}
		}

		return false;
	}

}

package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;
import com.ninja.NinjaEdit.regions.Region;

public class CommandGetId implements CommandExecutor
{
	NinjaEdit inst;
	
	int MAX_NUMBER = 9;

	public CommandGetId(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("NinjaEdit.getid"))
			{
				PlayerSession session = inst.getSession(p.getName());
				
				if(inst.getSession(p.getName()).pos1 != null && inst.getSession(p.getName()).pos1 != null)
				{
					Region region = session.getRegion();
					int size = region.getSize();
					
					if(size > MAX_NUMBER)
					{
						p.sendMessage(ChatColor.LIGHT_PURPLE + "Your selection is too big(" + size + ") the limit is " + MAX_NUMBER + ".");
						return true;
					}
					
					EditHistory editHistory = new EditHistory();
					p.sendMessage(ChatColor.LIGHT_PURPLE + editHistory.getIds(p.getWorld(), region, size));
					return true;
				}
				else
				{
					p.sendMessage(ChatColor.RED + "You need to make a selection first.");
					return true;
				}
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

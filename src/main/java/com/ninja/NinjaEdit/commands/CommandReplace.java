package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;
import com.ninja.NinjaEdit.Exceptions.UnknownItemException;
import com.ninja.NinjaEdit.blocks.DataBlock;

public class CommandReplace implements CommandExecutor
{
	NinjaEdit inst;

	public CommandReplace(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.replace"))
			{
				String name = p.getName();
				PlayerSession session = inst.getSession(name);
				EditHistory editHistory = new EditHistory();
				int from = 0;
				DataBlock to = null;
				editHistory.enableAsync();
				
				try
				{
					if(args.length == 1)
					{
						from = -1;
						to = inst.getBlock(args[0]);
					}
					else if(args.length >= 1)
					{
						from = inst.getBlock(args[0]).getTypeId();
						to = inst.getBlock(args[1]);
					}
				}
				catch(UnknownItemException e)
				{
					p.sendMessage(ChatColor.RED + "You must have a valid id!");
				}
				
				if(inst.getSession(name).pos1 != null && inst.getSession(name).pos1 != null)
				{
					int count = editHistory.replaceBlocks(p.getWorld(), session.getRegion(), from, to);

					session.remember(editHistory);
					p.sendMessage(ChatColor.LIGHT_PURPLE + "" + count + " blocks(s) have been replaced.");
					editHistory.finshAsyncBlocks(p.getWorld());
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
				p.sendMessage(ChatColor.RED + "Too few arguments");
				return true;
			}
		}
		return false;
	}
}

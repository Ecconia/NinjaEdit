package com.ninja.NinjaEdit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;
import com.ninja.NinjaEdit.Exceptions.UnknownItemException;
import com.ninja.NinjaEdit.blocks.pattern.Pattern;
import com.ninja.NinjaEdit.blocks.pattern.SingleBlockPattern;

import net.md_5.bungee.api.ChatColor;

public class CommandSet implements CommandExecutor
{
	NinjaEdit inst;

	public CommandSet(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.set"))
			{
				if(args.length != 0)
				{
					String name = p.getName();
					PlayerSession session = inst.getSession(name);
					EditHistory editHistory = new EditHistory();
					Pattern pattern;
					editHistory.enableAsync();
					
					try
					{
						pattern = inst.getBlockPattern(args[0]);
					}
					catch(UnknownItemException e)
					{
						p.sendMessage(ChatColor.DARK_RED + "You have to put in an id!");
						return true;

					}
					
					if(inst.getSession(name).pos1 != null && inst.getSession(name).pos1 != null)
					{
						int count;
						
						if(pattern instanceof SingleBlockPattern)
						{
							count = editHistory.setBlocks(p.getWorld(), session.getRegion(), (SingleBlockPattern) pattern);
						}
						else
						{
							count = editHistory.setBlocks(p.getWorld(), session.getRegion(), pattern);
						}
						
						session.remember(editHistory);
						p.sendMessage(ChatColor.LIGHT_PURPLE + "Operation completed (" + count + " blocks affected).");
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
		}
		return false;
	}
}

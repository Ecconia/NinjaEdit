package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;

public class CommandRedo implements CommandExecutor
{

	NinjaEdit inst;

	public CommandRedo(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(p.hasPermission("NinjaEdit.redo"))
			{
				PlayerSession session = inst.getSession(p.getName());
				if(session.redo(p.getWorld()))
				{
					p.sendMessage(ChatColor.LIGHT_PURPLE + "Undo successful.");
					return true;
				}
				else
				{
					p.sendMessage(ChatColor.RED + "Nothing to redo.");
					return true;
				}
			}
		}
		return false;
	}
}

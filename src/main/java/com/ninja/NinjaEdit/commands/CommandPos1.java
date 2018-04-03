package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.NinjaEdit;

public class CommandPos1 implements CommandExecutor
{
	NinjaEdit inst;

	public CommandPos1(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.Pos1"))
			{
				Location pos1 = p.getLocation();
				inst.getSession(p.getName()).setPos1(pos1);
				p.sendMessage(ChatColor.LIGHT_PURPLE + "First position set to (" + pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ() + ") (" + inst.getSession(p.getName()).getSelectionSize(p.getName()) + ")");
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

package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.NinjaEdit;

public class CommandPos2 implements CommandExecutor
{
	NinjaEdit inst;

	public CommandPos2(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.Pos2"))
			{
				Location pos2 = p.getLocation();
				inst.getSession(p.getName()).setPos2(pos2);
				p.sendMessage(ChatColor.LIGHT_PURPLE + "Second position set to (" + pos2.getBlockX() + ", " + pos2.getBlockY() + ", " + pos2.getBlockZ() + ") (" + inst.getSession(p.getName()).getSelectionSize(p.getName()) + ")");
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

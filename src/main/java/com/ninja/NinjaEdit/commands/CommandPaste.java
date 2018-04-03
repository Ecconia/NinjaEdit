package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;
import com.ninja.NinjaEdit.maths.Vec3;

public class CommandPaste implements CommandExecutor
{
	NinjaEdit inst;

	public CommandPaste(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(p.hasPermission("ninjaedit.paste"))
			{
				PlayerSession session = inst.getSession(p.getName());
				EditHistory editHistory = new EditHistory();
				editHistory.enableAsync();
				Vec3 pos = new Vec3((int) Math.floor(p.getLocation().getX()), (int) Math.floor(p.getLocation().getY()), (int) Math.floor(p.getLocation().getZ()));
				session.getClipboard().paste(p.getWorld(), editHistory, pos);
				session.remember(editHistory);
				editHistory.finshAsyncBlocks(p.getWorld());
				p.sendMessage(ChatColor.LIGHT_PURPLE + "The clipboard has been pasted at (" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")");
				return true;
			}
			else
			{
				p.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
				return true;
			}
		}
		return false;
	}
}

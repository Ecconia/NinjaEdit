package com.ninja.NinjaEdit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.EditHistory;
import com.ninja.NinjaEdit.NinjaEdit;
import com.ninja.NinjaEdit.PlayerSession;
import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.maths.Vec3;
import com.ninja.NinjaEdit.regions.CuboidClipboard;
import com.ninja.NinjaEdit.regions.Region;

public class CommandCut implements CommandExecutor
{

	NinjaEdit inst;

	public CommandCut(NinjaEdit inst)
	{
		this.inst = inst;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(p.hasPermission("NinjaEdit.cut"))
			{
				PlayerSession session = inst.getSession(p.getName());
				EditHistory editHistory = new EditHistory();
				Region region = session.getRegion();
				Vec3 min = region.getMinimumPoint();
				Vec3 max = region.getMaximumPoint();
				Vec3 pos = new Vec3((int) Math.floor(p.getLocation().getX()), (int) Math.floor(p.getLocation().getY()), (int) Math.floor(p.getLocation().getZ()));
				CuboidClipboard clipboard = new CuboidClipboard(max.tempSubtract(min).tempAdd(new Vec3(1, 1, 1)), min, min.tempSubtract(pos));
				clipboard.copy(p.getWorld(), editHistory);
				session.setClipboard(clipboard);
				editHistory.setBlocks(p.getWorld(), session.getRegion(), new DataBlock(0));
				p.sendMessage(ChatColor.LIGHT_PURPLE + "" + session.getRegion().getSize() + " block(s) were copied.");
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

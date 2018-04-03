package com.ninja.NinjaEdit;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ninja.NinjaEdit.maths.Vec3;
import com.ninja.NinjaEdit.regions.CuboidClipboard;
import com.ninja.NinjaEdit.regions.CuboidRegion;
import com.ninja.NinjaEdit.regions.Region;

public class PlayerSession
{

	public static final int MAX_HISTORY_SIZE = 15;

	public Location pos1, pos2;
	private Region region;
	private List<EditHistory> history = new LinkedList<EditHistory>();
	private int historyPointer = 0;
	private CuboidClipboard clipboard;

	NinjaEdit inst;

	public void remember(EditHistory editHistory)
	{
		// Don't store anything if no changes were made
		if(editHistory.size() == 0)
		{
			return;
		}

		// Destroy any sessions after this undo point
		while(historyPointer < history.size())
		{
			history.remove(historyPointer);
		}
		history.add(editHistory);
		while(history.size() > MAX_HISTORY_SIZE)
		{
			history.remove(0);
		}
		historyPointer = history.size();
	}

	public boolean undo(World world)
	{
		historyPointer--;
		if(historyPointer >= 0)
		{
			history.get(historyPointer).undo(world);
			return true;
		}
		else
		{
			historyPointer = 0;
			return false;
		}
	}

	public boolean redo(World world)
	{
		if(historyPointer < history.size())
		{
			history.get(historyPointer).redo(world);
			historyPointer++;
			return true;
		}

		return false;
	}

	public void setPos1(Location loc)
	{
		pos1 = loc;
		if(pos1 != null && pos2 != null)
		{
			region = new CuboidRegion(new Vec3(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new Vec3(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
		}
	}

	public void setPos2(Location loc)
	{
		pos2 = loc;
		if(pos1 != null && pos2 != null)
		{
			region = new CuboidRegion(new Vec3(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), new Vec3(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
		}
	}

	public int getSelectionSize(String name)
	{
		if(Bukkit.getPlayer(name) != null)
		{
			Location temp = Bukkit.getPlayer(name).getLocation();
			if(pos1 == null || pos2 == null)
			{
				//Not all position set
				return 0;
			}

			temp.setX(1 + Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX()));
			temp.setY(1 + Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY()));
			temp.setZ(1 + Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ()));

			//If this below is used. It is successful
			return temp.getBlockX() * temp.getBlockY() * temp.getBlockZ();

		}
		else
		{
			//Error
			return 0;
		}
	}

	public void learnRegionChanges(World world)
	{
		if(region instanceof CuboidRegion)
		{
			CuboidRegion cuboidRegion = (CuboidRegion) region;
			pos1 = new Location(world, cuboidRegion.getPos1().getBlockY(), cuboidRegion.getPos1().getBlockZ(), cuboidRegion.getPos1().getBlockX());
			pos2 = new Location(world, cuboidRegion.getPos2().getBlockY(), cuboidRegion.getPos2().getBlockZ(), cuboidRegion.getPos2().getBlockX());
		}
	}

	public Region getRegion()
	{
		return region;
	}

	public CuboidClipboard getClipboard()
	{
		return clipboard;
	}

	public void setClipboard(CuboidClipboard clipboard)
	{
		this.clipboard = clipboard;
	}

	public Vec3 getPlacementPosition(Player p)
	{
		if(pos1.equals(null))
		{
			return new Vec3(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
		}
		return new Vec3(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ());
	}
}

package com.ninja.NinjaEdit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.ninja.NinjaEdit.Exceptions.UnknownItemException;
import com.ninja.NinjaEdit.blocks.DataBlock;
import com.ninja.NinjaEdit.blocks.pattern.BlockChance;
import com.ninja.NinjaEdit.blocks.pattern.Pattern;
import com.ninja.NinjaEdit.blocks.pattern.RandomFillPattern;
import com.ninja.NinjaEdit.blocks.pattern.SingleBlockPattern;
import com.ninja.NinjaEdit.commands.CommandContract;
import com.ninja.NinjaEdit.commands.CommandCopy;
import com.ninja.NinjaEdit.commands.CommandCut;
import com.ninja.NinjaEdit.commands.CommandExpand;
import com.ninja.NinjaEdit.commands.CommandGetId;
import com.ninja.NinjaEdit.commands.CommandMove;
import com.ninja.NinjaEdit.commands.CommandPaste;
import com.ninja.NinjaEdit.commands.CommandPos1;
import com.ninja.NinjaEdit.commands.CommandPos2;
import com.ninja.NinjaEdit.commands.CommandRedo;
import com.ninja.NinjaEdit.commands.CommandReplace;
import com.ninja.NinjaEdit.commands.CommandSet;
import com.ninja.NinjaEdit.commands.CommandStack;
import com.ninja.NinjaEdit.commands.CommandUndo;
import com.ninja.NinjaEdit.commands.CommandUp;
import com.ninja.NinjaEdit.commands.CommandWand;

public class NinjaEdit extends JavaPlugin
{
	// All the players
	private HashMap<String, PlayerSession> sessions = new HashMap<String, PlayerSession>();

	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new NinjaEditListener(this), this);
		initCommands();
	}

	@Override
	public void onDisable()
	{
	}

	@SuppressWarnings("deprecation")
	public DataBlock getBlock(String id, boolean allAllowed) throws UnknownItemException
	{
		DataBlock blocktype;

		String[] args0 = id.split("\\|");
		String[] args1 = args0[0].split(":", 2);
		String testID = args1[0];
		int typeid = 0;
		int data;
		
		try
		{
			data = args1.length > 1 ? Integer.parseInt(args1[1]) : 0;
			
			if(data > 15 || data < 0)
			{
				data = 0;
			}

		}
		catch(NumberFormatException e)
		{
			data = 0;
		}

		try
		{
			typeid = Integer.parseInt(id);
			blocktype = new DataBlock(typeid, data);
		}
		catch(NumberFormatException e)
		{
			if(testID.equalsIgnoreCase("quartz"))
				blocktype = new DataBlock(155, data);
			else if(testID.equalsIgnoreCase("gold"))
				blocktype = new DataBlock(41, data);
			else if(testID.equalsIgnoreCase("iron"))
				blocktype = new DataBlock(42, data);
			else if(testID.equalsIgnoreCase("diamond"))
				blocktype = new DataBlock(57, data);
			else if(testID.equalsIgnoreCase("lapis"))
				blocktype = new DataBlock(22, data);
			else if(testID.equalsIgnoreCase("brick"))
				blocktype = new DataBlock(45, data);
			else if(testID.equalsIgnoreCase("melon"))
				blocktype = new DataBlock(103, data);
			else if(testID.equalsIgnoreCase("emerald"))
				blocktype = new DataBlock(133, data);
			else if(testID.equalsIgnoreCase("hay"))
				blocktype = new DataBlock(170, data);
			else if(testID.equalsIgnoreCase("coal"))
				blocktype = new DataBlock(173, data);
			else if(testID.equalsIgnoreCase("purpur"))
				blocktype = new DataBlock(201, data);
			else if(testID.equalsIgnoreCase("bone"))
				blocktype = new DataBlock(216, data);
			else
			{
				blocktype = new DataBlock(Bukkit.getUnsafe().getMaterialFromInternalName(testID).getId(), data);
			}
		}
		
		return blocktype;
	}

	public DataBlock getBlock(String id) throws UnknownItemException
	{
		return getBlock(id, false);
	}

	public Pattern getBlockPattern(String list) throws UnknownItemException
	{
		String[] items = list.split(",");

		if(items.length == 1)
		{
			return new SingleBlockPattern(getBlock(items[0]));
		}

		List<BlockChance> blockChances = new ArrayList<BlockChance>();

		for(String s : items)
		{
			DataBlock block;

			double chance = 1;
			block = getBlock(s);

			blockChances.add(new BlockChance(block, chance));
		}

		return new RandomFillPattern(blockChances);
	}

	public Set<Integer> getBlockIDs(String list) throws UnknownItemException
	{
		String[] items = list.split(",");
		Set<Integer> blocks = new HashSet<Integer>();
		
		for(String s : items)
		{
			blocks.add(getBlock(s).getTypeId());
		}
		
		return blocks;
	}

	public PlayerSession getSession(String player)
	{
		if(sessions.containsKey(player))
		{
			return sessions.get(player);
		}
		else
		{
			PlayerSession session = new PlayerSession();
			sessions.put(player, session);
			return session;
		}
	}

	private void initCommands()
	{
		this.getCommand("/pos1").setExecutor(new CommandPos1(this));
		this.getCommand("/pos2").setExecutor(new CommandPos2(this));
		this.getCommand("/set").setExecutor(new CommandSet(this));
		this.getCommand("/replace").setExecutor(new CommandReplace(this));
		this.getCommand("/undo").setExecutor(new CommandUndo(this));
		this.getCommand("/redo").setExecutor(new CommandRedo(this));
		this.getCommand("/up").setExecutor(new CommandUp(this));
		this.getCommand("/copy").setExecutor(new CommandCopy(this));
		this.getCommand("/paste").setExecutor(new CommandPaste(this));
		this.getCommand("/cut").setExecutor(new CommandCut(this));
		this.getCommand("/stack").setExecutor(new CommandStack(this));
		this.getCommand("/wand").setExecutor(new CommandWand());
		this.getCommand("/move").setExecutor(new CommandMove(this));
		this.getCommand("/getid").setExecutor(new CommandGetId(this));
		this.getCommand("/expand").setExecutor(new CommandExpand(this));
		this.getCommand("/contract").setExecutor(new CommandContract(this));
	}
}

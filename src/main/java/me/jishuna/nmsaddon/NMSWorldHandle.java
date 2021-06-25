package me.jishuna.nmsaddon;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;

public class NMSWorldHandle extends BukkitWorldHandle {

	@Override
	public BlockData createBlockData(String data) {
		return new NMSBlockData(((CraftBlockData)Bukkit.createBlockData(data)).getState());
	}
}

package me.jishuna.nmsaddon;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class NMSBlockType implements BlockType {
	private final Block delegate;
	
	public NMSBlockType(Block delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object getHandle() {
		return delegate;
	}

	@Override
	public BlockData getDefaultData() {
		return BukkitBlockData.newInstance(CraftBlockData.fromData(delegate.getBlockData()));
	}

	@Override
	public boolean isSolid() {
		return delegate.getBlockData().l();
	}

	@Override
	public boolean isWater() {
		return delegate == Blocks.A;
	}

}

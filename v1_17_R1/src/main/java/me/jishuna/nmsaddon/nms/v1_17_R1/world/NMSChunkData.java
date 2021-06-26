package me.jishuna.nmsaddon.nms.v1_17_R1.world;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.chunk.ProtoChunk;

public class NMSChunkData implements ChunkData {
	private final ProtoChunk delegate;

	public NMSChunkData(ProtoChunk delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
	delegate.setType(new BlockPosition(x, y, z), ((CraftBlockData) blockData.getHandle()).getState(), false);
	}

	@Override
	public @NotNull BlockData getBlockData(int i, int i1, int i2) {
	    return BukkitAdapter.adapt(CraftBlockData.fromData(delegate.getType(new BlockPosition(i, i1, i2))));
	}

	@Override
	public Object getHandle() {
		return delegate;
	}

	@Override
	public int getMaxHeight() {
		return delegate.getMaxBuildHeight();
	}

}

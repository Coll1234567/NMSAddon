package me.jishuna.nmsaddon;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.BukkitWorld;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.GeneratorAccess;

public class NMSWorld extends BukkitWorld implements World {

	public NMSWorld(RegionLimitedWorldAccess delegate) {
		super(delegate.getMinecraftWorld().getWorld());
		this.delegate = delegate;
	}

	private final RegionLimitedWorldAccess delegate;

	@Override
	public long getSeed() {
		return delegate.getMinecraftWorld().getSeed();
	}

	@Override
	public int getMaxHeight() {
		return delegate.getMaxBuildHeight();
	}

	@Override
	public ChunkGenerator getGenerator() {
		return new BukkitChunkGenerator(delegate.getMinecraftWorld().generator);
	}

	@Override
	public Chunk getChunkAt(int var1, int var2) {
		return new NMSChunk(delegate);
	}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		return new NMSBlock(delegate, new BlockPosition(x, y, z));
	}

	@Override
	public int getMinHeight() {
		return delegate.getMinBuildHeight();
	}

	public GeneratorAccess getDelegate() {
		return delegate;
	}

}

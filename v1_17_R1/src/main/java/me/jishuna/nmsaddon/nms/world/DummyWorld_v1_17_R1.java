package me.jishuna.nmsaddon.nms.world;

import java.util.UUID;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.BukkitWorld;

import net.minecraft.server.level.WorldServer;

@SuppressWarnings("resource")
public class DummyWorld_v1_17_R1 extends BukkitWorld implements World {
	private final WorldServer delegate;

	public DummyWorld_v1_17_R1(WorldServer delegate) {
		super(delegate.getWorld());
		this.delegate = delegate;
	}

	@Override
	public long getSeed() {
		return delegate.getSeed();
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
	public String getName() {
		return delegate.getMinecraftWorld().getWorld().getName();
	}

	@Override
	public UUID getUID() {
		return delegate.getMinecraftWorld().uuid;
	}

	@Override
	public Block getBlockAt(int i, int i1, int i2) {
		throw new UnsupportedOperationException("getBlockAt called on dummy world");
	}

	@Override
	public Entity spawnEntity(Location location, EntityType entityType) {
		throw new UnsupportedOperationException("spawnEntity called on dummy world");
	}

	@Override
	public int getMinHeight() {
		return delegate.getMinBuildHeight();
	}

	@Override
	public org.bukkit.World getHandle() {
		return delegate.getMinecraftWorld().getWorld();
	}

	public WorldServer getDelegate() {
		return delegate;
	}
}

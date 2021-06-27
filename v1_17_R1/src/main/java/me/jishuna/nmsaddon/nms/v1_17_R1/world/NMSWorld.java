package me.jishuna.nmsaddon.nms.v1_17_R1.world;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;

import me.jishuna.nmsaddon.nms.v1_17_R1.block.NMSBlock;
import me.jishuna.nmsaddon.nms.v1_17_R1.entity.NMSEntity;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.entity.EntityTypes;

@SuppressWarnings("resource")
public class NMSWorld extends BukkitWorld implements World {
	private final RegionLimitedWorldAccess delegate;

	public NMSWorld(RegionLimitedWorldAccess delegate) {
		super(delegate.getMinecraftWorld().getWorld());
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
		return new NMSBlock(delegate, new BlockPosition(i, i1, i2));
	}

	@Override
	public Entity spawnEntity(Location location, EntityType entityType) {
		org.bukkit.entity.EntityType type = ((BukkitEntityType) entityType).getHandle();
		EntityTypes<?> NMSType = IRegistry.Y
				.a(ResourceKey.a(IRegistry.l, CraftNamespacedKey.toMinecraft(type.getKey())));
		net.minecraft.world.entity.Entity entity = NMSType.a(delegate.getMinecraftWorld());
		entity.setPosition(location.getX(), location.getY(), location.getZ());
		delegate.addEntity(entity);
		return new NMSEntity(this, entity);
	}

	@Override
	public int getMinHeight() {
		return delegate.getMinBuildHeight();
	}

	@Override
	public org.bukkit.World getHandle() {
		return delegate.getMinecraftWorld().getWorld();
	}

	public RegionLimitedWorldAccess getDelegate() {
		return delegate;
	}
}

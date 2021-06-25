package me.jishuna.nmsaddon;

import java.io.File;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.entity.EntityTypes;

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
        return 255;
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
    public boolean isChunkGenerated(int i, int i1) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int i, int i1) {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int i, int i1, int i2) {
        return new NMSBlock(delegate, new BlockPosition(i, i1, i2));
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public org.bukkit.World getHandle() {
        return delegate.getMinecraftWorld().getWorld();
    }

    public RegionLimitedWorldAccess getDelegate() {
        return delegate;
    }
}

package me.jishuna.nmsaddon.nms.world;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;

import me.jishuna.nmsaddon.nms.block.NMSBlock_v1_17_R1;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;

public class NMSChunk_v1_17_R1 implements Chunk {
    private final RegionLimitedWorldAccess delegate;

    public NMSChunk_v1_17_R1(RegionLimitedWorldAccess delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }

    @Override
    public int getX() {
        return delegate.a().b;
    }

    @Override
    public int getZ() {
    	 return delegate.a().c;
    }

    @Override
    public World getWorld() {
        return new NMSWorld_v1_17_R1(delegate);
    }

    @Override
    public Block getBlock(int i, int i1, int i2) {
        return new NMSBlock_v1_17_R1(delegate, new BlockPosition((getX() << 4) + i, i1, (getZ() << 4)+ i2));
    }

    @Override
    public void setBlock(int i, int i1, int i2, @NotNull BlockData blockData) {
        delegate.setTypeAndData(new BlockPosition((getX() << 4) + i, i1, (getZ() << 4) + i2), ((CraftBlockData) blockData.getHandle()).getState(), 0);
    }

    @Override
    public @NotNull BlockData getBlockData(int i, int i1, int i2) {
        return getBlock(i, i1, i2).getBlockData();
    }
}

package me.jishuna.nmsaddon.data;

import com.dfsek.terra.api.platform.block.data.Waterlogged;

import me.jishuna.nmsaddon.NMSBlockData;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;

public class NMSWaterlogged extends NMSBlockData implements Waterlogged {
    public NMSWaterlogged(IBlockData delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return delegate.get(BlockProperties.C);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        super.delegate = delegate.set(BlockProperties.C, waterlogged);
    }
}

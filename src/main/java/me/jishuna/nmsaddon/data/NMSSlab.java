package me.jishuna.nmsaddon.data;

import com.dfsek.terra.api.platform.block.data.Slab;

import me.jishuna.nmsaddon.NMSAdapter;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;

public class NMSSlab extends NMSWaterlogged implements Slab {
    public NMSSlab(IBlockData delegate) {
        super(delegate);
    }

    @Override
    public Type getType() {
        return NMSAdapter.adapt(delegate.get(BlockProperties.bd));
    }

    @Override
    public void setType(Type type) {
        delegate = delegate.set(BlockProperties.bd, NMSAdapter.adapt(type));
    }
   
}

package me.jishuna.nmsaddon.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Stairs;

import me.jishuna.nmsaddon.NMSAdapter;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;

public class NMSStairs extends NMSWaterlogged implements Stairs {
    public NMSStairs(IBlockData delegate) {
        super(delegate);
    }	

    @Override
    public Shape getShape() {
        return NMSAdapter.adapt(getHandle().get(BlockProperties.be));
    }

    @Override
    public void setShape(Shape shape) {
        super.delegate = getHandle().set(BlockProperties.be, NMSAdapter.adapt(shape));
    }

    @Override
    public Half getHalf() {
        return NMSAdapter.adapt(getHandle().get(BlockProperties.ac));
    }

    @Override
    public void setHalf(Half half) {
        super.delegate = getHandle().set(BlockProperties.ac, NMSAdapter.adapt(half));
    }

    @Override
    public BlockFace getFacing() {
        return NMSAdapter.adapt(getHandle().get(BlockProperties.P));
    }

    @Override
    public void setFacing(BlockFace facing) {
        super.delegate = getHandle().set(BlockProperties.P, NMSAdapter.adapt(facing));
    }
}

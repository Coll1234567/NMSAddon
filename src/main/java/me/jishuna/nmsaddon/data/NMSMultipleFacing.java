package me.jishuna.nmsaddon.data;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.MultipleFacing;

import me.jishuna.nmsaddon.NMSBlockData;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;

public class NMSMultipleFacing extends NMSBlockData implements MultipleFacing {
    public NMSMultipleFacing(IBlockData delegate) {
        super(delegate);
    }

    @Override
    public Set<BlockFace> getFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.get(BlockProperties.J)) set.add(BlockFace.NORTH);
        if(delegate.get(BlockProperties.L)) set.add(BlockFace.SOUTH);
        if(delegate.get(BlockProperties.K)) set.add(BlockFace.EAST);
        if(delegate.get(BlockProperties.M)) set.add(BlockFace.WEST);
        if(delegate.b(BlockProperties.H) && delegate.get(BlockProperties.H)) set.add(BlockFace.UP);
        if(delegate.b(BlockProperties.I) && delegate.get(BlockProperties.I)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public void setFace(BlockFace face, boolean facing) {
        switch(face) {
            case NORTH:
                delegate = delegate.set(BlockProperties.J, facing);
                break;
            case SOUTH:
                delegate = delegate.set(BlockProperties.L, facing);
                break;
            case EAST:
                delegate = delegate.set(BlockProperties.K, facing);
                break;
            case WEST:
                delegate = delegate.set(BlockProperties.M, facing);
                break;
            case UP:
                delegate = delegate.set(BlockProperties.H, facing);
                break;
            case DOWN:
                delegate = delegate.set(BlockProperties.I, facing);
                break;
        }
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        Set<BlockFace> set = new HashSet<>();
        if(delegate.b(BlockProperties.J)) set.add(BlockFace.NORTH);
        if(delegate.b(BlockProperties.L)) set.add(BlockFace.SOUTH);
        if(delegate.b(BlockProperties.K)) set.add(BlockFace.EAST);
        if(delegate.b(BlockProperties.M)) set.add(BlockFace.WEST);
        if(delegate.b(BlockProperties.H)) set.add(BlockFace.UP);
        if(delegate.b(BlockProperties.I)) set.add(BlockFace.DOWN);
        return set;
    }

    @Override
    public boolean hasFace(BlockFace f) {
        return getFaces().contains(f);
    }
}

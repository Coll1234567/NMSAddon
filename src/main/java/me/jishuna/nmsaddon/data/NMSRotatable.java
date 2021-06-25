package me.jishuna.nmsaddon.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Rotatable;

import me.jishuna.nmsaddon.NMSBlockData;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;

public class NMSRotatable extends NMSBlockData implements Rotatable {
    public NMSRotatable(IBlockData delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getRotation() {
        int r = delegate.get(BlockProperties.aW);
        switch(r) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.SOUTH_SOUTH_WEST;
            case 2:
                return BlockFace.SOUTH_WEST;
            case 3:
                return BlockFace.WEST_SOUTH_WEST;
            case 4:
                return BlockFace.WEST;
            case 5:
                return BlockFace.WEST_NORTH_WEST;
            case 6:
                return BlockFace.NORTH_WEST;
            case 7:
                return BlockFace.NORTH_NORTH_WEST;
            case 8:
                return BlockFace.NORTH;
            case 9:
                return BlockFace.NORTH_NORTH_EAST;
            case 10:
                return BlockFace.NORTH_EAST;
            case 11:
                return BlockFace.EAST_NORTH_EAST;
            case 12:
                return BlockFace.EAST;
            case 13:
                return BlockFace.EAST_SOUTH_EAST;
            case 14:
                return BlockFace.SOUTH_EAST;
            case 15:
                return BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalArgumentException("Unknown rotation " + r);
        }
    }

    @Override
    public void setRotation(BlockFace face) {
        switch(face) {
            case UP:
            case DOWN:
                throw new IllegalArgumentException("Illegal rotation " + face);
            case SOUTH:
                delegate = delegate.set(BlockProperties.aW, 0);
                return;
            case SOUTH_SOUTH_WEST:
				delegate = delegate.set(BlockProperties.aW, 1);
                return;
            case SOUTH_WEST:
                delegate = delegate.set(BlockProperties.aW, 2);
                return;
            case WEST_SOUTH_WEST:
                delegate = delegate.set(BlockProperties.aW, 3);
                return;
            case WEST:
                delegate = delegate.set(BlockProperties.aW, 4);
                return;
            case WEST_NORTH_WEST:
                delegate = delegate.set(BlockProperties.aW, 5);
                return;
            case NORTH_WEST:
                delegate = delegate.set(BlockProperties.aW, 6);
                return;
            case NORTH_NORTH_WEST:
                delegate = delegate.set(BlockProperties.aW, 7);
                return;
            case NORTH:
                delegate = delegate.set(BlockProperties.aW, 8);
                return;
            case NORTH_NORTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 9);
                return;
            case NORTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 10);
                return;
            case EAST_NORTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 11);
                return;
            case EAST:
                delegate = delegate.set(BlockProperties.aW, 12);
                return;
            case EAST_SOUTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 13);
                return;
            case SOUTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 14);
                return;
            case SOUTH_SOUTH_EAST:
                delegate = delegate.set(BlockProperties.aW, 15);
                return;
        }
    }
}

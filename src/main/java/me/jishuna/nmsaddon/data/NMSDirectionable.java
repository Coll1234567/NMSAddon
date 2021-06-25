package me.jishuna.nmsaddon.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;

import me.jishuna.nmsaddon.NMSAdapter;
import me.jishuna.nmsaddon.NMSBlockData;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockStateDirection;

public class NMSDirectionable extends NMSBlockData implements Directional {
	private final BlockStateDirection property;

	public NMSDirectionable(IBlockData delegate, BlockStateDirection property) {
		super(delegate);
		this.property = property;
	}

	@Override
	public BlockFace getFacing() {
		switch (delegate.get(property)) {
		case d:
			return BlockFace.SOUTH;
		case a:
			return BlockFace.DOWN;
		case b:
			return BlockFace.UP;
		case f:
			return BlockFace.EAST;
		case e:
			return BlockFace.WEST;
		case c:
			return BlockFace.NORTH;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void setFacing(BlockFace facing) {
		delegate = delegate.set(property, NMSAdapter.adapt(facing));
	}
}

package me.jishuna.nmsaddon;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;

import net.minecraft.core.IRegistry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;

public class NMSBlockData implements BlockData {
	protected IBlockData delegate;

	public NMSBlockData(IBlockData delegate) {
		this.delegate = delegate;
	}

	@Override
	public BlockType getBlockType() {
		return new NMSBlockType(delegate);
	}

	@Override
	public boolean matches(BlockData other) {
		return delegate.getBlock() == ((NMSBlockData) other).delegate.getBlock();
	}

	@Override
	public BlockData clone() {
		try {
			return (NMSBlockData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public String getAsString() {
		StringBuilder data = new StringBuilder(IRegistry.W.getKey(delegate.getBlock()).toString());
		//System.out.println("????");
		if (!delegate.getStateMap().isEmpty()) {
			data.append('[');
//			data.append(delegate.getStateMap().entrySet().stream().map(StateAccessor.getPropertyMapPrinter())
//					.collect(Collectors.joining(",")));
			data.append(']');
		}
		return data.toString();
	}

	@Override
	public boolean isAir() {
		return delegate.isAir();
	}

	@Override
	public boolean isStructureVoid() {
		return delegate.getBlock() == Blocks.jb;
	}

	@Override
	public IBlockData getHandle() {
		return delegate;
	}
}

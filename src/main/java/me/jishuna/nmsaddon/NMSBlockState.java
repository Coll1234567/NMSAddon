package me.jishuna.nmsaddon;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.block.entity.TileEntity;

public class NMSBlockState implements BlockState {
	private final RegionLimitedWorldAccess delegate;
	private final BlockPosition position;

	public NMSBlockState(RegionLimitedWorldAccess delegate2, BlockPosition position) {
		this.delegate = delegate2;
		this.position = position;
	}
	
	//TODO 3 child classes for special blocks

	@Override
	public Object getHandle() {
		return delegate;
	}

	@Override
	public Block getBlock() {
		return new NMSBlock(delegate, position);
	}

	@Override
	public BlockData getBlockData() {
		   return BukkitAdapter.adapt(CraftBlockData.fromData(delegate.getType(position)));
	}

	@Override
	public int getX() {
		return position.getX();
	}

	@Override
	public int getY() {
		return position.getY();
	}

	@Override
	public int getZ() {
		return position.getZ();
	}

	@Override
	public boolean update(boolean var1) {
		TileEntity tileEntity = delegate.getTileEntity(position);

		if (tileEntity.hasWorld()) {
			tileEntity.getWorld().getChunkAtWorldCoords(position).setTileEntity(tileEntity);
		}
		return true;
	}

}

package me.jishuna.nmsaddon.nms.v1_17_R1.block.state;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;

import me.jishuna.nmsaddon.nms.v1_17_R1.inventory.NMSInventory;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.block.entity.TileEntityContainer;

public class NMSContainer extends NMSBlockState implements Container {
	private final TileEntityContainer delegate;

	protected NMSContainer(RegionLimitedWorldAccess world, BlockPosition position) {
		super(world, position);
		this.delegate = (TileEntityContainer) world.getTileEntity(position);
	}

	@Override
	public Inventory getInventory() {
		return new NMSInventory(delegate);
	}
}

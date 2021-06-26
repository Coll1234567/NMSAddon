package me.jishuna.nmsaddon.nms.block.state;

import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.inventory.Inventory;

import me.jishuna.nmsaddon.nms.inventory.NMSInventory_v1_17_R1;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.block.entity.TileEntityContainer;

public class NMSContainer_v1_17_R1 extends NMSBlockState_v1_17_R1 implements Container {

	protected NMSContainer_v1_17_R1(RegionLimitedWorldAccess delegate2, BlockPosition position) {
		super(delegate2, position);
	}

	@Override
	public Inventory getInventory() {
		return new NMSInventory_v1_17_R1(((TileEntityContainer) getDelegate().getTileEntity(getPosition())));
	}
}

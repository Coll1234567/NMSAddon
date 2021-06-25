package me.jishuna.nmsaddon;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.bukkit.world.block.BukkitBlockTypeAndItem;

import net.minecraft.world.level.block.state.IBlockData;

public class NMSBlockType extends BukkitBlockTypeAndItem {
	@Override
	public ItemStack newItemStack(int amount) {
		return null;
	}

	private final IBlockData delegate;

	public NMSBlockType(IBlockData delegate) {
		super(CraftBlockData.fromData(delegate).getMaterial());
		this.delegate = delegate;
	}

	@Override
	public BlockData getDefaultData() {
		return new NMSBlockData(delegate);
	}
}

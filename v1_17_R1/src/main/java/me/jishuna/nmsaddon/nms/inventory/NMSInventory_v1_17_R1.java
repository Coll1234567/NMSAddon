package me.jishuna.nmsaddon.nms.inventory;

import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

import com.dfsek.terra.api.platform.inventory.Inventory;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;

import net.minecraft.world.level.block.entity.TileEntityContainer;

public class NMSInventory_v1_17_R1 implements Inventory {
	private final TileEntityContainer container;

	public NMSInventory_v1_17_R1(TileEntityContainer container) {
		this.container = container;
	}

	@Override
	public Object getHandle() {
		return container;
	}

	@Override
	public ItemStack getItem(int var1) {
		org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(container.getItem(var1));
		return item.getType().isAir() ? null : new BukkitItemStack(item);
	}

	@Override
	public int getSize() {
		return container.getSize();
	}

	@Override
	public void setItem(int var1, ItemStack var2) {
		container.setItem(var1, CraftItemStack.asNMSCopy(((BukkitItemStack) var2).getHandle()));
	}
}

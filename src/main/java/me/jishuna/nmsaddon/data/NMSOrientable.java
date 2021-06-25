package me.jishuna.nmsaddon.data;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.data.Orientable;

import me.jishuna.nmsaddon.NMSAdapter;
import me.jishuna.nmsaddon.NMSBlockData;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;

public class NMSOrientable extends NMSBlockData implements Orientable {
	private final BlockStateEnum<EnumDirection.EnumAxis> property;

	public NMSOrientable(IBlockData delegate, BlockStateEnum<EnumDirection.EnumAxis> property) {
		super(delegate);
		this.property = property;
	}

	@Override
	public Set<Axis> getAxes() {
		return Arrays.stream(Axis.values()).collect(Collectors.toSet());
	}

	@Override
	public Axis getAxis() {
		return NMSAdapter.adapt(getHandle().get(property));
	}

	@Override
	public void setAxis(Axis axis) {
		delegate = delegate.set(property, NMSAdapter.adapt(axis));
	}
}

package me.jishuna.nmsaddon;

import java.util.Arrays;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Bisected;
import com.dfsek.terra.api.platform.block.data.Slab;
import com.dfsek.terra.api.platform.block.data.Stairs;

import me.jishuna.nmsaddon.data.NMSDirectionable;
import me.jishuna.nmsaddon.data.NMSMultipleFacing;
import me.jishuna.nmsaddon.data.NMSOrientable;
import me.jishuna.nmsaddon.data.NMSRotatable;
import me.jishuna.nmsaddon.data.NMSSlab;
import me.jishuna.nmsaddon.data.NMSStairs;
import me.jishuna.nmsaddon.data.NMSWaterlogged;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.BlockPropertyHalf;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.BlockPropertyStairsShape;

public class NMSAdapter {
	public static NMSBlockData adapt(IBlockData state) {
		if (state.b(BlockProperties.be))
			return new NMSStairs(state);

		if (state.b(BlockProperties.bd))
			return new NMSSlab(state);

		if (state.b(BlockProperties.F))
			return new NMSOrientable(state, BlockProperties.F);
		if (state.b(BlockProperties.G))
			return new NMSOrientable(state, BlockProperties.G);

		if (state.b(BlockProperties.aW))
			return new NMSRotatable(state);

		if (state.b(BlockProperties.N))
			return new NMSDirectionable(state, BlockProperties.N);
		if (state.b(BlockProperties.O))
			return new NMSDirectionable(state, BlockProperties.O);
		if (state.b(BlockProperties.P))
			return new NMSDirectionable(state, BlockProperties.P);

		if (state.s()
				.containsAll(Arrays.asList(BlockProperties.J, BlockProperties.K, BlockProperties.L, BlockProperties.M)))
			return new NMSMultipleFacing(state);
		if (state.b(BlockProperties.C))
			return new NMSWaterlogged(state);
		return new NMSBlockData(state);
	}

	public static Bisected.Half adapt(BlockPropertyHalf half) {
		switch (half) {
		case b:
			return Bisected.Half.BOTTOM;
		case a:
			return Bisected.Half.TOP;
		default:
			throw new IllegalStateException();
		}
	}

	public static BlockPropertyHalf adapt(Bisected.Half half) {
		switch (half) {
		case TOP:
			return BlockPropertyHalf.a;
		case BOTTOM:
			return BlockPropertyHalf.b;
		default:
			throw new IllegalStateException();
		}
	}

	public static Slab.Type adapt(BlockPropertySlabType type) {
		switch (type) {
		case b:
			return Slab.Type.BOTTOM;
		case a:
			return Slab.Type.TOP;
		case c:
			return Slab.Type.DOUBLE;
		default:
			throw new IllegalStateException();
		}
	}

	public static BlockPropertySlabType adapt(Slab.Type type) {
		switch (type) {
		case DOUBLE:
			return BlockPropertySlabType.c;
		case TOP:
			return BlockPropertySlabType.a;
		case BOTTOM:
			return BlockPropertySlabType.b;
		default:
			throw new IllegalStateException();
		}
	}

	public static Stairs.Shape adapt(BlockPropertyStairsShape shape) {
		switch (shape) {
		case e:
			return Stairs.Shape.OUTER_RIGHT;
		case c:
			return Stairs.Shape.INNER_RIGHT;
		case d:
			return Stairs.Shape.OUTER_LEFT;
		case b:
			return Stairs.Shape.INNER_LEFT;
		case a:
			return Stairs.Shape.STRAIGHT;
		default:
			throw new IllegalStateException();
		}
	}

	public static BlockPropertyStairsShape adapt(Stairs.Shape shape) {
		switch (shape) {
		case STRAIGHT:
			return BlockPropertyStairsShape.a;
		case INNER_LEFT:
			return BlockPropertyStairsShape.b;
		case OUTER_LEFT:
			return BlockPropertyStairsShape.d;
		case INNER_RIGHT:
			return BlockPropertyStairsShape.c;
		case OUTER_RIGHT:
			return BlockPropertyStairsShape.e;
		default:
			throw new IllegalStateException();
		}
	}

	public static BlockFace adapt(EnumDirection direction) {
		switch (direction) {
		case b:
			return BlockFace.DOWN;
		case a:
			return BlockFace.UP;
		case e:
			return BlockFace.WEST;
		case f:
			return BlockFace.EAST;
		case c:
			return BlockFace.NORTH;
		case d:
			return BlockFace.SOUTH;
		default:
			throw new IllegalStateException();
		}
	}

	public static EnumDirection adapt(BlockFace face) {
		switch (face) {
		case NORTH:
			return EnumDirection.c;
		case WEST:
			return EnumDirection.e;
		case SOUTH:
			return EnumDirection.d;
		case EAST:
			return EnumDirection.f;
		case UP:
			return EnumDirection.a;
		case DOWN:
			return EnumDirection.b;
		default:
			throw new IllegalArgumentException("Illegal direction: " + face);
		}
	}

	public static Axis adapt(EnumDirection.EnumAxis axis) {
		switch (axis) {
		case a:
			return Axis.X;
		case b:
			return Axis.Y;
		case c:
			return Axis.Z;
		default:
			throw new IllegalStateException();
		}
	}

	public static EnumDirection.EnumAxis adapt(Axis axis) {
		switch (axis) {
		case Z:
			return EnumDirection.EnumAxis.c;
		case Y:
			return EnumDirection.EnumAxis.b;
		case X:
			return EnumDirection.EnumAxis.a;
		default:
			throw new IllegalStateException();
		}
	}
}

package me.jishuna.nmsaddon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitChunkGeneratorWrapperWrapper extends BukkitChunkGeneratorWrapper {

	private static Field needsLoadField;
	private static Method loadMethod;

	static {
		try {
			needsLoadField = BukkitChunkGeneratorWrapper.class.getDeclaredField("needsLoad");
			needsLoadField.setAccessible(true);

			loadMethod = BukkitChunkGeneratorWrapper.class.getDeclaredMethod("load",
					com.dfsek.terra.api.platform.world.World.class);
			loadMethod.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public BukkitChunkGeneratorWrapperWrapper(BukkitChunkGeneratorWrapper wrapper) {
		super(wrapper.getHandle());
	}

	@Override
	public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z,
			@NotNull BiomeGrid biome) {
		com.dfsek.terra.api.platform.world.World bukkitWorld = BukkitAdapter.adapt(world);

		try {
			if (needsLoadField.getBoolean(this)) {
				loadMethod.invoke(this, bukkitWorld);
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return (ChunkData) getHandle().generateChunkData(bukkitWorld, random, x, z,
				new BukkitChunkGenerator.BukkitChunkData(createChunkData(world))).getHandle();
	}

}

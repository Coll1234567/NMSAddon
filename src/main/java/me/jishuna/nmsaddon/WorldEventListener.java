package me.jishuna.nmsaddon;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.TreeType;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.generator.CustomChunkGenerator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.world.TerraWorldLoadEvent;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.pack.ConfigPack;

import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class WorldEventListener implements EventListener {
	private final TerraPlugin plugin;

	public WorldEventListener(TerraPlugin plugin) {
		this.plugin = plugin;
	}

	@Global
	public void onWorldLoad(TerraWorldLoadEvent event) throws NoSuchFieldException, IllegalAccessException {
		if (event.getWorld().getWorld() instanceof DummyWorld)
			return;
		CraftWorld world = ((CraftWorld) event.getWorld().getWorld().getHandle());
		System.out.println("Now");

		// logger.info("Injecting into world \"" + world.getName() + "\".");

		Field populators = CraftWorld.class.getDeclaredField("populators");
		populators.setAccessible(true);
		((List<?>) populators.get(world)).clear(); // populators bad

		// logger.info("Removed populators.");

		WorldServer worldServer = world.getHandle();
		CustomChunkGenerator chunkGenerator = (CustomChunkGenerator) worldServer.getChunkProvider().getChunkGenerator();

		Field customDelegate = CustomChunkGenerator.class.getDeclaredField("delegate");
		customDelegate.setAccessible(true);

		final long seed = world.getSeed();
		final ConfigPack pack = event.getPack();
//		final IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().l
//				.b(IRegistry.aO);

		NMSAddon.getNMSAddon().getWorldMap().put(worldServer.getDimensionManager(), event.getWorld());

		NMSChunkGenerator2 generator = new NMSChunkGenerator2(worldServer,
				(ChunkGenerator) customDelegate.get(chunkGenerator), world.getGenerator(), plugin, event.getPack());

		Field finalGenerator = ChunkProviderServer.class.getDeclaredField("d");
		finalGenerator.setAccessible(true);

		finalGenerator.set(worldServer.getChunkProvider(), generator);

		Field pcmGen = PlayerChunkMap.class.getDeclaredField("r");
		pcmGen.setAccessible(true);

		pcmGen.set(worldServer.getChunkProvider().a, generator);
	}

	@Global
	@Priority(Priority.HIGHEST)
	public void injectTrees(ConfigPackPreLoadEvent event) {
		for (TreeType value : TreeType.values()) {
			event.getPack().getTreeRegistry().addUnchecked(BukkitAdapter.TREE_TRANSFORMER.translate(value),
					new NMSTree(plugin, value)); // overwrite trees with evil trees
		}
	}

}
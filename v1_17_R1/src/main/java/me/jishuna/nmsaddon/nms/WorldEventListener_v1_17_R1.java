package me.jishuna.nmsaddon.nms;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.world.TerraWorldLoadEvent;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.pack.ConfigPack;

import me.jishuna.nmsaddon.nms.generation.NMSChunkGenerator_v1_17_R1;
import me.jishuna.nmsaddon.nms.generation.TerraBiomeSource_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.NMSTree_v1_17_R1;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.biome.BiomeBase;

public class WorldEventListener_v1_17_R1 implements EventListener {
	private final TerraPlugin plugin;

	public WorldEventListener_v1_17_R1(TerraPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("resource")
	@Global
	public void onWorldLoad(TerraWorldLoadEvent event) throws NoSuchFieldException, IllegalAccessException {
		if (event.getWorld().getWorld() instanceof DummyWorld)
			return;
		CraftWorld world = ((CraftWorld) event.getWorld().getWorld().getHandle());

		// logger.info("Injecting into world \"" + world.getName() + "\".");

		Field populators = CraftWorld.class.getDeclaredField("populators");
		populators.setAccessible(true);
		((List<?>) populators.get(world)).clear(); // populators bad

		// logger.info("Removed populators.");

		WorldServer worldServer = world.getHandle();

		final long seed = world.getSeed();
		final ConfigPack pack = event.getPack();
		final IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().l
				.b(IRegistry.aO);

		NMSAdapter_v1_17_R1.getInstance().getWorldMap().put(worldServer.getWorld().getUID(), event.getWorld());

		NMSChunkGenerator_v1_17_R1 generator = new NMSChunkGenerator_v1_17_R1(new TerraBiomeSource_v1_17_R1(biomeRegistry, seed, pack), seed,
				pack);

		generator.setWorldId(worldServer.getWorld().getUID());

		Field finalGenerator = ChunkProviderServer.class.getDeclaredField("d");
		finalGenerator.setAccessible(true);

		finalGenerator.set(worldServer.getChunkProvider(), generator);

		Field pcmGen = PlayerChunkMap.class.getDeclaredField("r");
		pcmGen.setAccessible(true);

		pcmGen.set(worldServer.getChunkProvider().a, generator);
	}

	@SuppressWarnings("deprecation")
	@Global
	@Priority(Priority.HIGHEST)
	public void injectTrees(ConfigPackPreLoadEvent event) {
		for (TreeType value : TreeType.values()) {
			event.getPack().getTreeRegistry().addUnchecked(BukkitAdapter.TREE_TRANSFORMER.translate(value),
					new NMSTree_v1_17_R1(plugin, value)); // overwrite trees with evil trees
		}
	}

}
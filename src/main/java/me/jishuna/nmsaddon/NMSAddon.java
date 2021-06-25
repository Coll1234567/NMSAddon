package me.jishuna.nmsaddon;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;

import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration2;
import net.minecraft.world.level.levelgen.placement.WorldGenDecorator;

@Addon("NMS-Addon")
@Author("jishuna")
@Version("0.1.0")
public class NMSAddon extends TerraAddon implements EventListener {
	private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();
	private final Map<DimensionManager, TerraWorld> worldMap = new HashMap<>();

	@Inject
	private TerraPlugin plugin;

	private static NMSAddon nmsaddon;

	@Override
	public void initialize() {
		plugin.getEventManager().registerListener(this, this);
		plugin.getEventManager().registerListener(this, new WorldEventListener(plugin));
		nmsaddon = this;
		
		//((TerraBukkitPlugin)this.plugin).setHandle(new NMSWorldHandle());
	}

	public TerraPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(TerraPlugin plugin) {
		this.plugin = plugin;
	}

	@Priority(Priority.LOWEST)
	@Global
	public void injectTrees(ConfigPackPreLoadEvent event) {
		PreLoadCompatibilityOptions template = new PreLoadCompatibilityOptions();
		try {
			event.loadTemplate(template);
		} catch (ConfigException e) {
			e.printStackTrace();
		}

		templates.put(event.getPack(), Pair.of(template, null));
	}

	public TerraWorld getWorld(DimensionManager type) {
		TerraWorld world = worldMap.get(type);
		if (world == null)
			throw new IllegalArgumentException("No world exists with dimension type " + type);
		return world;
	}

	@Priority(Priority.HIGHEST)
	@Global
	public void createInjectionOptions(ConfigPackPostLoadEvent event) {
		PostLoadCompatibilityOptions template = new PostLoadCompatibilityOptions();

		try {
			event.loadTemplate(template);
		} catch (ConfigException e) {
			e.printStackTrace();
		}

		templates.get(event.getPack()).setRight(template);

		//
		// logger.info("Registering biomes...");
		IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO);
		for (ConfigPack pack : plugin.getConfigRegistry().entries()) {
			pack.getBiomeRegistry().forEach((id, biome) -> Utils.registerOrOverwrite(biomeRegistry, IRegistry.aO,
					new MinecraftKey("terra", Utils.createBiomeID(pack, id)), Utils.createBiome(biome, pack)));
		}
		// logger.info("Biomes registered.");

		//biomeRegistry.forEach(biome -> biomeRegistry.c(biome).ifPresent(key -> System.out.println(key.toString())));
	}

	public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
		return templates;
	}

	public Map<DimensionManager, TerraWorld> getWorldMap() {
		return worldMap;
	}

	public static NMSAddon getNMSAddon() {
		return nmsaddon;
	}

}

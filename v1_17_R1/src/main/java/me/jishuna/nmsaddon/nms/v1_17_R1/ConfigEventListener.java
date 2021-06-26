package me.jishuna.nmsaddon.nms.v1_17_R1;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.config.pack.ConfigPack;

import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.biome.BiomeBase;

public class ConfigEventListener implements EventListener {
	private final TerraPlugin plugin;

	public ConfigEventListener(TerraPlugin plugin) {
		this.plugin = plugin;
	}

	@Priority(Priority.LOWEST)
	@Global
	public void onPreLoad(ConfigPackPreLoadEvent event) {
		PreLoadCompatibilityOptions template = new PreLoadCompatibilityOptions();
		try {
			event.loadTemplate(template);
		} catch (ConfigException e) {
			e.printStackTrace();
		}

		NMSAdapter_v1_17_R1.getInstance().getTemplates().put(event.getPack(), Pair.of(template, null));
	}

	@SuppressWarnings("resource")
	@Priority(Priority.HIGHEST)
	@Global
	public void onPostLoad(ConfigPackPostLoadEvent event) {
		PostLoadCompatibilityOptions template = new PostLoadCompatibilityOptions();

		try {
			event.loadTemplate(template);
		} catch (ConfigException e) {
			e.printStackTrace();
		}

		NMSAdapter_v1_17_R1.getInstance().getTemplates().get(event.getPack()).setRight(template);

		//
		// logger.info("Registering biomes...");
		IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO);
		for (ConfigPack pack : plugin.getConfigRegistry().entries()) {
			pack.getBiomeRegistry().forEach((id, biome) -> NMSUtils.registerOrOverwrite(biomeRegistry, IRegistry.aO,
					new MinecraftKey("terra", NMSUtils.createBiomeID(pack, id)), NMSUtils.createBiome(biome, pack)));
		}
		// logger.info("Biomes registered.");

		// biomeRegistry.forEach(biome -> biomeRegistry.c(biome).ifPresent(key ->
		// System.out.println(key.toString())));
	}
}

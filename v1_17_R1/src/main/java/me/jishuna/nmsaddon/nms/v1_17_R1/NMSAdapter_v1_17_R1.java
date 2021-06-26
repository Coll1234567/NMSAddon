package me.jishuna.nmsaddon.nms.v1_17_R1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;

import me.jishuna.nmsaddon.nms.NMSAdapter;
import me.jishuna.nmsaddon.nms.v1_17_R1.generation.PopulatorFeature;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration2;
import net.minecraft.world.level.levelgen.placement.WorldGenDecorator;

public class NMSAdapter_v1_17_R1 implements NMSAdapter {
	private static NMSAdapter_v1_17_R1 instance;
	
	public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(WorldGenFeatureEmptyConfiguration.a);
	public static final WorldGenFeatureConfigured<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE
			.b(WorldGenFeatureEmptyConfiguration.b).a(WorldGenDecorator.a.a(WorldGenFeatureEmptyConfiguration2.c));
	
	private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();
	private final Map<UUID, TerraWorld> worldMap = new HashMap<>();
	
	public NMSAdapter_v1_17_R1() {
		instance = this;
	}

	@Override
	public void registerListeners(TerraAddon addon, TerraPlugin plugin) {
		plugin.getEventManager().registerListener(addon, new WorldEventListener(plugin));
		plugin.getEventManager().registerListener(addon, new ConfigEventListener(plugin));
	}
	
	public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
		return templates;
	}

	@Override
	public Map<UUID, TerraWorld> getWorldMap() {
		return worldMap;
	}
	
	@Override
	public TerraWorld getWorld(UUID id) {
		TerraWorld world = worldMap.get(id);
		if (world == null)
			throw new IllegalArgumentException("No world exists with UUID " + id);
		return world;
	}

	public static NMSAdapter_v1_17_R1 getInstance() {
		return instance;
	}
}
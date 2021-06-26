package me.jishuna.nmsaddon.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.builder.BiomeBuilder;

import net.minecraft.resources.MinecraftKey;

public class PostLoadCompatibilityOptions_v1_17_R1 implements ConfigTemplate {
    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<MinecraftKey>> excludedPerBiomeStructures = new HashMap<>();

    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<MinecraftKey>> excludedPerBiomeFeatures = new HashMap<>();

    public Map<BiomeBuilder, Set<MinecraftKey>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }

    public Map<BiomeBuilder, Set<MinecraftKey>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }
}

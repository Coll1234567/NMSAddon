package me.jishuna.nmsaddon.nms;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import net.minecraft.resources.MinecraftKey;

public class PreLoadCompatibilityOptions_v1_17_R1 implements ConfigTemplate {
    @Value("features.inject-registry.enable")
    @Default
    private boolean doRegistryInjection = false;

    @Value("features.inject-biome.enable")
    @Default
    private boolean doBiomeInjection = false;

    @Value("features.inject-registry.excluded-features")
    @Default
    private Set<MinecraftKey> excludedRegistryFeatures = new HashSet<>();

    @Value("features.inject-biome.excluded-features")
    @Default
    private Set<MinecraftKey> excludedBiomeFeatures = new HashSet<>();

    @Value("structures.inject-biome.excluded-features")
    @Default
    private Set<MinecraftKey> excludedBiomeStructures = new HashSet<>();

    public boolean doBiomeInjection() {
        return doBiomeInjection;
    }

    public boolean doRegistryInjection() {
        return doRegistryInjection;
    }

    public Set<MinecraftKey> getExcludedBiomeFeatures() {
        return excludedBiomeFeatures;
    }

    public Set<MinecraftKey> getExcludedRegistryFeatures() {
        return excludedRegistryFeatures;
    }

    public Set<MinecraftKey> getExcludedBiomeStructures() {
        return excludedBiomeStructures;
    }
}

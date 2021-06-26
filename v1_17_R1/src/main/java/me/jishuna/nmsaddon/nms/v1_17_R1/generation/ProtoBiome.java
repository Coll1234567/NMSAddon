package me.jishuna.nmsaddon.nms.v1_17_R1.generation;

import com.dfsek.terra.api.platform.world.Biome;

import net.minecraft.core.IRegistryWritable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.biome.BiomeBase;

public class ProtoBiome implements Biome {
    private final MinecraftKey identifier;

    public ProtoBiome(MinecraftKey identifier) {
        this.identifier = identifier;
    }

    public BiomeBase get(IRegistryWritable<BiomeBase> registry) {
        return registry.get(identifier);
    }

    @Override
    public Object getHandle() {
        return identifier;
    }
}

package me.jishuna.nmsaddon;

import java.util.Objects;
import java.util.stream.Collectors;

import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;

public class TerraBiomeSource extends WorldChunkManager {
	public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder
			.create(config -> config.group(Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID()))
					.apply(config, config.stable(NMSAddon.getNMSAddon().getPlugin().getConfigRegistry()::get))));

	public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(RegistryLookupCodec.a(IRegistry.aO).forGetter(source -> source.biomeRegistry),
					Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed),
					PACK_CODEC.fieldOf("pack").stable().forGetter(source -> source.pack))
			.apply(instance, instance.stable(TerraBiomeSource::new)));

	private final IRegistry<BiomeBase> biomeRegistry;
	private final long seed;
	private final BiomeProvider grid;
	private final ConfigPack pack;

	public TerraBiomeSource(IRegistry<BiomeBase> biomes, long seed, ConfigPack pack) {
		super(biomes.g().filter(biome -> Objects.requireNonNull(biomes.getKey(biome)).getNamespace().equals("terra"))
				.collect(Collectors.toList()));

		this.biomeRegistry = biomes;
		this.seed = seed;
		this.grid = pack.getBiomeProviderBuilder().build(seed);
		this.pack = pack;
	}

	@Override
	public BiomeBase getBiome(int biomeX, int biomeY, int biomeZ) {
		UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(biomeX << 2, biomeZ << 2);
		return biomeRegistry.get(new MinecraftKey("terra", Utils.createBiomeID(pack, biome.getID())));
	}

	@Override
	protected Codec<? extends WorldChunkManager> a() {
		return CODEC;
	}

	@Override
	public WorldChunkManager a(long var1) {
		return new TerraBiomeSource(this.biomeRegistry, seed, pack);
	}

}

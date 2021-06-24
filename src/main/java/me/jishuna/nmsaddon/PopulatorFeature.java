package me.jishuna.nmsaddon;

import com.dfsek.terra.api.world.generation.Chunkified;
import com.mojang.serialization.Codec;

import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;

/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature extends WorldGenerator<WorldGenFeatureEmptyConfiguration> {
	public PopulatorFeature(Codec<WorldGenFeatureEmptyConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeaturePlaceContext<WorldGenFeatureEmptyConfiguration> context) {
		ChunkGenerator chunkGenerator = context.b();
		if (!(chunkGenerator instanceof NMSChunkGenerator))
			return true;
		GeneratorAccessSeed world = context.a();
		NMSChunkGenerator gen = (NMSChunkGenerator) chunkGenerator;
		gen.getHandle().getPopulators().forEach(populator -> {
			if (!(populator instanceof Chunkified)) {
				populator.populate(new NMSWorld((RegionLimitedWorldAccess) world),
						new NMSChunk((RegionLimitedWorldAccess) world));
			}
		});
		return true;
	}

}
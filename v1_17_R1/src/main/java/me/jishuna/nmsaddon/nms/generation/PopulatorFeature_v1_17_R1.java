package me.jishuna.nmsaddon.nms.generation;

import com.dfsek.terra.api.world.generation.Chunkified;
import com.mojang.serialization.Codec;

import me.jishuna.nmsaddon.nms.world.NMSChunk_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.NMSWorld_v1_17_R1;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;

/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature_v1_17_R1 extends WorldGenerator<WorldGenFeatureEmptyConfiguration> {
	public PopulatorFeature_v1_17_R1(Codec<WorldGenFeatureEmptyConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeaturePlaceContext<WorldGenFeatureEmptyConfiguration> context) {
		ChunkGenerator chunkGenerator = context.b();
		if (!(chunkGenerator instanceof NMSChunkGenerator_v1_17_R1))
			return true;
		GeneratorAccessSeed world = context.a();
		NMSChunkGenerator_v1_17_R1 gen = (NMSChunkGenerator_v1_17_R1) chunkGenerator;
		gen.getHandle().getPopulators().forEach(populator -> {
			if (!(populator instanceof Chunkified)) {
				populator.populate(new NMSWorld_v1_17_R1((RegionLimitedWorldAccess) world),
						new NMSChunk_v1_17_R1((RegionLimitedWorldAccess) world));
			}
		});
		return true;
	}

}
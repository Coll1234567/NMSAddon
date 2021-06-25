package me.jishuna.nmsaddon;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.generation.Chunkified;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldGenStage.Features;

public class NMSChunkGenerator extends ChunkGenerator implements GeneratorWrapper, com.dfsek.terra.api.platform.world.generator.ChunkGenerator {
	public static final Codec<ConfigPack> PACK_CODEC = RecordCodecBuilder
			.create(config -> config.group(Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID()))
					.apply(config, config.stable(NMSAddon.getNMSAddon().getPlugin().getConfigRegistry()::get)));

	public static final Codec<NMSChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(TerraBiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					PACK_CODEC.fieldOf("pack").stable().forGetter(generator -> generator.pack))
			.apply(instance, instance.stable(NMSChunkGenerator::new)));

	private static Field generatorAccessField;

	static {
		try {
			generatorAccessField = StructureManager.class.getDeclaredField("a");
			generatorAccessField.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	private final long seed;
	private final DefaultChunkGenerator3D delegate;
	private final TerraBiomeSource biomeSource;

	private final ConfigPack pack;
	private DimensionManager dimensionType;

	public NMSChunkGenerator(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
		super(biomeSource, new StructureSettings(false));
		this.pack = configPack;

		this.delegate = new DefaultChunkGenerator3D(pack, NMSAddon.getNMSAddon().getPlugin());
		delegate.getMain().logger().info("Loading world with config pack " + pack.getTemplate().getID());
		this.biomeSource = biomeSource;

		this.seed = seed;
	}

	@Override
	public TerraChunkGenerator getHandle() {
		return delegate;
	}

	@Override
	protected Codec<? extends ChunkGenerator> a() {
		return CODEC;
	}

	@Override
	public void buildBase(RegionLimitedWorldAccess p0, IChunkAccess p1) {
		// No-op

	}

	@Override
	public CompletableFuture<IChunkAccess> buildNoise(Executor executor, StructureManager accessor,
			IChunkAccess chunk) {
		return CompletableFuture.supplyAsync(() -> {
			World world;
			GeneratorAccess access;
			try {
				access = (GeneratorAccess) generatorAccessField.get(accessor);
				world = new NMSWorld((RegionLimitedWorldAccess) access);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			delegate.generateChunkData(world, new FastRandom(), chunk.getPos().b, chunk.getPos().c, new NMSChunkData((ProtoChunk) chunk));
			delegate.getPopulators().forEach(populator -> {
				if (populator instanceof Chunkified) {
					populator.populate(world, new NMSChunk((RegionLimitedWorldAccess) access));
				}
			});
			return chunk;
		}, executor);
	}

	@Override
	public BlockColumn getBaseColumn(int x, int z, LevelHeightAccessor view) {
		TerraWorld world = NMSAddon.getNMSAddon().getWorld(dimensionType);
		IBlockData[] array = new IBlockData[view.getHeight()];
		for (int y = view.getMinBuildHeight() + view.getHeight() - 1; y >= view.getMinBuildHeight(); y--) {
			array[y] = ((CraftBlockData) world.getUngeneratedBlock(x, y, z).getHandle()).getState();
		}
		return new BlockColumn(view.getMinBuildHeight(), array);
	}

	@Override
	public void doCarving(long i, BiomeManager biomemanager, IChunkAccess ichunkaccess, Features carver) {
		//if (pack.getTemplate().vanillaCaves()) {
			super.doCarving(i, biomemanager, ichunkaccess, carver);
		//}
	}

	@Override
	public int getBaseHeight(int x, int z, Type heightmap, LevelHeightAccessor heightmapType) {
		TerraWorld world = NMSAddon.getNMSAddon().getWorld(dimensionType);
		int height = world.getWorld().getMaxHeight();
		while (height >= world.getWorld().getMinHeight() && !heightmap.e()
				.test(((CraftBlockData) world.getUngeneratedBlock(x, height - 1, z).getHandle()).getState())) {
			height--;
		}
		return height;
	}

	@Override
	public ChunkGenerator withSeed(long p0) {
		return new NMSChunkGenerator((TerraBiomeSource) this.biomeSource.a(seed), seed, pack);
	}

	public void setDimensionType(DimensionManager dimensionType) {
		this.dimensionType = dimensionType;
	}
}

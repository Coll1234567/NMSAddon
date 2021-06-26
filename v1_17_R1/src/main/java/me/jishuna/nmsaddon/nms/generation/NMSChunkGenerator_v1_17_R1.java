package me.jishuna.nmsaddon.nms.generation;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.generation.Chunkified;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.jishuna.nmsaddon.NMSAddon;
import me.jishuna.nmsaddon.nms.NMSAdapter_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.DummyWorld_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.NMSChunkData_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.NMSChunk_v1_17_R1;
import me.jishuna.nmsaddon.nms.world.NMSWorld_v1_17_R1;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.level.BlockColumn;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.SpawnerCreature;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSettingsMobs.c;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldGenStage.Features;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;

public class NMSChunkGenerator_v1_17_R1 extends ChunkGenerator
		implements GeneratorWrapper, com.dfsek.terra.api.platform.world.generator.ChunkGenerator {
	private static Field generatorAccessField;

	static {
		try {
			generatorAccessField = StructureManager.class.getDeclaredField("a");
			generatorAccessField.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public static final Codec<ConfigPack> PACK_CODEC = RecordCodecBuilder
			.create(config -> config.group(Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID()))
					.apply(config, config.stable(NMSAddon.getInstance().getPlugin().getConfigRegistry()::get)));

	public static final Codec<NMSChunkGenerator_v1_17_R1> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(TerraBiomeSource_v1_17_R1.CODEC.fieldOf("biome_source")
					.forGetter(generator -> generator.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					PACK_CODEC.fieldOf("pack").stable().forGetter(generator -> generator.configPack))
			.apply(instance, instance.stable(NMSChunkGenerator_v1_17_R1::new)));

	private final long seed;
	private final DefaultChunkGenerator3D delegate;
	private final TerraBiomeSource_v1_17_R1 biomeSource;

	private final ConfigPack configPack;
	private UUID worldId;

	public NMSChunkGenerator_v1_17_R1(TerraBiomeSource_v1_17_R1 biomeSource, long seed, ConfigPack configPack) {
		super(biomeSource, new StructureSettings(false));
		this.configPack = configPack;

		this.delegate = new DefaultChunkGenerator3D(configPack, NMSAddon.getInstance().getPlugin());
		delegate.getMain().logger().info("Loading world with config pack " + configPack.getTemplate().getID());
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
				world = new NMSWorld_v1_17_R1((RegionLimitedWorldAccess) access);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			delegate.generateChunkData(world, new FastRandom(), chunk.getPos().b, chunk.getPos().c,
					new NMSChunkData_v1_17_R1((ProtoChunk) chunk));
			delegate.getPopulators().forEach(populator -> {
				if (populator instanceof Chunkified) {
					populator.populate(world, new NMSChunk_v1_17_R1((RegionLimitedWorldAccess) access));
				}
			});
			return chunk;
		}, executor);
	}

	@Override
	public BlockColumn getBaseColumn(int x, int z, LevelHeightAccessor view) {
		TerraWorld world = NMSAdapter_v1_17_R1.getInstance().getWorld(worldId);
		IBlockData[] array = new IBlockData[view.getHeight()];
		for (int y = view.getMinBuildHeight() + view.getHeight() - 1; y >= view.getMinBuildHeight(); y--) {
			array[y] = ((CraftBlockData) world.getUngeneratedBlock(x, y, z).getHandle()).getState();
		}
		return new BlockColumn(view.getMinBuildHeight(), array);
	}

	@Override
	public void doCarving(long i, BiomeManager biomemanager, IChunkAccess ichunkaccess, Features carver) {
		if (configPack.getTemplate().vanillaCaves()) {
			super.doCarving(i, biomemanager, ichunkaccess, carver);
		}
	}

	@Override
	public int getBaseHeight(int x, int z, Type heightmap, LevelHeightAccessor heightmapType) {
		TerraWorld world = NMSAdapter_v1_17_R1.getInstance().getWorld(worldId);
		int height = world.getWorld().getMaxHeight();
		while (height >= world.getWorld().getMinHeight() && !heightmap.e()
				.test(((CraftBlockData) world.getUngeneratedBlock(x, height - 1, z).getHandle()).getState())) {
			height--;
		}
		return height;
	}

	@Override
	public void createStructures(IRegistryCustom iregistrycustom, StructureManager structuremanager,
			IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i) {
		if (configPack.getTemplate().vanillaStructures()) {
			super.createStructures(iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i);
		}
	}

	@Override
	public boolean a(ChunkCoordIntPair chunkcoordintpair) {
		if (configPack.getTemplate().vanillaStructures()) {
			return super.a(chunkcoordintpair);
		}
		return false;
	}

	@Override
	public @Nullable BlockPosition findNearestMapFeature(WorldServer worldserver,
			StructureGenerator<?> structuregenerator, BlockPosition blockposition, int i, boolean flag) {
		if (!configPack.getTemplate().disableStructures()) {
			String name = java.util.Objects.requireNonNull(IRegistry.aW.getKey(structuregenerator)).toString();
			TerraWorld terraWorld = NMSAdapter_v1_17_R1.getInstance().getWorld(worldId);
			TerraStructure located = configPack.getStructure(configPack.getTemplate().getLocatable().get(name));
			if (located != null) {
				CompletableFuture<BlockPosition> result = new CompletableFuture<>();
				AsyncStructureFinder finder = new AsyncStructureFinder(terraWorld.getBiomeProvider(), located,
						adapt(blockposition).toLocation(new DummyWorld_v1_17_R1(worldserver)), 0, 500, location -> {
							result.complete(adapt(location));
						}, NMSAddon.getInstance().getPlugin());
				finder.run(); // Do this synchronously.
				try {
					return result.get();
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return super.findNearestMapFeature(worldserver, structuregenerator, blockposition, i, flag);
	}

	@Override
	public void addMobs(RegionLimitedWorldAccess region) {
		if (configPack.getTemplate().vanillaMobs()) {
			int cx = region.a().b;
			int cy = region.a().c;
			BiomeBase biome = region.getBiome((new ChunkCoordIntPair(cx, cy)).l());
			SeededRandom chunkRandom = new SeededRandom();
			chunkRandom.a(region.getSeed(), cx << 4, cy << 4);
			SpawnerCreature.a(region, biome, region.a(), chunkRandom);
		}
	}

	@Override
	public WeightedRandomList<c> getMobsFor(BiomeBase biomebase, StructureManager structuremanager,
			EnumCreatureType enumcreaturetype, BlockPosition pos) {
		if (structuremanager.a(pos, true, StructureGenerator.j).e()) {
			if (enumcreaturetype == EnumCreatureType.a) {
				return StructureGenerator.j.c();
			}

			if (enumcreaturetype == EnumCreatureType.b) {
				return StructureGenerator.j.h();
			}
		}

		if (enumcreaturetype == EnumCreatureType.a) {
			if (structuremanager.a(pos, false, StructureGenerator.b).e()) {
				return StructureGenerator.b.c();
			}

			if (structuremanager.a(pos, false, StructureGenerator.l).e()) {
				return StructureGenerator.l.c();
			}

			if (structuremanager.a(pos, true, StructureGenerator.n).e()) {
				return StructureGenerator.n.c();
			}
		}

		return enumcreaturetype == EnumCreatureType.d && structuremanager.a(pos, false, StructureGenerator.l).e()
				? StructureGenerator.l.i()
				: super.getMobsFor(biomebase, structuremanager, enumcreaturetype, pos);

	}

	@Override
	public ChunkGenerator withSeed(long p0) {
		return new NMSChunkGenerator_v1_17_R1((TerraBiomeSource_v1_17_R1) this.biomeSource.a(seed), seed, configPack);
	}

	public void setWorldId(UUID id) {
		this.worldId = id;
	}

	private Vector3 adapt(BlockPosition pos) {
		return new Vector3(pos.getX(), pos.getY(), pos.getZ());
	}

	private BlockPosition adapt(Vector3 v) {
		return new BlockPosition(v.getBlockX(), v.getBlockY(), v.getBlockZ());
	}
}

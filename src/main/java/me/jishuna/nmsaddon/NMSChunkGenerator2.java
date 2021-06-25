package me.jishuna.nmsaddon;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.generator.CustomChunkGenerator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.population.CavePopulator;
import com.dfsek.terra.world.population.FloraPopulator;
import com.dfsek.terra.world.population.OrePopulator;
import com.dfsek.terra.world.population.StructurePopulator;
import com.dfsek.terra.world.population.TreePopulator;

import net.minecraft.core.IRegistry;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.chunk.BiomeStorage;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;

public class NMSChunkGenerator2 extends CustomChunkGenerator {

	private final CavePopulator cavePopulator;
	private final StructurePopulator structurePopulator;
	private final OrePopulator orePopulator;
	private final TreePopulator treePopulator;
	private final FloraPopulator floraPopulator;
	private final WorldChunkManager source;

	public NMSChunkGenerator2(WorldServer world, ChunkGenerator chunkGenerator,
			org.bukkit.generator.ChunkGenerator generator, TerraPlugin main, ConfigPack pack) {
		super(world, chunkGenerator, generator);

		System.out.println("Instantiated janky generator.");
		this.cavePopulator = new CavePopulator(main);
		this.structurePopulator = new StructurePopulator(main);
		this.orePopulator = new OrePopulator(main);
		this.treePopulator = new TreePopulator(main);
		this.floraPopulator = new FloraPopulator(main);
		this.source = new TerraBiomeSource(((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO),
				world.getSeed(), pack);
	}
	
	//TODO back to NMS version?

	@Override
	public void addDecorations(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager) {
		super.addDecorations(regionlimitedworldaccess, structuremanager);
		World world = new NMSWorld(regionlimitedworldaccess);
		com.dfsek.terra.api.platform.world.Chunk chunk = new NMSChunk(regionlimitedworldaccess);
		cavePopulator.populate(world, chunk);
		structurePopulator.populate(world, chunk);
		orePopulator.populate(world, chunk);
		treePopulator.populate(world, chunk);
		floraPopulator.populate(world, chunk);
	}

	@Override
	public void buildBase(RegionLimitedWorldAccess regionlimitedworldaccess, IChunkAccess ichunkaccess) {
		super.buildBase(regionlimitedworldaccess, ichunkaccess);
		createBiomes(((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO), ichunkaccess);
	}

	@Override
	public void createBiomes(IRegistry<BiomeBase> iregistry, IChunkAccess ichunkaccess) {
		ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();
		((ProtoChunk) ichunkaccess).a(new BiomeStorage(iregistry, ichunkaccess, chunkcoordintpair, this.source));
	}
}

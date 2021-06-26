package me.jishuna.nmsaddon.nms.v1_17_R1.world;

import java.util.Random;

import org.bukkit.TreeType;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;

import net.minecraft.core.BlockPosition;
import net.minecraft.data.worldgen.BiomeDecoratorGroups;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.block.BlockChorusFlower;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;

public class NMSTree implements Tree {
    private final TerraPlugin main;
    private final TreeType type;

    public NMSTree(TerraPlugin main, TreeType type) {
        this.main = main;
        this.type = type;
    }

    @Override
    public boolean plant(Location loc, Random random) {
        BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        RegionLimitedWorldAccess world = ((NMSWorld) loc.getWorld()).getDelegate();

        WorldGenFeatureConfigured<?, ?> gen;
        switch (type) {
            case BIG_TREE:
                gen = BiomeDecoratorGroups.cK;
                break;
            case BIRCH:
                gen = BiomeDecoratorGroups.cF;
                break;
            case REDWOOD:
                gen = BiomeDecoratorGroups.cH;
                break;
            case TALL_REDWOOD:
                gen = BiomeDecoratorGroups.cI;
                break;
            case JUNGLE:
                gen = BiomeDecoratorGroups.cM;
                break;
            case SMALL_JUNGLE:
                gen = BiomeDecoratorGroups.cL;
                break;
            case COCOA_TREE:
                gen = BiomeDecoratorGroups.cJ;
                break;
            case JUNGLE_BUSH:
                gen = BiomeDecoratorGroups.cR;
                break;
            case RED_MUSHROOM:
                gen = BiomeDecoratorGroups.cC;
                break;
            case BROWN_MUSHROOM:
                gen = BiomeDecoratorGroups.cB;
                break;
            case SWAMP:
                gen = BiomeDecoratorGroups.cQ;
                break;
            case ACACIA:
                gen = BiomeDecoratorGroups.cG;
                break;
            case DARK_OAK:
                gen = BiomeDecoratorGroups.cE;
                break;
            case MEGA_REDWOOD:
                gen = BiomeDecoratorGroups.cO;
                break;
            case TALL_BIRCH:
                gen = BiomeDecoratorGroups.cP;
                break;
            case CHORUS_PLANT:
                BlockChorusFlower.a(world, pos, random, 8);
                return true;
            case CRIMSON_FUNGUS:
                gen = BiomeDecoratorGroups.cx;
                break;
            case WARPED_FUNGUS:
                gen = BiomeDecoratorGroups.cz;
                break;
            case TREE:
            default:
                gen = BiomeDecoratorGroups.cD;
                break;
        }

        return gen.a(world, world.getMinecraftWorld().getChunkProvider().getChunkGenerator(), random, pos);
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createBlockData("minecraft:grass_block"),
                main.getWorldHandle().createBlockData("minecraft:podzol"),
                main.getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}

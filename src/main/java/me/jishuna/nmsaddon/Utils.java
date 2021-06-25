package me.jishuna.nmsaddon;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeFog.a;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.levelgen.WorldGenStage;
import net.minecraft.world.level.levelgen.WorldGenStage.Decoration;
import net.minecraft.world.level.levelgen.WorldGenStage.Features;
import net.minecraft.world.level.levelgen.carver.WorldGenCarverWrapper;

public class Utils {

	private static final Transformer<String, ProtoBiome> biomeFixer = new Transformer.Builder<String, ProtoBiome>()
			.addTransform(Utils::parseBiome, Validator.notNull())
			.addTransform(id -> parseBiome("minecraft:" + id.toLowerCase()), Validator.notNull()).build();

	private static ProtoBiome parseBiome(String id) {
		MinecraftKey identifier = MinecraftKey.a(id);
		if (RegistryGeneration.i.get(identifier) == null)
			return null; // failure.
		return new ProtoBiome(identifier);
	}

	public static String createBiomeID(ConfigPack pack, String biomeID) {
		return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
	}

	public static BiomeBase createBiome(BiomeBuilder biome, ConfigPack pack) {
		BiomeTemplate template = biome.getTemplate();
		Map<String, Integer> colors = template.getColors();

		IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().l.b(IRegistry.aO);
		org.bukkit.block.Biome bukkitbiome = ((org.bukkit.block.Biome) (new ArrayList<>(
				biome.getVanillaBiomes().getContents()).get(0).getHandle()));
		BiomeBase vanilla = biomeFixer.translate(bukkitbiome.toString()).get(biomeRegistry);

		BiomeSettingsGeneration.a generationSettings = new BiomeSettingsGeneration.a();

		generationSettings.a(vanilla.e().d()); // It needs a surfacebuilder, even though we dont use it.

		//generationSettings.a(WorldGenStage.Decoration.i, NMSAddon.POPULATOR_CONFIGURED_FEATURE);

		if (true || pack.getTemplate().vanillaCaves()) {
			for (WorldGenStage.Features carver : WorldGenStage.Features.values()) {
				for (Supplier<WorldGenCarverWrapper<?>> configuredCarverSupplier : vanilla.e().a(carver)) {
					generationSettings.a(carver, configuredCarverSupplier.get());
				}
			}
		}

		BiomeFog accessor = vanilla.l();
		BiomeFog.a effects = new BiomeFog.a().b(colors.getOrDefault("water", accessor.b()))
				.c(colors.getOrDefault("water-fog", accessor.c())).a(colors.getOrDefault("fog", accessor.a()))
				.d(colors.getOrDefault("sky", accessor.d())).a(accessor.g());

		if (colors.containsKey("grass")) {
			effects.f(colors.get("grass"));
		} else {
			accessor.f().ifPresent(effects::f);
		}
		if (colors.containsKey("foliage")) {
			effects.e(colors.get("foliage"));
		} else {
			accessor.e().ifPresent(effects::e);
		}

		return new BiomeBase.a().a(vanilla.c()).a(vanilla.t()).a(vanilla.h()).b(vanilla.j()).c(vanilla.k())
				.d(vanilla.getHumidity()).a(effects.a()).a(vanilla.b()).a(generationSettings.a()).a();
	}

	public static <T> void registerOrOverwrite(IRegistry<T> registry, ResourceKey<IRegistry<T>> key,
			MinecraftKey identifier, T item) {
		if (registry.c(identifier)) {
			((IRegistryWritable<T>) registry).a(registry.getId(registry.get(identifier)),
					ResourceKey.a(key, identifier), item, Lifecycle.stable());
		} else {
			IRegistry.a(registry, identifier, item);
		}
	}

}

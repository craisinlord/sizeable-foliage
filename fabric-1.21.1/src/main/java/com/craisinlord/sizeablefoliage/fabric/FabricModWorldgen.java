package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.Constants;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Wires the sparse natural generation for the 4 foliage blocks into vanilla overworld biomes. */
public final class FabricModWorldgen {
    private static final TagKey<Biome> VERY_SHORT_GRASS_SPAWN_BIOMES = biomeTag("very_short_grass_spawn_biomes");
    private static final TagKey<Biome> VERY_TALL_GRASS_SPAWN_BIOMES = biomeTag("very_tall_grass_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_1X1_SPAWN_BIOMES = biomeTag("big_bush_1x1_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_2X2_SPAWN_BIOMES = biomeTag("big_bush_2x2_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_2X2_VERY_COMMON_SPAWN_BIOMES = biomeTag("big_bush_2x2_very_common_spawn_biomes");
    private static final TagKey<Biome> MEGA_TAIGA_SPAWN_BIOMES = biomeTag("mega_taiga_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_3X3_QUITE_COMMON_SPAWN_BIOMES = biomeTag("big_bush_3x3_quite_common_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_3X3_UNCOMMON_SPAWN_BIOMES = biomeTag("big_bush_3x3_uncommon_spawn_biomes");
    private static final TagKey<Biome> BIG_BUSH_3X3_RARE_SPAWN_BIOMES = biomeTag("big_bush_3x3_rare_spawn_biomes");

    private FabricModWorldgen() {
    }

    public static void init() {
        addFeature("big_bush", BIG_BUSH_1X1_SPAWN_BIOMES);

        addFeature("very_short_grass", VERY_SHORT_GRASS_SPAWN_BIOMES);
        addFeature("very_tall_grass", VERY_TALL_GRASS_SPAWN_BIOMES);
        addFeature("big_bush_2x2", BIG_BUSH_2X2_SPAWN_BIOMES);
        addFeature("big_bush_2x2_very_common", BIG_BUSH_2X2_VERY_COMMON_SPAWN_BIOMES);
        addFeature("big_bush_3x3", MEGA_TAIGA_SPAWN_BIOMES);
        addFeature("big_bush_3x3_quite_common", BIG_BUSH_3X3_QUITE_COMMON_SPAWN_BIOMES);
        addFeature("big_bush_3x3_uncommon", BIG_BUSH_3X3_UNCOMMON_SPAWN_BIOMES);
        addFeature("big_bush_3x3_rare", BIG_BUSH_3X3_RARE_SPAWN_BIOMES);
    }

    private static TagKey<Biome> biomeTag(String path) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
    }

    private static void addFeature(String path, TagKey<Biome> biomeTag) {
        addFeature(path, BiomeSelectors.tag(biomeTag));
    }

    private static void addFeature(String path, java.util.function.Predicate<net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext> selector) {
        ResourceKey<PlacedFeature> key = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
        BiomeModifications.addFeature(selector, GenerationStep.Decoration.VEGETAL_DECORATION, key);
    }
}

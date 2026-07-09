package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.content.worldgen.VeryTallGrassFeature;
import com.craisinlord.sizeablefoliage.registry.ModFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class FabricModFeatures {
    public static final VeryTallGrassFeature VERY_TALL_GRASS = register("very_tall_grass", ModFeatures.createVeryTallGrassFeature());
    public static final BigBushWorldgenFeature BIG_BUSH_2X2 = register("big_bush_2x2", ModFeatures.createBigBush2x2Feature());
    public static final BigBushWorldgenFeature BIG_BUSH_3X3 = register("big_bush_3x3", ModFeatures.createBigBush3x3Feature());

    private FabricModFeatures() {
    }

    public static void init() {
    }

    private static <T extends Feature<?>> T register(String path, T feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path), feature);
    }
}

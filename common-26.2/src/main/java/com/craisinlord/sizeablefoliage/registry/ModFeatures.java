package com.craisinlord.sizeablefoliage.registry;

import com.craisinlord.sizeablefoliage.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.BigSweetBerryBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.VeryTallGrassFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Loader-agnostic feature factory methods, mirroring {@link ModBlocks}. */
public final class ModFeatures {
    private ModFeatures() {
    }

    public static VeryTallGrassFeature createVeryTallGrassFeature() {
        return new VeryTallGrassFeature(NoneFeatureConfiguration.CODEC);
    }

    public static BigBushWorldgenFeature createBigBush2x2Feature() {
        return new BigBushWorldgenFeature(NoneFeatureConfiguration.CODEC, 1);
    }

    public static BigBushWorldgenFeature createBigBush3x3Feature() {
        return new BigBushWorldgenFeature(NoneFeatureConfiguration.CODEC, 2);
    }

    public static BigSweetBerryBushWorldgenFeature createBigSweetBerryBushFeature() {
        return new BigSweetBerryBushWorldgenFeature(NoneFeatureConfiguration.CODEC);
    }
}

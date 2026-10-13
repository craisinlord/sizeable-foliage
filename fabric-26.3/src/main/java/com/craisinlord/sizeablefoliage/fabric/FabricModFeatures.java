package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.BigSweetBerryBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.VeryTallGrassFeature;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class FabricModFeatures {
    public static final MapCodec<VeryTallGrassFeature> VERY_TALL_GRASS = register("very_tall_grass", VeryTallGrassFeature.CODEC);
    public static final MapCodec<BigBushWorldgenFeature> BIG_BUSH = register("big_bush", BigBushWorldgenFeature.CODEC);
    public static final MapCodec<BigSweetBerryBushWorldgenFeature> BIG_SWEET_BERRY_BUSH = register("big_sweet_berry_bush", BigSweetBerryBushWorldgenFeature.CODEC);

    private FabricModFeatures() {
    }

    public static void init() {
    }

    private static <T extends Feature> MapCodec<T> register(String path, MapCodec<T> codec) {
        Registry.register(BuiltInRegistries.FEATURE_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path), codec);
        return codec;
    }
}

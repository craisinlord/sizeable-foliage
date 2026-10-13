package com.craisinlord.sizeablefoliage.neoforge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.BigSweetBerryBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.VeryTallGrassFeature;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModFeatures {
    private static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<VeryTallGrassFeature>> VERY_TALL_GRASS =
            FEATURE_TYPES.register("very_tall_grass", () -> VeryTallGrassFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<BigBushWorldgenFeature>> BIG_BUSH =
            FEATURE_TYPES.register("big_bush", () -> BigBushWorldgenFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<BigSweetBerryBushWorldgenFeature>> BIG_SWEET_BERRY_BUSH =
            FEATURE_TYPES.register("big_sweet_berry_bush", () -> BigSweetBerryBushWorldgenFeature.CODEC);

    private NeoForgeModFeatures() {
    }

    public static void register(IEventBus modEventBus) {
        FEATURE_TYPES.register(modEventBus);
    }
}

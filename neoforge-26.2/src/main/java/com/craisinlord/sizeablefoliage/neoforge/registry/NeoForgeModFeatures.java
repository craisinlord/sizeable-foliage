package com.craisinlord.sizeablefoliage.neoforge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.registry.ModFeatures;
import com.craisinlord.sizeablefoliage.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.BigSweetBerryBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.worldgen.VeryTallGrassFeature;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModFeatures {
    private static final DeferredRegister<net.minecraft.world.level.levelgen.feature.Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    public static final DeferredHolder<net.minecraft.world.level.levelgen.feature.Feature<?>, VeryTallGrassFeature> VERY_TALL_GRASS =
            FEATURES.register("very_tall_grass", ModFeatures::createVeryTallGrassFeature);
    public static final DeferredHolder<net.minecraft.world.level.levelgen.feature.Feature<?>, BigBushWorldgenFeature> BIG_BUSH_2X2 =
            FEATURES.register("big_bush_2x2", ModFeatures::createBigBush2x2Feature);
    public static final DeferredHolder<net.minecraft.world.level.levelgen.feature.Feature<?>, BigBushWorldgenFeature> BIG_BUSH_3X3 =
            FEATURES.register("big_bush_3x3", ModFeatures::createBigBush3x3Feature);
    public static final DeferredHolder<net.minecraft.world.level.levelgen.feature.Feature<?>, BigSweetBerryBushWorldgenFeature> BIG_SWEET_BERRY_BUSH =
            FEATURES.register("big_sweet_berry_bush", ModFeatures::createBigSweetBerryBushFeature);

    private NeoForgeModFeatures() {
    }

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}

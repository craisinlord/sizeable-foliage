package com.craisinlord.sizeablefoliage.forge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.worldgen.BigBushWorldgenFeature;
import com.craisinlord.sizeablefoliage.content.worldgen.VeryTallGrassFeature;
import com.craisinlord.sizeablefoliage.registry.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeModFeatures {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    public static final RegistryObject<VeryTallGrassFeature> VERY_TALL_GRASS = FEATURES.register("very_tall_grass", ModFeatures::createVeryTallGrassFeature);
    public static final RegistryObject<BigBushWorldgenFeature> BIG_BUSH_2X2 = FEATURES.register("big_bush_2x2", ModFeatures::createBigBush2x2Feature);
    public static final RegistryObject<BigBushWorldgenFeature> BIG_BUSH_3X3 = FEATURES.register("big_bush_3x3", ModFeatures::createBigBush3x3Feature);

    private ForgeModFeatures() {
    }

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}

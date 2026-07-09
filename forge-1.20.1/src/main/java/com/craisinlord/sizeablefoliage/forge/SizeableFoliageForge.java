package com.craisinlord.sizeablefoliage.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.SizeableFoliage;
import com.craisinlord.sizeablefoliage.forge.registry.ForgeModBlocks;
import com.craisinlord.sizeablefoliage.forge.registry.ForgeModFeatures;

@Mod(Constants.MOD_ID)
public class SizeableFoliageForge {
    public SizeableFoliageForge() {
        SizeableFoliage.init();
        ForgeModBlocks.register(FMLJavaModLoadingContext.get().getModEventBus());
        ForgeModFeatures.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

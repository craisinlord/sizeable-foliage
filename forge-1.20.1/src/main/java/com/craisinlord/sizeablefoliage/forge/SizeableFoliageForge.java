package com.craisinlord.sizeablefoliage.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.ModCompostables;
import com.craisinlord.sizeablefoliage.SizeableFoliage;
import com.craisinlord.sizeablefoliage.forge.registry.ForgeModBlocks;
import com.craisinlord.sizeablefoliage.forge.registry.ForgeModFeatures;

@Mod(Constants.MOD_ID)
public class SizeableFoliageForge {
    public SizeableFoliageForge() {
        SizeableFoliage.init();
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeModBlocks.register(modEventBus);
        ForgeModFeatures.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ModCompostables.register(
                ForgeModBlocks.BIG_BUSH_ITEM.get(),
                ForgeModBlocks.TORCHFLOWER_BUSH_ITEM.get(),
                ForgeModBlocks.VERY_SHORT_GRASS_ITEM.get(),
                ForgeModBlocks.VERY_TALL_GRASS_ITEM.get()
        );
    }
}

package com.craisinlord.sizeablefoliage.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.ModCompostables;
import com.craisinlord.sizeablefoliage.SizeableFoliage;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModBlocks;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModFeatures;

@Mod(Constants.MOD_ID)
public class SizeableFoliageNeoForge {
    public SizeableFoliageNeoForge(IEventBus modEventBus) {
        SizeableFoliage.init();
        NeoForgeModBlocks.register(modEventBus);
        NeoForgeModFeatures.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ModCompostables.register(
                NeoForgeModBlocks.BIG_BUSH_ITEM.get(),
                NeoForgeModBlocks.TORCHFLOWER_BUSH_ITEM.get(),
                NeoForgeModBlocks.VERY_SHORT_GRASS_ITEM.get(),
                NeoForgeModBlocks.VERY_TALL_GRASS_ITEM.get()
        );
    }
}

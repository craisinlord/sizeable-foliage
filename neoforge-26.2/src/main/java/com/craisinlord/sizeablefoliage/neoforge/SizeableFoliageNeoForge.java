package com.craisinlord.sizeablefoliage.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.SizeableFoliage;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModBlocks;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModFeatures;

@Mod(Constants.MOD_ID)
public class SizeableFoliageNeoForge {
    public SizeableFoliageNeoForge(IEventBus modEventBus) {
        SizeableFoliage.init();
        NeoForgeModBlocks.register(modEventBus);
        NeoForgeModFeatures.register(modEventBus);
    }
}

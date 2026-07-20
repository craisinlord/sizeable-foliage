package com.craisinlord.sizeablefoliage.fabric;

import net.fabricmc.api.ModInitializer;
import com.craisinlord.sizeablefoliage.ModCompostables;
import com.craisinlord.sizeablefoliage.SizeableFoliage;

public class SizeableFoliageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SizeableFoliage.init();
        FabricModBlocks.init();
        FabricModFeatures.init();
        FabricModWorldgen.init();
        ModCompostables.register(
                FabricModBlocks.TORCHFLOWER_BUSH_ITEM,
                FabricModBlocks.VERY_SHORT_GRASS_ITEM,
                FabricModBlocks.VERY_TALL_GRASS_ITEM
        );
    }
}
